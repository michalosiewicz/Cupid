package com.mo.cupid.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takePhotoButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cameraFragment)
        }

        binding.editButton.setOnClickListener {

        }

        binding.sendButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_galleryFragment)
        }

    }

}