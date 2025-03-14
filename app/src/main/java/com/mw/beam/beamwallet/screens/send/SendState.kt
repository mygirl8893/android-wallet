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

package com.mw.beam.beamwallet.screens.send

import com.mw.beam.beamwallet.core.entities.WalletAddress
import com.mw.beam.beamwallet.core.entities.WalletStatus

/**
 * Created by vain onnellinen on 1/2/19.
 */
class SendState {
    var walletStatus: WalletStatus? = null
    var isChangeForbidden = false
    var privacyMode = false
    var isTokenEmpty = true
    var isTokenValid = true
    var expiredAddresses = listOf<WalletAddress>()
    var scannedAddress : String? = null
    var oldToken : String? = null
    var scannedAmount: Double? = null
}
