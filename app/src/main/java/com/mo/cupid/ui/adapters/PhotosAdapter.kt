package com.mo.cupid.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mo.cupid.databinding.ItemPhotoBinding

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val listPhotos = mutableListOf<Uri>()

    @SuppressLint("NotifyDataSetChanged")
    fun addNewItems(photos: List<Uri>) {
        listPhotos.clear()
        listPhotos.addAll(photos)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) {
            binding.imageView.setImageURI(uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPhotos[position])
    }

    override fun getItemCount(): Int = listPhotos.size
}