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

package com.mw.beam.beamwallet.screens.address_details

import com.mw.beam.beamwallet.base_screen.BaseRepository
import com.mw.beam.beamwallet.core.entities.OnTxStatusData
import com.mw.beam.beamwallet.core.helpers.Category
import com.mw.beam.beamwallet.core.helpers.CategoryHelper
import com.mw.beam.beamwallet.core.listeners.WalletListener
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 3/4/19.
 */
class AddressRepository : BaseRepository(), AddressContract.Repository {

    override fun deleteAddress(addressId: String) {
        CategoryHelper.changeCategoryForAddress(addressId, null)
        getResult("deleteAddress") {
            wallet?.deleteAddress(addressId)
        }
    }

    override fun getTxStatus(): Subject<OnTxStatusData> {
        return getResult(WalletListener.subOnTxStatus, "getTxStatus") {
            wallet?.getWalletStatus()
        }
    }

    override fun getCategory(address: String): Category? {
        return CategoryHelper.getCategoryForAddress(address)
    }
}
