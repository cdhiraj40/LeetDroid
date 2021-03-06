package com.cdhiraj40.leetdroid.ui.fragments.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdhiraj40.leetdroid.adapter.LicenseListAdapter
import com.cdhiraj40.leetdroid.databinding.FragmentLicenseBinding
import com.cdhiraj40.leetdroid.model.LicensesModel
import com.cdhiraj40.leetdroid.utils.CommonUtils.openLink
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.cdhiraj40.leetdroid.utils.JsonUtils.readAssetsFile

class LicenseFragment : Fragment(), LicenseListAdapter.OnItemClicked {

    private lateinit var licenseBinding: FragmentLicenseBinding
    private lateinit var licenseListAdapter: LicenseListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        licenseBinding = FragmentLicenseBinding.inflate(inflater)

        val rootView = licenseBinding.root
        licenseListAdapter = LicenseListAdapter(requireContext())
        licenseListAdapter.setOnClick(this)
        setUpLicenses()
        return rootView
    }

    private fun setUpLicenses() {
        val licenseString = requireContext().assets.readAssetsFile("licenses.json")

        val licenseJson: LicensesModel = JsonUtils.generateObjectFromJson(
            licenseString,
            LicensesModel::class.java
        )

        licenseListAdapter.setData(licenseJson)
        licenseBinding.licenseRecyclerView.layoutManager =
            LinearLayoutManager(context)
        licenseBinding.licenseRecyclerView.adapter =
            licenseListAdapter

    }

    override fun onItemClick(position: Int, website: String?) {
        openLink(requireContext(), website.toString())
    }

}