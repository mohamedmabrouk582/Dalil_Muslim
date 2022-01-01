package com.mabrouk.domain.response

data class Meta(
    val current_page:Int,
    val next_page:Int?,
    val prev_page:Int?,
    val total_pages:Int?,
    val total_count:Int?
)