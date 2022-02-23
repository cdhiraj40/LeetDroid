package com.example.leetdroid.model

class ContributorListModel : ArrayList<ContributorListModel.ContributorListModelItem>() {

    val contributorListItem: ContributorListModelItem? = null

    class ContributorListModelItem {
        val author: AuthorNode? = null

        class AuthorNode {
            val id: Int? = null
            val login: String? = null
            val avatar_url: String? = null
            val html_url: String? = null
        }
    }
}