package com.mabrouk.dalilmuslim.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.mabrouk.dalilmuslim.R
import com.mabrouk.dalilmuslim.databinding.DownloadProgressBinding
import com.mabrouk.dalilmuslim.utils.EventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class DownloadProgressDialog : DialogFragment()  {
    @Inject
    lateinit var eventBus: EventBus
    lateinit var layoutBinding:DownloadProgressBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       layoutBinding=DataBindingUtil.inflate(inflater, R.layout.download_progress,container,false)
        this.dialog?.setCanceledOnTouchOutside(false)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            eventBus.receiveType().collect {
                layoutBinding.fileTxt.text=it
            }
        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder= AlertDialog.Builder(requireContext())
//        builder.setView()
//    }


    companion object{
        val fragment:DownloadProgressDialog = DownloadProgressDialog()
        fun start() : DownloadProgressDialog {
            return fragment
        }

        fun show(fg:FragmentManager){
            start().show(fg,"DownloadProgressDialog")
        }

        fun stop(){
            fragment.dismiss()
        }
    }

}