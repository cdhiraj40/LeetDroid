package com.example.leetdroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.model.LicensesModel

class LicenseListAdapter(val context: Context) :
    RecyclerView.Adapter<LicenseListAdapter.ViewHolder>() {

    private var licenseList = LicensesModel()
    private lateinit var licenses: List<LicensesModel.LibrariesNode.Library>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.license_item, parent, false)

        return ViewHolder(view)
    }

    fun setData(licensesModel: LicensesModel) {
        licenseList = licensesModel
        licenses = licensesModel.libraries?.library!!
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int,
            website: String?
        )
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val licenseItem = licenses[position]

        // sets the title of a question to the textView from our itemHolder class
        holder.licenseTitle.text =
            licenseItem.name

        holder.licenseSubTitle.text =
            "By ${licenseItem.author}, ${licenseItem.license}"

        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position,
                licenseItem.website
            )
        }

    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return licenses.size
    }

    fun getDataItemCount(): Int {
        return licenses.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val licenseTitle: TextView = itemView.findViewById(R.id.license_title)
        val licenseSubTitle: TextView = itemView.findViewById(R.id.license_sub_title)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}

