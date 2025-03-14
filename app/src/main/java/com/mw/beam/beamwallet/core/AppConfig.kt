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

package com.mw.beam.beamwallet.core

import com.mw.beam.beamwallet.BuildConfig
import java.util.*

/**
 * Created by vain onnellinen on 10/1/18.
 */
object AppConfig {
    const val APP_TAG = "BeamWallet"
    const val APP_VERSION = "version: ${BuildConfig.VERSION_NAME} code: ${BuildConfig.VERSION_CODE}"
    const val FLAVOR_MAINNET = "mainnet"
    const val FLAVOR_MASTERNET = "masternet"
    const val FLAVOR_TESTNET = "beamtestnet"
    const val LOG_CLEAN_TIME: Long = 259200000
    const val LOG_PATTERN = "{d yyyy-MM-dd hh:mm:ss.SSS} {l}/{t}: {m}"
    const val SHARE_TYPE = "text/plain"
    const val SHARE_VALUE = "Logs"
    const val AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider"
    const val SUPPORT_EMAIL = "support@beam.mw"
    const val DB_FILE_NAME = "wallet.db"
    const val NODE_DB_FILE_NAME = "node.db"
    const val NODE_JOURNAL_FILE_NAME = "node.db-journal"
    const val BEAM_SITE_LINK = "https://www.beam.mw/"
    const val BEAM_EXCHANGES_LINK = "$BEAM_SITE_LINK#exchanges"
    const val MASTERNET_EXPLORER_PREFIX = "master-net."
    const val TESTNET_EXPLORER_PREFIX = "testnet."
    var EXPLORER_PREFIX = ""
    var NODE_ADDRESS = ""
    var DB_PATH = ""
    var LOG_PATH = ""
    var TRANSACTIONS_PATH = ""
    var LOCALE: Locale = Locale.US
    val EXPLORER_LINK
        get() = "https://${EXPLORER_PREFIX}explorer.beam.mw/"

    fun buildTransactionLink(kernelId: String) = "${EXPLORER_LINK}block?kernel_id=$kernelId"
}
