/*
 * // Copyright 2018 Beam Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 */

package com.mw.beam.beamwallet.screens.transaction_details

import android.view.Menu
import com.mw.beam.beamwallet.base_screen.BasePresenter
import com.mw.beam.beamwallet.core.AppConfig
import com.mw.beam.beamwallet.core.entities.Utxo
import com.mw.beam.beamwallet.core.helpers.TxSender
import com.mw.beam.beamwallet.core.helpers.TxStatus
import io.reactivex.disposables.Disposable

/**
 * Created by vain onnellinen on 10/18/18.
 */
class TransactionDetailsPresenter(currentView: TransactionDetailsContract.View, currentRepository: TransactionDetailsContract.Repository, private val state: TransactionDetailsState)
    : BasePresenter<TransactionDetailsContract.View, TransactionDetailsContract.Repository>(currentView, currentRepository),
        TransactionDetailsContract.Presenter {
    private val COPY_TAG = "PROOF"

    private lateinit var utxosByTxSubscription: Disposable
    private lateinit var txUpdateSubscription: Disposable
    private lateinit var paymentProofSubscription: Disposable

    override fun onCreate() {
        super.onCreate()
        state.txDescription = view?.getTransactionDetails()
    }

    override fun onViewCreated() {
        super.onViewCreated()
        state.txDescription?.let {
            val senderAddress = if (it.sender.value) it.myId else it.peerId
            val receiverAddress = if (it.sender.value) it.peerId else it.myId

            view?.configCategoryAddresses(repository.getCategoryForAddress(senderAddress), repository.getCategoryForAddress(receiverAddress))
        }
    }

    override fun initSubscriptions() {
        super.initSubscriptions()

        utxosByTxSubscription = repository.getUtxoByTx(state.txDescription!!.id).subscribe { utxos ->
            if (!utxos.isNullOrEmpty()) {
                updateUtxos(utxos)
            }
        }

        txUpdateSubscription = repository.getTxStatus().subscribe { data ->
            state.configTransactions(data.tx)
            data.tx?.firstOrNull { it.id == state.txDescription?.id }?.let {
                state.txDescription = it
                repository.getUtxoByTx(state.txDescription!!.id)

                view?.init(it, repository.isPrivacyModeEnabled())

                if (canRequestProof()) {
                    repository.requestProof(it.id)
                }
            }
        }

        paymentProofSubscription = repository.getPaymentProof(state.txDescription!!.id, canRequestProof()).subscribe {
            if (it.txId == state.txDescription?.id) {
                state.paymentProof = it
                view?.updatePaymentProof(it)
            }
        }
    }

    override fun onOpenInBlockExplorerPressed() {
        if (state.txDescription == null) {
            return
        }

        if (repository.isAllowOpenExternalLink()) {
            view?.openExternalLink(AppConfig.buildTransactionLink(state.txDescription!!.kernelId))
        } else {
            view?.showOpenLinkAlert()
        }
    }

    override fun onOpenLinkPressed() {
        if (state.txDescription == null) {
            return
        }

        view?.openExternalLink(AppConfig.buildTransactionLink(state.txDescription!!.kernelId))
    }

    private fun updateUtxos(utxos: List<Utxo>) {
        view?.updateUtxos(utxos.map { utxo ->
            var type = UtxoType.Exchange

            if (state.txDescription?.selfTx == false && !isExchangeUtxo(utxo)) {
                type = if (state.txDescription?.id == utxo.createTxId) {
                    UtxoType.Receive
                } else {
                    UtxoType.Send
                }
            }

            UtxoInfoItem(type, utxo.amount)
        }, repository.isPrivacyModeEnabled())
    }

    private fun isExchangeUtxo(utxo: Utxo): Boolean {
        return state.transactions.values.any { (it.id == utxo.createTxId || it.id == utxo.spentTxId) && it.selfTx }
    }

    private fun canRequestProof(): Boolean {
        if (state.txDescription == null) {
            return false
        }

        return state.txDescription!!.sender == TxSender.SENT && !state.txDescription!!.selfTx && state.txDescription!!.status == TxStatus.Completed
    }

    override fun onCopyPaymentProof() {
        state.paymentProof?.let {
            view?.copyToClipboard(it.rawProof, COPY_TAG)
            view?.showCopiedAlert()
        }
    }

    override fun onShowPaymentProof() {
        if (state.paymentProof == null) return
        view?.showPaymentProof(state.paymentProof!!)
    }

    override fun getSubscriptions(): Array<Disposable>? = arrayOf(utxosByTxSubscription, txUpdateSubscription, paymentProofSubscription)

    override fun onMenuCreate(menu: Menu?) {
        view?.configMenuItems(menu, state.txDescription?.status ?: return)
    }

    override fun onCancelTransaction() {
        repository.cancelTransaction(state.txDescription)
        view?.finishScreen()
    }

    override fun onDeleteTransaction() {
        repository.deleteTransaction(state.txDescription)
        view?.finishScreen()
    }

    override fun onStart() {
        super.onStart()
        view?.init(state.txDescription ?: return, repository.isPrivacyModeEnabled())
    }
}
