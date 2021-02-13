package com.sababado.rolo.cookbook.utils

import androidx.core.content.FileProvider

/**
 * Use this to help export files such as a generated PDF to a Share Email intent.
 *
 * First reference this file provider in the manifest.
 * ```xml
 *
 * <application>
 *     <provider
 *         android:name=".utils.fileexports.GenericFileProvider"
 *         android:authorities="<USE_THE_PACKAGE_NAME_HERE>.provider"
 *         android:exported="false"
 *         android:grantUriPermissions="true">
 *         <meta-data
 *             android:name="android.support.FILE_PROVIDER_PATHS"
 *             android:resource="@xml/provider_paths"/>
 *         </provider>
 *     ...
 * </application>
 * ```
 *
 * Then use it in code.
 *
 * ```kotlin
 * return FileProvider.getUriForFile(
 *     context,
 *     "<USE_THE_PACKAGE_NAME_HERE>.provider",
 *     file
 * )
 * ```
 */
class GenericFileProvider : FileProvider()