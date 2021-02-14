package com.sababado.rolo.cookbook.ads

import android.content.Context
import android.os.Bundle
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.sababado.rolo.cookbook.BuildConfig
import java.net.MalformedURLException
import java.net.URL

class AdHelper {
    companion object {
        fun setupAds(
            context: Context,
            adView: AdView,
            adMobPubId: String,
            privacy_url: String,
            debugDeviceId: String = "",
            adHelperListener: AdHelperListener? = null
        ) {
            MobileAds.initialize(context)
            if (BuildConfig.DEBUG) {
                ConsentInformation.getInstance(context)
                    .addTestDevice(debugDeviceId)
                ConsentInformation.getInstance(context).debugGeography =
                    DebugGeography.DEBUG_GEOGRAPHY_EEA;
            }
            // check for EU consent
            // https://developers.google.com/admob/android/eu-consent
            val consentInformation: ConsentInformation = ConsentInformation.getInstance(context)
            val publisherIds =
                arrayOf(adMobPubId)
            consentInformation.requestConsentInfoUpdate(
                publisherIds,
                object : ConsentInfoUpdateListener {
                    override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {
                        // User's consent status successfully updated.
                        var canViewAds =
                            !ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown
                        if (!canViewAds) {
                            canViewAds = when (consentStatus) {
                                ConsentStatus.PERSONALIZED -> true
                                ConsentStatus.NON_PERSONALIZED -> true
                                else -> {
                                    askForAdConsent(context, adView, privacy_url, adHelperListener)
                                    false
                                }
                            }
                        }

                        if (canViewAds) {
                            startAds(
                                context,
                                adView,
                                consentStatus != ConsentStatus.PERSONALIZED,
                                adHelperListener
                            )
                        }
                    }

                    override fun onFailedToUpdateConsentInfo(errorDescription: String?) {
                        // User's consent status failed to update.
                    }
                })
        }

        private fun askForAdConsent(
            context: Context,
            adView: AdView,
            privacy_url: String,
            adHelperListener: AdHelperListener?
        ) {
            var privacyUrl: URL? = null
            try {
                privacyUrl = URL(privacy_url)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                // Handle error.
            }
            var form: ConsentForm? = null

            form = ConsentForm.Builder(context, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        form?.show()
                        // Consent form loaded successfully.
                        adHelperListener?.onConsentFormLoaded(context)
                    }

                    override fun onConsentFormOpened() {
                        // Consent form was displayed.
                        adHelperListener?.onConsentFormOpened(context)
                    }

                    override fun onConsentFormClosed(
                        consentStatus: ConsentStatus, userPrefersAdFree: Boolean
                    ) {
                        adHelperListener?.onConsentFormClosed(context, userPrefersAdFree)

                        // ignoring userPrefersAdFree because there is no ad-free version at this time.
                        startAds(
                            context,
                            adView,
                            consentStatus != ConsentStatus.PERSONALIZED,
                            adHelperListener
                        )
                    }

                    override fun onConsentFormError(errorDescription: String) {
                        // Consent form error.
                        adHelperListener?.onConsentFormError(context, errorDescription)
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build()
            form.load()
        }

        private fun startAds(
            context: Context,
            adView: AdView,
            userDoesNotWantPersonalizedAds: Boolean,
            adHelperListener: AdHelperListener?
        ) {
            val adRequestBuilder = AdRequest.Builder()
            if (userDoesNotWantPersonalizedAds) {
                val extras = Bundle()
                extras.putString("npa", "1")
                adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            }

            val adRequest = adRequestBuilder.build()
            adView.adListener = object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    val ec: String = when (errorCode) {
                        AdRequest.ERROR_CODE_INVALID_REQUEST -> "invalid_request"
                        AdRequest.ERROR_CODE_NETWORK_ERROR -> "network_error"
                        AdRequest.ERROR_CODE_NO_FILL -> "no_fill"
                        else -> "internal_error"
                    }
                    adHelperListener?.onAdFailedToLoad(context, errorCode, ec)
                }

                override fun onAdLoaded() {
                    adHelperListener?.onAdLoaded(context)
                }
            }
            adView.loadAd(adRequest)
        }
    }


    abstract class AdHelperListener {

        open fun onConsentFormOpened(context: Context) {}

        open fun onConsentFormLoaded(context: Context) {}

        open fun onConsentFormError(context: Context, reason: String) {}

        open fun onConsentFormClosed(context: Context, userPrefersAdFree: Boolean) {}

        open fun onAdImpression(context: Context) {}

        open fun onAdLeftApplication(context: Context) {}

        open fun onAdClicked(context: Context) {}

        open fun onAdFailedToLoad(context: Context, errorCode: Int, errorCodeString: String) {}

        open fun onAdFailedToLoad(context: Context, p0: LoadAdError?) {}

        open fun onAdClosed(context: Context) {}

        open fun onAdOpened(context: Context) {}

        open fun onAdLoaded(context: Context) {}
    }
}