package com.mabrouk.data.entities

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 31/07/2021 created by Just clean
 */
data class StoryUrlEntity(
    val format: StoryFormat,
    val url: String
) {
    data class StoryFormat(
        val itg: Int,
        val ext: String,
        val height: Int,
        val isDashContainer: Boolean
    )
}