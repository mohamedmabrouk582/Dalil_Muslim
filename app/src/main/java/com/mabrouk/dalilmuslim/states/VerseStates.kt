package com.mabrouk.dalilmuslim.states

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import com.mabrouk.data.entities.QuranReaderEntity

sealed class VerseStates {
    object Idle : VerseStates()
    data class DownloadVerse(val workInfo: LiveData<WorkInfo>) : VerseStates()
    data class UpdateReader(val readerEntity: QuranReaderEntity) : VerseStates()
}