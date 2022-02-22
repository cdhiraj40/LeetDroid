package com.example.leetdroid.ui.fragments.preferences

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.leetdroid.BuildConfig
import com.example.leetdroid.R
import com.example.leetdroid.utils.extensions.copyToClipboard
import com.example.leetdroid.utils.extensions.showSnackBar


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
            showSnackBar(requireActivity(), "Contributors")
            true
        }
        findPreference<Preference>("about_licenses")!!.setOnPreferenceClickListener { preference ->
            findNavController().navigate(R.id.licensesFragment)
            true
        }
    }
}