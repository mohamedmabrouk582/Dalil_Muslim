package com.mabrouk.data.entities

import androidx.room.Entity
import com.mabrouk.domain.response.PrayerDate
import com.mabrouk.domain.response.PrayerMeta
import com.mabrouk.domain.response.Timing

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 16/01/2022 created by Just clean
 */
//http://api.aladhan.com/v1/timingsByCity?city=Dubai&country=United%20Arab%20Emirates&method=8
@Entity
data class PrayerEntity(
    val timings: Timing,
    val date : PrayerDate,
    val meta: PrayerMeta
)