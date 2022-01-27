package com.mo.cupid.ui.editor

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentEditorBinding
import com.mo.cupid.providers.MessageProvider

class EditorFragment : Fragment() {

    private lateinit var messageProvider: MessageProvider
    lateinit var binding: FragmentEditorBinding
    private lateinit var viewModel: EditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_editor,
            container,
            false
        )

        viewModel =
            EditorViewModel(
                arguments?.getString("userName") ?: "",
                arguments?.getString("eventName") ?: ""
            )

        return binding.apply {
            viewModel = this@EditorFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!

        binding.sendPhoto.setOnClickListener {
            context?.let { context -> viewModel.sendPhoto(context) }
        }

        binding.editPhoto.setOnClickListener {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        viewModel.pickImageIntent.observe(viewLifecycleOwner) { intent ->
            getResultForPickImage.launch(intent)
        }

        viewModel.saveImageIntent.observe(viewLifecycleOwner) { intent ->
            getResultForSaveImage.launch(intent)
        }

        viewModel.sendError.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
        }

        viewModel.sendSuccess.observe(viewLifecycleOwner) {
            messageProvider.toastMessage(it)
            val bundle = bundleOf(
                "userName" to arguments?.getString("userName"),
                "eventName" to arguments?.getString("eventName")
            )
            findNavController().navigate(R.id.action_editorFragment_to_menuFragment, bundle)
        }
    }

    private val getResultForPickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            val intent = Intent(context, DsPhotoEditorActivity::class.java)
            if (uri != null) {
                viewModel.startEditor(intent, uri)
            }
        }
    }

    private val getResultForSaveImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.imageView.setImageURI(result.data?.data)
            viewModel.uriImage.value = result.data?.data
            binding.sendPhoto.isEnabled = true
            messageProvider.toastMessage("Photo saved")
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.pickImage()
            } else {
                messageProvider.toastMessage("No permission")
            }
        }
}