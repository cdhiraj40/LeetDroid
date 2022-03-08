package com.cdhiraj40.leetdroid.ui.fragments.preferences

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cdhiraj40.leetdroid.BuildConfig
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.utils.extensions.copyToClipboard
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar


class AboutFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_about, rootKey)


        findPreference<Preference>("about_version")!!.summary = String.format(
            "%s", BuildConfig.VERSION_NAME
        )
        findPreference<Preference>("about_version")!!.setOnPreferenceClickListener { preference ->
            requireContext().copyToClipboard(
                findPreference<Preference>("about_version")!!.summary.toString()
            )
            showSnackBar(requireActivity(), "Copied")
            true
        }
        findPreference<Preference>("about_contributors")!!.setOnPreferenceClickListener { preference ->
            findNavController().navigate(R.id.contributorFragment)
            true
        }
        findPreference<Preference>("about_licenses")!!.setOnPreferenceClickListener { preference ->
            findNavController().navigate(R.id.licensesFragment)
            true
        }
    }
}