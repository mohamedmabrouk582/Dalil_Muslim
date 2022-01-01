package com.mabrouk.data.db.quran

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mabrouk.data.entities.*

@Database(
    entities = [
        SuraEntity::class,
        JuzEntity::class,
        VerseEntity::class,
        TafsirAyaEntity::class,
        QuranReaderEntity::class,
        RadioEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbConverter::class)
abstract class QuranDB : RoomDatabase() {
    abstract fun getQuranDao(): QuranDao
}