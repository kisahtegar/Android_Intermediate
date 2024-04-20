package com.kisahcode.androidintermediate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Parcelable data class representing a hero entity.
 *
 * @property name The name of the hero.
 * @property description The description of the hero.
 * @property photo The URL or resource identifier of the hero's photo.
 */
@Parcelize
data class Hero(
    var name: String,
    var description: String,
    var photo: String
) : Parcelable