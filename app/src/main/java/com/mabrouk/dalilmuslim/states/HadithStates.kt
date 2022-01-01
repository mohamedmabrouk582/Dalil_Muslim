package com.mabrouk.dalilmuslim.states

import com.mabrouk.data.entities.HadithBookNumberEntity
import com.mabrouk.data.entities.HadithCategoryEntity
import com.mabrouk.data.entities.HadithEntity
import com.mabrouk.domain.models.HadithCategory

sealed class HadithStates{
    object Idle : HadithStates()
    data class LoadCategories(val data:ArrayList<HadithCategoryEntity>) :HadithStates()
    data class LoadHadithBooks(val data:ArrayList<HadithBookNumberEntity>) : HadithStates()
    data class LoadHadith(val data : ArrayList<HadithEntity>) : HadithStates()
}
