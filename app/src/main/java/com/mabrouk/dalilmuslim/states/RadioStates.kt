package com.mabrouk.dalilmuslim.states

import com.mabrouk.data.entities.RadioEntity

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
sealed class RadioStates{
    object Idle : RadioStates()
    data class LoadData(val data:ArrayList<RadioEntity>) : RadioStates()

}
