package com.cdhiraj40.leetdroid.ui.fragments.user

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.text.bold
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.data.entitiy.User
import com.cdhiraj40.leetdroid.data.viewModel.FirebaseUserViewModel
import com.cdhiraj40.leetdroid.data.viewModel.UserViewModel
import com.cdhiraj40.leetdroid.databinding.FragmentMyProfileBinding
import com.cdhiraj40.leetdroid.model.UserProfileErrorModel
import com.cdhiraj40.leetdroid.model.UserProfileModel
import com.cdhiraj40.leetdroid.model.UserProfileModel.DataNode.AllQuestionsCountNode
import com.cdhiraj40.leetdroid.model.UserProfileModel.DataNode.MatchedUserNode
import com.cdhiraj40.leetdroid.model.UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode
import com.cdhiraj40.leetdroid.model.UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode
import com.cdhiraj40.leetdroid.ui.base.BaseFragment
import com.cdhiraj40.leetdroid.utils.CommonFunctions.Logout.showLogOutDialog
import com.cdhiraj40.leetdroid.utils.CommonFunctions.Round.roundDouble
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.Converters.AllQuestionsCountConverters.fromAllQuestionsCountNode
import com.cdhiraj40.leetdroid.utils.Converters.AllQuestionsCountConverters.fromStringAllQuestionsCount
import com.cdhiraj40.leetdroid.utils.Converters.ContributionsNodeConverters.fromContributionsNode
import com.cdhiraj40.leetdroid.utils.Converters.ContributionsNodeConverters.fromStringContributions
import com.cdhiraj40.leetdroid.utils.Converters.MatchedUserNodeConverters.fromMatchedUserNode
import com.cdhiraj40.leetdroid.utils.Converters.MatchedUserNodeConverters.fromStringMatchedUser
import com.cdhiraj40.leetdroid.utils.Converters.ProfileNodeConverters.fromProfileNode
import com.cdhiraj40.leetdroid.utils.Converters.ProfileNodeConverters.fromStringProfileNode
import com.cdhiraj40.leetdroid.utils.Converters.SubmitStatsNodeConverters.fromStringSubmitStats
import com.cdhiraj40.leetdroid.utils.Converters.SubmitStatsNodeConverters.fromSubmitStatsNode
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.cdhiraj40.leetdroid.utils.SharedPreferences
import com.cdhiraj40.leetdroid.utils.dialog.AlertDialogShower
import com.cdhiraj40.leetdroid.utils.dialog.AppDialogs
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.math.roundToInt


//TODO do tell someone if they have a username more than 15 characters -> "we have your full username, we are just showing here only first 15 characters!"
class MyProfileFragment : BaseFragment() {

    private lateinit var myProfileBinding: FragmentMyProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var firebaseUserViewModel: FirebaseUserViewModel
    private lateinit var email: String
    private lateinit var username: String
    private lateinit var alertDialogShower: AlertDialogShower
    private lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        myProfileBinding = FragmentMyProfileBinding.inflate(layoutInflater)
        val rootView = myProfileBinding.root


        loadingView = rootView.findViewById(R.id.loading_view)

        // hide the views to show loading screen
        loadingView.visibility = View.VISIBLE
        myProfileBinding.userProfileLayout.visibility = View.GONE
        myProfileBinding.userProblemsDetailsLayout.visibility = View.GONE
        myProfileBinding.extraUserDetailsLayout.visibility = View.GONE


        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[UserViewModel::class.java]

        firebaseUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FirebaseUserViewModel::class.java]

        alertDialogShower = AlertDialogShower(requireActivity())

        getFirebaseProfile()
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun getFirebaseProfile() {
        val preferences = SharedPreferences(requireContext())
        if (!preferences.userDataLoaded) {
            // get username and email from room database
            firebaseUserViewModel.getFirebaseUser.observe(viewLifecycleOwner) {
                username = it.username
                email = it.email
                loadUser()
                preferences.userDataLoaded = true

            }
        } else {
            userViewModel.getUser.observe(viewLifecycleOwner) { it ->
                it?.let {
                    setupProfile(it)
                }
            }
        }
    }

    // setup profile from room database
    private fun setupProfile(user: User) {
        val allQuestionsCount = fromStringAllQuestionsCount(user.allQuestionsCount)
        val matchedUser = fromStringMatchedUser(user.matchedUser)
        val contributions = fromStringContributions(user.contributions)
        val profile = fromStringProfileNode(user.profile)
        val acSubmissionNum = fromStringSubmitStats(user.acSubmissionNum)
        val totalSubmissionNum = fromStringSubmitStats(user.totalSubmissionNum)

        setupUser(profile, matchedUser)

        setupUserSubmissions(allQuestionsCount, acSubmissionNum, totalSubmissionNum)

        setupClickListeners(matchedUser)

        loadingView.visibility = View.GONE
        myProfileBinding.userProfileLayout.visibility = View.VISIBLE
        myProfileBinding.userProblemsDetailsLayout.visibility = View.VISIBLE
        myProfileBinding.extraUserDetailsLayout.visibility = View.VISIBLE
    }

    private fun setupClickListeners(matchedUser: MatchedUserNode?) {
        myProfileBinding.recentSubmissionListLayout.setOnClickListener {
            val bundle = bundleOf(
                "username" to matchedUser?.username,
            )
            myProfileBinding.root.findNavController()
                .navigate(R.id.action_myProfileFragment_to_recentSubmissions, bundle)
        }

        myProfileBinding.userContestRankingsLayout.setOnClickListener {
            val bundle = bundleOf(
                "username" to matchedUser?.username,
            )
            myProfileBinding.root.findNavController()
                .navigate(R.id.action_myProfileFragment_to_contest_details, bundle)
        }
    }

    /**
     * all -> acSubmissionNum[0] / totalSubmissionNum[0] / allQuestionsCount[0]
     * easy-> acSubmissionNum[1] / totalSubmissionNum[1] / allQuestionsCount[1]
     * medium-> acSubmissionNum[2] / totalSubmissionNum[2] / allQuestionsCount[2]
     * hard-> acSubmissionNum[3] / totalSubmissionNum[3] / allQuestionsCount[3]
     */
    private fun setupUserSubmissions(
        allQuestionsCount: List<AllQuestionsCountNode>?,
        acSubmissionNum: List<AcSubmissionNumNode>?,
        totalSubmissionNum: List<AcSubmissionNumNode>?
    ) {
        showDifficultyStats(allQuestionsCount!!, acSubmissionNum, totalSubmissionNum)

        showSnackbars(acSubmissionNum, totalSubmissionNum)
    }

    private fun showDifficultyStats(
        allQuestionsCount: List<AllQuestionsCountNode>,
        acSubmissionNum: List<AcSubmissionNumNode>?,
        totalSubmissionNum: List<AcSubmissionNumNode>?
    ) {
        // easy difficulty
        myProfileBinding.easyDifficultyCount.text = SpannableStringBuilder()
            .bold { append(acSubmissionNum!![1].count.toString()) }
            .append("/ ${allQuestionsCount[1].count.toString()}")

        // medium difficulty
        myProfileBinding.mediumDifficultyCount.text = SpannableStringBuilder()
            .bold { append(acSubmissionNum!![2].count.toString()) }
            .append("/ ${allQuestionsCount[2].count.toString()}")

        // hard difficulty
        myProfileBinding.hardDifficultyCount.text = SpannableStringBuilder()
            .bold { append(acSubmissionNum!![3].count.toString()) }
            .append("/ ${allQuestionsCount[3].count.toString()}")

        // total Submission Num
        if (acSubmissionNum!![0].submissions!! > 0 && totalSubmissionNum!![0].submissions!! > 0) {
            var problemsAcceptanceRate =
                acSubmissionNum[0].submissions?.toDouble()
                    ?.div(totalSubmissionNum[0].submissions!!)!! * 100

            problemsAcceptanceRate = roundDouble(problemsAcceptanceRate, 1)
            myProfileBinding.questionProgressBar.progress = problemsAcceptanceRate.roundToInt()
            myProfileBinding.problemsAcceptanceRate.text =
                problemsAcceptanceRate.toString().plus("% \nAcceptance")
        } else {
            myProfileBinding.questionProgressBar.progress = 0
            myProfileBinding.problemsAcceptanceRate.text = "0% \nAcceptance"
        }

        myProfileBinding.userProblemsSolvedCount.text = acSubmissionNum[0].count.toString()
    }

    private fun showSnackbars(
        acSubmissionNum: List<AcSubmissionNumNode>?,
        totalSubmissionNum: List<AcSubmissionNumNode>?
    ) {
        // easy difficulty
        myProfileBinding.easyDifficultyCount.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![1].submissions!!.toDouble(),
                totalSubmissionNum!![1].submissions!!
            )
        }

        myProfileBinding.easyDifficultyText.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![1].submissions!!.toDouble(),
                totalSubmissionNum!![1].submissions!!
            )
        }

        // medium difficulty
        myProfileBinding.mediumDifficultyCount.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![2].submissions!!.toDouble(),
                totalSubmissionNum!![2].submissions!!
            )
        }

        myProfileBinding.mediumDifficultyText.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![2].submissions!!.toDouble(),
                totalSubmissionNum!![2].submissions!!
            )
        }

        // hard difficulty
        myProfileBinding.hardDifficultyCount.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![3].submissions!!.toDouble(),
                totalSubmissionNum!![3].submissions!!
            )
        }

        myProfileBinding.hardDifficultyText.setOnClickListener {
            showAcceptanceRate(
                acSubmissionNum!![3].submissions!!.toDouble(),
                totalSubmissionNum!![3].submissions!!
            )
        }

    }

    private fun setupUser(
        profile: ProfileNode?,
        matchedUser: MatchedUserNode?,
    ) {
        myProfileBinding.userFullName.text =
            profile?.realName

        myProfileBinding.username.text = matchedUser?.username
        myProfileBinding.userBio.text = profile?.aboutMe
        myProfileBinding.userWebsite.text =
            if (profile?.websites == null) profile?.websites!![0] else "Website not added"

        myProfileBinding.userRating.rating = profile.starRating!!.toFloat()
        myProfileBinding.userEducation.text =
            if (profile.school == null) profile.school else "Education not added"

        myProfileBinding.userLocation.text =
            if (profile.countryName == null) profile.countryName else "Location not added"

        Glide.with(requireContext())
            .load(profile.userAvatar)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .into(myProfileBinding.userProfileAvatar)

        // ranking
        myProfileBinding.userRanking.text = "~".plus(matchedUser?.profile?.ranking.toString())
    }

    // load user from online
    private fun loadUser() {
        val call: Call = createApiCall()
        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(Constant.TAG(MyProfileFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body!!.string()
                val userData: UserProfileModel = JsonUtils.generateObjectFromJson(
                    body,
                    UserProfileModel::class.java
                )
                if (userData.data?.matchedUser == null) {
                    val errorData: UserProfileErrorModel = JsonUtils.generateObjectFromJson(
                        body,
                        UserProfileErrorModel::class.java
                    )
                    if (errorData.errors?.get(0)?.message.toString() == "That user does not exist.") {
                        showSnackBar(
                            requireActivity(),
                            "Something went wrong, please try again later"
                        )
                        return
                    } else {
                        showSnackBar(
                            requireActivity(),
                            "Something went wrong, please try again later"
                        )
                        return
                    }
                }
                val user = User(
                    fromAllQuestionsCountNode(userData.data?.allQuestionsCount).toString(),
                    fromMatchedUserNode(userData.data?.matchedUser).toString(),
                    fromContributionsNode(userData.data?.matchedUser?.contributions!!).toString(),
                    fromProfileNode(userData.data?.matchedUser?.profile!!).toString(),
                    fromSubmitStatsNode(userData.data?.matchedUser?.submitStats?.acSubmissionNum!!).toString(),
                    fromSubmitStatsNode(userData.data?.matchedUser?.submitStats?.totalSubmissionNum!!).toString()
                )
                addUpdateUser(user)
            }
        })
    }

    // creates an okHttpClient call for user data
    private fun createApiCall(): Call {
        val okHttpClient = OkHttpClient()
        val postBody =
            Gson().toJson(LeetCodeRequests.Helper.getUserProfileRequest(this.username))
        val requestBody: RequestBody =
            postBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .build()
        val request: Request = Request.Builder()
            .headers(headers)
            .post(requestBody)
            .url(URL.graphql)
            .build()
        return okHttpClient.newCall(request)
    }

    // add or update data
    private fun addUpdateUser(user: User) {
        val preferences = SharedPreferences(requireContext())
        lifecycleScope.launch {
            if (!preferences.userAdded) {
                preferences.userAdded = true
                userViewModel.addUser(user)
            } else {
                user.id = 1
                userViewModel.updateUser(user)
            }
            userViewModel.getUser.observe(viewLifecycleOwner) { it ->
                it?.let {
                    setupProfile(it)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // clear the Main Activity's menu
        menu.clear()
        inflater.inflate(R.menu.my_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                showLogOutDialog(requireActivity(), requireContext())
                true
            }
            R.id.sync -> {
                showSyncDataDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAcceptanceRate(correctSubmissions: Double, allSubmissions: Int) {
        showSnackBar(
            requireActivity(),
            "Acceptance Rate is ${
                roundDouble(
                    correctSubmissions.div(allSubmissions)
                        .times(100), 2
                )
            } %"
        )
    }

    private fun showSyncDataDialog() {
        alertDialogShower.show(AppDialogs.SyncData, {
            // get username and email from room database
            firebaseUserViewModel.getFirebaseUser.observe(viewLifecycleOwner) { it ->
                it?.let {
                    username = it.username
                    email = it.email
                }
                loadUser()
            }
        }, {})
    }
}