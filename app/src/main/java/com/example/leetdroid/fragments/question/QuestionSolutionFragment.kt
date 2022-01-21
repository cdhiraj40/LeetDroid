package com.example.leetdroid.fragments.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.lifecycle.ViewModelProvider
import com.example.leetdroid.databinding.FragmentQuestionSolutionBinding
import com.example.leetdroid.sharedViewModel.QuestionSharedViewModel
import android.webkit.WebViewClient


class QuestionSolutionFragment : Fragment() {

    private lateinit var fragmentSolutionBinding:FragmentQuestionSolutionBinding
    private lateinit var questionTitleSlug: String
    private var questionHasSolution: Boolean = false
    private lateinit var questionSharedViewModel:QuestionSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSolutionBinding = FragmentQuestionSolutionBinding.inflate(layoutInflater)
        val rootView = fragmentSolutionBinding.root


        val solutionView = fragmentSolutionBinding.solutionView
        questionSharedViewModel = ViewModelProvider(requireActivity()).get(QuestionSharedViewModel::class.java)

        questionSharedViewModel.questionTitleSlug.observe(viewLifecycleOwner, {
            // updating data in Title-Text
            questionTitleSlug = it

            solutionView.loadUrl("https://leetcode.com/problems/$questionTitleSlug/solution/")

            val solutionViewSettings = solutionView.settings
            solutionViewSettings.javaScriptEnabled = true
            solutionViewSettings.domStorageEnabled = true
            solutionViewSettings.databaseEnabled = true

            solutionViewSettings.cacheMode = WebSettings.LOAD_DEFAULT
            solutionView.webViewClient = WebViewClient()
            fragmentSolutionBinding.questionTitleText.text = questionTitleSlug
        })

        questionSharedViewModel.questionHasSolution.observe(viewLifecycleOwner, {
            // updating data in Title-Text
            questionHasSolution = it
        })

        if(!questionHasSolution){
            fragmentSolutionBinding.questionTitleText.text = "No Solution for this question"
        }


        return rootView
    }

}