package com.mabrouk.dalilmuslim.states

import com.mabrouk.data.entities.JuzEntity
import com.mabrouk.data.entities.SuraEntity

sealed class SurahStates{
    object Idle : SurahStates()
    data class RequestJuzs(val juzs:ArrayList<JuzEntity>) : SurahStates()
    data class RequestSurahs(val suras:ArrayList<SuraEntity>) : SurahStates()
    data class Error(val error:String) : SurahStates()
}
