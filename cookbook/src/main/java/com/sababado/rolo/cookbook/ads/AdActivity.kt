package com.sababado.rolo.cookbook.ads

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.sababado.rolo.cookbook.R
import com.sababado.rolo.cookbook.utils.MetaDataUtils


abstract class AdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ad_banner_activity)
        val content = findViewById<ViewGroup>(R.id.content)
        layoutInflater.inflate(getContentViewId(), content, true)

        val adViewId = getAdViewId()
        if (adViewId != 0) {
            val adbmobPubId = MetaDataUtils.requireMetaDataFlag(this, "rolo.cookbook.admob_pub_id")
            val privacyUrl = MetaDataUtils.requireMetaDataFlag(this, "rolo.cookbook.privacy_url")
            val debugDeviceId =
                MetaDataUtils.requireMetaDataFlag(this, "rolo.cookbook.debug_device_id")

            val adView = findViewById<AdView>(adViewId)
            AdHelper.setupAds(
                this,
                adView,
                adbmobPubId,
                privacyUrl,
                debugDeviceId
            )
        }
    }

    @IdRes
    open fun getAdViewId(): Int {
        return R.id.adView
    }

    @LayoutRes
    /**
     * Get the content view id that should show in the activity.
     */
    abstract fun getContentViewId(): Int
}