package com.mabrouk.domain.response

import com.google.gson.annotations.SerializedName
import com.mabrouk.domain.models.Verse


data class VersesResponse(
    val verses:ArrayList<Verse>,
    @SerializedName("pagination")
    val meta: Meta
)