package com.mo.cupid.ui.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mo.cupid.R
import com.mo.cupid.databinding.FragmentGalleryBinding
import java.util.ArrayList

class GalleryFragment : Fragment() {

    private val pickImage = 1
    var position = 0
    var pickedImgUris = ArrayList<Uri>()

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel = GalleryViewModel()

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

        return binding.apply {
            viewModel = this@GalleryFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openGallery()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === pickImage && resultCode === Activity.RESULT_OK && data != null) {
            val imageView = binding.imageView

            if (data.clipData != null) {
                val count: Int = data.clipData!!.itemCount

                for (i in 0 until count) {
                    val imageURI: Uri = data.clipData!!.getItemAt(i).uri

                    pickedImgUris.add(imageURI)
                }

                imageView.setImageURI(pickedImgUris[0])
                position = 0
            } else {
                val imageUri: Uri? = data.data

                if (imageUri != null) {
                    pickedImgUris.add(imageUri)
                }

                imageView.setImageURI(pickedImgUris[0])
                position = 0
            }
        } else {
            Toast.makeText(context, "Nie wybrano zdjęcia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"

        startActivityForResult(intent, pickImage)
    }

    private fun selectAndSendFiles() {

    }
}