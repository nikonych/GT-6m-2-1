package com.example.gt_6m_2_1.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val name: Uri? = null,
    var isSelected: Boolean = false
): Parcelable

