package com.example.leetdroid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.leetdroid.databinding.FragmentHomeBinding

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