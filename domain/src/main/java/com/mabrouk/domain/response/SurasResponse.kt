package com.mabrouk.domain.response

import com.mabrouk.domain.models.Sura


data class SurasResponse(
    val chapters:ArrayList<Sura>
)