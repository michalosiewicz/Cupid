package com.mo.cupid.ui.camera

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.databinding.DataBindingUtil
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentCameraBinding
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.mo.cupid.managers.CameraManager
import com.mo.cupid.providers.MessageProvider

class CameraFragment : Fragment() {

    private lateinit var viewModel: CameraViewModel
    private lateinit var messageProvider: MessageProvider
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_camera,
            container,
            false
        )

        cameraManager = activity?.let { CameraManager(it, this) }!!
        viewModel = CameraViewModel(cameraManager)

        return binding.apply {
            viewModel = this@CameraFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageProvider = context?.let { MessageProvider(it) }!!

        requestPermission.launch(Manifest.permission.CAMERA)

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.startCamera()
            } else {
                binding.takePhoto.isEnabled = false
                binding.changeView.isEnabled = false
                messageProvider.toastMessage("No permission")
            }
        }

}