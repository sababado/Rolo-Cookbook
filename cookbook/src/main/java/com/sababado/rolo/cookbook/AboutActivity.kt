package com.sababado.rolo.cookbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

private const val arg_version_code = "version code"
private const val arg_version_name = "version name"

class AboutActivity : AppCompatActivity() {

    companion object {
        fun goToAboutActivity(context: Context, versionCode: Int, versionName: String) {
            val intent = Intent(context, AboutActivity::class.java)
            intent.putExtra(arg_version_code, versionCode)
            intent.putExtra(arg_version_name, versionName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.about, rootKey)
            val versionCode = requireActivity().intent?.getIntExtra(arg_version_code, 0)
            val versionName = requireActivity().intent?.getStringExtra(arg_version_name)
            findPreference<Preference>("p_app_version")?.summary = "$versionName ($versionCode)"
        }
    }
}