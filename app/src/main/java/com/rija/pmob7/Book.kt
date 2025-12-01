package com.rija.pmob7

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val title: String?,
    val releaseDate: String?,
    val description: String?,
    val pages: Int?,
    val cover: String?
) : Parcelable