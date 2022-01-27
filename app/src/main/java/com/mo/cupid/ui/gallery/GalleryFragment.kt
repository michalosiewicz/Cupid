package com.mo.cupid.ui.gallery

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentGalleryBinding
import com.mo.cupid.providers.MessageProvider

class GalleryFragment : Fragment() {

    private lateinit var messageProvider: MessageProvider
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_gallery,
            container,
            false
        )

        viewModel = GalleryViewModel(
            arguments?.getString("userName") ?: "",
            arguments?.getString("eventName") ?: ""
        )

        return binding.apply {
            viewModel = this@GalleryFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!

        viewModel.sendError.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
        }

        viewModel.sendSuccess.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName"),
                "eventName" to arguments?.getString("eventName")
            )
            findNavController().navigate(R.id.action_galleryFragment_to_menuFragment, bundle)
        }

        binding.selectPhotos.setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        viewModel.intent.observe(viewLifecycleOwner) { intent ->
            getPhotos.launch(intent)
        }

        binding.sendPhotos.setOnClickListener {
            context?.let { context -> viewModel.sendPhotos(context) }
        }
    }

    private val getPhotos = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getSelectedPhotos(result.data!!)
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.openGallery()
            } else {
                messageProvider.toastMessage("No permission")
            }
        }
}