package com.scootin.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentSplashBinding
import com.scootin.util.fragment.autoCleared
import com.scootin.view.activity.MainActivity
import com.scootin.viewmodel.home.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import androidx.lifecycle.observe
import com.scootin.view.activity.SplashActivity

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var binding by autoCleared<FragmentSplashBinding>()
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            startNextActivity()
        }, 3000)


    }

    //If user already login then no need to go again just refresh the token and continue to next screen..
    private fun gotoLoginFragment() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashToLogin(),
            NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .setPopUpTo(R.id.splash_fragment, true).build()
        )
    }

    private fun startNextActivity() {
        Timber.i("Starting next activity..")
        viewModel.firstLaunch().observe(viewLifecycleOwner) {

            val isRunning = activity as SplashActivity?

            if (isRunning?.isRunning() == true) {
                Timber.i("I am running")
                return@observe
            }

            if(it) {
                gotoLoginFragment()
            } else {
                gotoHomePage()
            }
        }
    }

    private fun gotoHomePage() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
        activity?.finish()
    }

}