package com.mo.cupid.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentCameraBinding
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.navigation.fragment.findNavController
import com.mo.cupid.managers.CameraManager

class CameraFragment : Fragment() {

    private val viewModel = CameraViewModel()
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var cameraSelector: CameraSelector

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

        return binding.apply {
            viewModel = this@CameraFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraManager = activity?.let { CameraManager(it, this) }!!

        if (allPermissionsGranted()) {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraManager.startCamera(cameraSelector)
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

        binding.takePhoto.setOnClickListener {
            cameraManager.takePhoto()
        }

        binding.changeView.setOnClickListener {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                cameraManager.startCamera(cameraSelector)
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraManager.startCamera(cameraSelector)
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera(CameraSelector.DEFAULT_BACK_CAMERA)
            } else {
                Toast.makeText(
                    activity,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        activity?.let { activity ->
            ContextCompat.checkSelfPermission(
                activity.baseContext, it
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}