package com.mo.cupid.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentLogInBinding
import com.mo.cupid.providers.MessageProvider

class LogInFragment : Fragment() {

    private lateinit var messageProvider: MessageProvider
    private val viewModel = LogInViewModel()
    private lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_log_in,
            container,
            false
        )

        return binding.apply {
            viewModel = this@LogInFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!

        viewModel.logInSuccess.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
            val bundle = bundleOf(
                "userName" to viewModel.userName.value
            )
            findNavController().navigate(R.id.action_logInFragment_to_joinEventFragment, bundle)
        }

        viewModel.logInError.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
            viewModel.userName.value = ""
            viewModel.password.value = ""
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }
}