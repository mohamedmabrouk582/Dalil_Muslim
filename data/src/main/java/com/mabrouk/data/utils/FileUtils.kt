package com.mabrouk.data.utils

import android.os.Environment
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream


object FileUtils {

   fun saveAudio(bytes: ResponseBody,url:String, suraNum: Int, ayaNum: Int): Boolean{
      //val root = if (hasSdCard()){
//         val root=  Environment.getExternalStorageState()
       //}
//      else{
//           Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
//       }
       val root = Environment.getExternalStorageDirectory().toString()
       val dir=File("$root/quran_audios/$url/$suraNum")
        if(!dir.exists()) dir.mkdirs()
       val fileName="$suraNum$ayaNum.mp3"
       val file=File(dir, fileName)
       if (file.exists()) return true
       return try {
           val out = FileOutputStream(file)
           out.write(bytes.bytes())
           out.flush();
           out.close();
           //out.write(bytes)
           true
       }catch (e: Exception){
           e.printStackTrace()
           false
       }
   }

    fun fileIsFound(url:String,suraNum: Int, ayaNum: Int)  = File(getAudioPath(url,suraNum, ayaNum)).exists()

    fun getAudioPath(url:String,suraNum: Int, ayaNum: Int):String{
//        val root = if (hasSdCard()){
//            Environment.getExternalStorageState()
//        }else{
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()
//        }
        val root = Environment.getExternalStorageDirectory().toString()
        val dir=File("$root/quran_audios/$url/$suraNum")
        val fileName="$suraNum$ayaNum.mp3"
        val file=File(dir, fileName)
        return file.absolutePath
    }

    private fun hasSdCard() : Boolean{
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        val isSDSupportedDevice = Environment.isExternalStorageRemovable()
        return isSDSupportedDevice && isSDPresent
    }

}