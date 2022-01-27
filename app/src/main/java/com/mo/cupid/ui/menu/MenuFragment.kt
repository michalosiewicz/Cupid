package com.mo.cupid.ui.menu

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
import com.mo.cupid.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu,
            container,
            false
        )
        bundle = bundleOf(
            "userName" to arguments?.getString("userName"),
            "eventName" to arguments?.getString("eventName")
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventName.text = arguments?.getString("eventName")

        binding.userName.text = arguments?.getString("userName")

        binding.takePhotoButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cameraFragment)
        }

        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_editorFragment, bundle)
        }

        binding.sendButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_galleryFragment, bundle)
        }

        binding.logout.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_logInFragment)
        }

        binding.changeEvent.setOnClickListener {
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName")
            )
            findNavController().navigate(R.id.action_menuFragment_to_joinEventFragment, bundle)
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })

    }
}