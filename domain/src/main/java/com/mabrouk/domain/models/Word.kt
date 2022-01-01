package com.mabrouk.domain.models

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 28/08/2021 created by Just clean
 */
data class Word(
    val id:Long,
    val position:Int ,
    val text_indopak:String,
    val verse_key:String,
    val transliteration:Transliteration ,
    val audio: Audio,
    val translation:Translation
){

    data class Transliteration(
        val text:String
    )

    data class Audio(
        val url:String
    )

    data class Translation(
        val text: String,
        val language_name:String
    )
}