package com.mabrouk.data.utils

import android.util.Log
import com.mabrouk.data.entities.*
import com.mabrouk.domain.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import com.mabrouk.domain.models.Sura as SuraDomain
import com.mabrouk.domain.models.TafsirAya as TafsirDomain
import com.mabrouk.domain.models.Verse as VerseDomain
import com.mabrouk.domain.models.Juz as JuzDomain

fun Radio.toEntity() : RadioEntity =
    RadioEntity(name,radio_url)

@JvmName("toEntityRadio")
fun ArrayList<Radio>.toEntity() : ArrayList<RadioEntity> =
    ArrayList(map { it.toEntity() })

fun AudioEntity.toDomain() : Audio =
    Audio(url, duration, format)
fun TranslationEntity.toDomain() : Translation =
    Translation(id, language_name, textTranslation, textIndopak, verseKey, audioUrl, textTransliteration)

fun Audio.toEntity() : AudioEntity =
    AudioEntity(url, duration, format)
fun Translation.toEntity() : TranslationEntity =
    TranslationEntity(id, language_name, textTranslation, textIndopak, verseKey, audioUrl, textTransliteration)

@JvmName("toEntityTranslation")
fun ArrayList<Translation>.toEntity() : ArrayList<TranslationEntity> =
    ArrayList(map { it.toEntity() })

@JvmName("toDomainTranslationEntity")
fun ArrayList<TranslationEntity>.toDomain() : ArrayList<Translation> =
    ArrayList(map { it.toDomain() })

fun Media.toEntity() : MediaEntity=
        MediaEntity(url, provider, author_name)
fun MediaEntity.toDomain() : Media=
        Media(url, provider, author_name)

@JvmName("toEntityMedia")
fun ArrayList<Media>.toEntity() : ArrayList<MediaEntity> =
        ArrayList(map { it.toEntity() })

@JvmName("toDomainMediaEntity")
fun ArrayList<MediaEntity>.toDomain() : ArrayList<Media> =
        ArrayList(map { it.toDomain() })

fun SuraEntity.toDomain() : SuraDomain =
    SuraDomain(sura_id,bismillah_pre,revelation_place, name_complex, name_arabic, name_simple, verses_count,isDownload = isDownload)

fun VerseEntity.toDomain() : VerseDomain =
    VerseDomain(aya_id,verse_number,chapter_id, text_madani, text_indopak, text_simple, juz_number, hizb_number, rub_number, sajdah, sajdah_number, page_number, audio?.toDomain(),media_contents?.toDomain())

fun TafsirAyaEntity.toDomain() : TafsirDomain =
    TafsirDomain(tafsir_id,verse_id, t_text, t_language_name, t_resource_name, verse_key)

fun JuzEntity.toDomain() : JuzDomain =
    JuzDomain(id, juz_number, verse_mapping,isDownload)

@JvmName("toDomainSuraEntity")
fun ArrayList<SuraEntity>.toDomain() : ArrayList<SuraDomain> = ArrayList(map { it.toDomain() })

fun ArrayList<VerseEntity>.toDomain() : ArrayList<VerseDomain> = ArrayList(map {it.toDomain()})

@JvmName("toDomainTafsirAyaEntity")
fun ArrayList<TafsirAyaEntity>.toDomain() : ArrayList<TafsirDomain> = ArrayList(map {it.toDomain()})

@JvmName("toDomainJuzEntity")
fun ArrayList<JuzEntity>.toDomain() : ArrayList<JuzDomain> = ArrayList(map {it.toDomain()})

// to entity
fun SuraDomain.toEntity() : SuraEntity =
    SuraEntity(id,bismillah_pre,revelation_place, name_complex, name_arabic, name_simple, verses_count,from_to,isDownload)

fun VerseDomain.toEntity() : VerseEntity =
    VerseEntity(id,verse_number, chapter_id, text_madani, text_indopak, text_simple, juz_number, hizb_number, rub_number, sajdah, sajdah_number, page_number, audio?.toEntity(),ArrayList(words?.let {
        it.removeLast()
        it
    }?.map {
        TranslationEntity(
            it.id,
            it.translation.language_name,
            it.translation.text,
            it.text_indopak,
            it.verse_key,
            it.audio.url,
            it.transliteration.text
        )
    }!!),media_contents?.toEntity())

fun TafsirDomain.toEntity() : TafsirAyaEntity =
    TafsirAyaEntity(id,verse_id, text?:"", language_name, resource_name, verse_key, chapter_id)

fun JuzDomain.toEntity() : JuzEntity =
    JuzEntity(id, juz_number, verse_mapping,isDownload)

fun ArrayList<SuraDomain>.toEntity() : ArrayList<SuraEntity> = ArrayList(map { it.toEntity() })

@JvmName("toEntityVerseDomain")
fun ArrayList<VerseDomain>.toEntity() : ArrayList<VerseEntity> = ArrayList(map {it.toEntity()})


@JvmName("toEntityTafsirDomain")
fun ArrayList<TafsirDomain>.toEntity() : ArrayList<TafsirAyaEntity> = ArrayList(map {it.toEntity()})


@JvmName("toEntityJuzDomain")
fun ArrayList<JuzDomain>.toEntity() : ArrayList<JuzEntity> = ArrayList(map {it.toEntity()})


fun mapJuz(juzs: ArrayList<JuzEntity>,suras:ArrayList<SuraEntity>) : ArrayList<Juz_Sura>{
    val data:ArrayList<Juz_Sura> = ArrayList()
    juzs.forEach { juz ->
        val ids = juz.verse_mapping?.keys?.map {
            it.toInt()
        }!!
        data.add(Juz_Sura(juz.juz_number,ids,juz.verse_mapping,isDownload = juz.isDownload))
        data.addAll(juz.verse_mapping.keys.map { key ->
            suras[key.toInt() - 1].apply {
                from_to = juz.verse_mapping[key]
            }
        }.map {
            Juz_Sura(juz.juz_number,ids,juz.verse_mapping,it,it.from_to,juz.isDownload)
        })
    }

    data.forEach {
        Log.d("susususu","${it}")
    }

    return data
}


//hadith
fun HadithCategory.toEntity() : HadithCategoryEntity =
    HadithCategoryEntity(name, hasBooks, hasChapters, totalHadith, totalAvailableHadith, collection)

@JvmName("toEntityHadithCategory")
fun ArrayList<HadithCategory>.toEntity() : ArrayList<HadithCategoryEntity> =
    ArrayList(map {
        it.toEntity()
    })

fun HadithBookNumber.toEntity() : HadithBookNumberEntity=
    HadithBookNumberEntity(collectionName?:"",bookNumber, hadithStartNumber, hadithEndNumber, numberOfHadith, book)


@JvmName("toEntityHadithBookNumber")
fun ArrayList<HadithBookNumber>.toEntity() : ArrayList<HadithBookNumberEntity> =
    ArrayList(map { it.toEntity() })

fun HadithModel.toEntity() : HadithEntity=
    HadithEntity(collection, bookNumber, chapterId, hadithNumber, hadith)

@JvmName("toEntityHadithModel")
fun ArrayList<HadithModel>.toEntity() : ArrayList<HadithEntity> =
    ArrayList(map{it.toEntity()})


fun <t>fromJsonToObject(json:String?) : t?{
    val type = object : TypeToken<t>(){}.type
    return Gson().fromJson(json,type)
}

fun Int.decimalFormat() :String {
    val pattern = "000"
    val number = NumberFormat.getNumberInstance(Locale.US)
    val myFormatter = number as DecimalFormat
    myFormatter.applyPattern(pattern)
    return myFormatter.format(this)
}



