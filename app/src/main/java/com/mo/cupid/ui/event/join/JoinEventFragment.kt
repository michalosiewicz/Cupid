package com.mo.cupid.ui.event.join

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
import com.mo.cupid.databinding.FragmentJoinEventBinding
import com.mo.cupid.providers.MessageProvider

class JoinEventFragment : Fragment() {

    private lateinit var messageProvider: MessageProvider
    private lateinit var binding: FragmentJoinEventBinding
    private val viewModel = JoinEventViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_join_event,
            container,
            false
        )

        return binding.apply {
            viewModel = this@JoinEventFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!
        binding.userName.text = "Welcome " + arguments?.getString("userName")

        binding.createEvent.setOnClickListener {
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName")
            )
            findNavController().navigate(
                R.id.action_joinEventFragment_to_createEventFragment,
                bundle
            )
            viewModel.name.value = ""
            viewModel.password.value = ""
        }

        binding.logout.setOnClickListener {
            findNavController().navigate(R.id.action_joinEventFragment_to_logInFragment)
        }

        viewModel.joinSuccess.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName"),
                "eventName" to viewModel.name.value
            )
            findNavController().navigate(R.id.action_joinEventFragment_to_menuFragment, bundle)
        }

        viewModel.joinError.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }
}