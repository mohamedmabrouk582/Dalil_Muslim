package com.mabrouk.domain.models


data class Verse(
    val id:Int,
    val verse_number:Int,
    val chapter_id:Int,
    val text_madani:String?=null,
    val text_indopak:String?=null,
    val text_simple:String?=null,
    val juz_number:Int?=null,
    val hizb_number:Int?=null,
    val rub_number:Int?=null,
    val sajdah:String?=null,
    val sajdah_number:Int?=null,
    val page_number:Int?=null,
    val audio: Audio?=null,
    val media_contents:ArrayList<Media>?=null,
    val words : ArrayList<Word>?=null
)