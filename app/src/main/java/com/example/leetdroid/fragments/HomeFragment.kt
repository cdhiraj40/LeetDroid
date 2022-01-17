package com.example.leetdroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.leetdroid.adapter.AllQuestionsAdapter

import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentHomeBinding

import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.utils.JsonUtils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        val rootView = fragmentHomeBinding.root

        return rootView
    }

    object Constant {
        const val TAG = "HomeFragment"
    }
}