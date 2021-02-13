package com.sababado.rolo.cookbook.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log

object MetaDataUtils {
    @JvmStatic
    fun getMetaDataFlag(context: Context, tag: String): String? {
        context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            .apply {
                val v = metaData.getString(tag)
                if (v == null) {
                    Log.i("MetaDataUtils", "Unable to locate meta data: $tag")
                }
                return v
            }
    }

    @JvmStatic
    fun requireMetaDataFlag(context: Context, tag: String): String {
        return getMetaDataFlag(context, tag)
            ?: throw Resources.NotFoundException("Unable to locate meta data: $tag")
    }
}