package com.example.gt_6m_2_1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.gt_6m_2_1.databinding.ItemImageBinding
import com.example.gt_6m_2_1.model.Photo
import kotlin.reflect.KFunction1

class AllImagesAdapter(
    private var media: MutableList<Photo>,
    private val updateCounter: KFunction1<Int, Unit>
) : Adapter<AllImagesAdapter.AllImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllImagesViewHolder {
        return AllImagesViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun getSelectedImages() : ArrayList<Photo> {
        return media.filter { it.isSelected } as ArrayList<Photo>
    }

    override fun getItemCount() = media.size

    override fun onBindViewHolder(holder: AllImagesViewHolder, position: Int) {
        holder.bind(media[position])
    }

    inner class AllImagesViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.image.setImageURI(photo.name)

            itemView.setOnClickListener {
                if (photo.isSelected) {
                    photo.isSelected = false
                    binding.vCheckImages.isVisible = false
                    binding.vSelectImages.isVisible = false
                } else {
                    photo.isSelected = true
                    binding.vCheckImages.isVisible = true
                    binding.vSelectImages.isVisible = true
                }
                updateCounter(media.filter { it.isSelected }.size)
            }
        }

    }
}