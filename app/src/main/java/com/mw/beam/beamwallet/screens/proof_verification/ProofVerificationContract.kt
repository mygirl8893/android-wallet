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

package com.mw.beam.beamwallet.screens.proof_verification

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.entities.PaymentProof

interface ProofVerificationContract {

    interface View: MvpView {
        fun showErrorProof()
        fun hideErrorProof()
        fun showProof(proof: PaymentProof)
        fun showCopiedMessage()
        fun getDetailsContent(proof: PaymentProof): String
        fun clear()
    }

    interface Presenter: MvpPresenter<View> {
        fun onProofCodeChanged(proof: String)
        fun onCopyDetailsPressed()
    }

    interface Repository: MvpRepository {
        fun getVerifyPaymentProof(proof: String): PaymentProof?
    }

}