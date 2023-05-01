package com.example.gt_6m_2_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.gt_6m_2_1.databinding.ItemImageBinding
import com.example.gt_6m_2_1.model.Photo

class SelectedImagesAdapter(private var media: MutableList<Photo>): Adapter<SelectedImagesAdapter.SelectedImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImagesViewHolder {
        return SelectedImagesViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = media.size

    override fun onBindViewHolder(holder: SelectedImagesViewHolder, position: Int) {
        holder.bind(media[position])
    }

    inner class SelectedImagesViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.image.setImageURI(photo.name)
        }

    }
}