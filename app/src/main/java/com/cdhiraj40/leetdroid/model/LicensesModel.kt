package com.cdhiraj40.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list' error
 *  if no data comes we check for error here
 */
class LicensesModel : Serializable {

    /**
     *
     * { "libraries": { "library": [ { "name": "LeetDroid", "author": "Dhiraj Chauhan", "website": "https://github.com/cdhiraj40/LeetDroid/", "license": "MIT License" },
     * { "name": "Android Jetpack", "author": "Google", "website": "https://developer.android.com/jetpack", "license": "Apache 2.0 License" },
     */
    var libraries: LibrariesNode? = null

    class LibrariesNode {
        var library: List<Library>? = null

        class Library {
            var name: String? = null
            var author: String? = null
            var website: String? = null
            var license: String? = null
        }
    }
}