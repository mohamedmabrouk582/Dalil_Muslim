package com.mabrouk.domain.response

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 16/01/2022 created by Just clean
 */

data class PrayerResponse(
    val code : Int,
    val data : PrayerModel
)

data class PrayerModel(
   val timings:Timing,
   val date : PrayerDate,
   val meta: PrayerMeta
)

data class Timing(
    val Fajr: String,
    val Sunrise:String,
    val Dhuhr:String,
    val Asr:String,
    val Sunset:String,
    val Maghrib:String,
    val Isha:String,
    val Imsak:String,
    val Midnight:String
)

data class PrayerDate(
    val readable:String,
    val timestamp:Long,
    val hijri : Hijri,
    val gregorian : Hijri
)
data class Hijri(
    val date:String,
    val format:String,
    val day: Int,
    val weekday : HashMap<String,String>,
    val month :Month,
    val year:Int,
    val holidays:ArrayList<String>
)

data class Month(
    val number:Int,
    val en:String,
    val ar:String
)

data class PrayerMeta(
    val latitude : Double,
    val longitude:Double,
    val timezone:String,
    val method : Method
)
data class Method(
   val id : Int,
   val name :String
)