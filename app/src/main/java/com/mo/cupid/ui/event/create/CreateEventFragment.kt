package com.mo.cupid.ui.event.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentCreateEventBinding
import com.mo.cupid.providers.MessageProvider

class CreateEventFragment : Fragment() {

    private lateinit var messageProvider: MessageProvider
    private lateinit var binding: FragmentCreateEventBinding
    private val viewModel = CreateEventViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_event,
            container,
            false
        )

        return binding.apply {
            viewModel = this@CreateEventFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!

        viewModel.createSuccess.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName")
            )
            findNavController().navigate(
                R.id.action_createEventFragment_to_joinEventFragment,
                bundle
            )
        }

        viewModel.createError.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
        }

        binding.joinEvent.setOnClickListener {
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName")
            )
            findNavController().navigate(
                R.id.action_createEventFragment_to_joinEventFragment,
                bundle
            )
        }
    }
}