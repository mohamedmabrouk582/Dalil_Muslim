package com.mabrouk.dalilmuslim.utils

import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import android.util.SparseArray
import android.webkit.MimeTypeMap
import com.google.common.primitives.Ints
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

class YouTubeExtractor(private val mVideoIdentifier: String) {
    private val mElFields: ArrayList<String> =
        ArrayList(Arrays.asList("embedded", "detailpage", "vevo", ""))
    private var mConnection: HttpsURLConnection? = null

    //endregion
    //region Getters and Setters
    var preferredVideoQualities: List<Int> = Ints.asList(
        YOUTUBE_VIDEO_QUALITY_MEDIUM_360,
        YOUTUBE_VIDEO_QUALITY_SMALL_240, YOUTUBE_VIDEO_QUALITY_HD_720,
        YOUTUBE_VIDEO_QUALITY_HD_1080
    )
    private var mCancelled = false

    //endregion
    //region Public
    fun startExtracting(listener: YouTubeExtractorListener?) {
        var elField = mElFields[0]
        mElFields.removeAt(0)
        if (elField.length > 0) elField = "&el=$elField"
        val language = Locale.getDefault().language
        val link = String.format(
            "https://www.youtube.com/get_video_info?video_id=%s%s&ps=default&eurl=&gl=US&hl=%s",
            mVideoIdentifier,
            elField,
            language
        )
        val youtubeExtractorThread =
            HandlerThread("YouTubeExtractorThread", Process.THREAD_PRIORITY_BACKGROUND)
        youtubeExtractorThread.start()
        val youtubeExtractorHandler = Handler(youtubeExtractorThread.looper)
        val listenerHandler = Handler(Looper.getMainLooper())
        youtubeExtractorHandler.post(object : Runnable {
            override fun run() {
                try {
                    mConnection = URL(link).openConnection() as HttpsURLConnection
                    mConnection!!.setRequestProperty("Accept-Language", language)
                    val reader = BufferedReader(
                        InputStreamReader(
                            mConnection!!.inputStream
                        )
                    )
                    val builder = StringBuilder()
                    var line: String?
                    while ((reader.readLine()
                            .also { line = it }) != null && !mCancelled
                    ) builder.append(line)
                    reader.close()
                    if (!mCancelled) {
                        val result = getYouTubeResult(builder.toString())
                        listenerHandler.post(Runnable {
                            if (!mCancelled && listener != null) {
                                listener.onSuccess(result)
                            }
                        })
                    }
                } catch (e: Exception) {
                    listenerHandler.post(object : Runnable {
                        override fun run() {
                            if (!mCancelled && listener != null) {
                                listener.onFailure(Error(e))
                            }
                        }
                    })
                } finally {
                    if (mConnection != null) {
                        mConnection!!.disconnect()
                    }
                    youtubeExtractorThread.quit()
                }
            }
        })
    }

    fun cancelExtracting() {
        mCancelled = true
    }

    @Throws(UnsupportedEncodingException::class, YouTubeExtractorException::class)
    private fun getYouTubeResult(html: String): YouTubeExtractorResult {
        val video = getQueryMap(html, "UTF-8")
        var videoUri: Uri? = null
        if (video.containsKey("url_encoded_fmt_stream_map")) {
            val streamQueries: MutableList<String> = ArrayList(
                Arrays.asList(
                    *video["url_encoded_fmt_stream_map"]!!
                        .split(",").toTypedArray()
                )
            )
            val adaptiveFmts = video["adaptive_fmts"]
            val split = adaptiveFmts!!.split(",").toTypedArray()
            streamQueries.addAll(Arrays.asList(*split))
            val streamLinks = SparseArray<String?>()
            for (streamQuery: String in streamQueries) {
                val stream = getQueryMap(streamQuery, "UTF-8")
                val type = stream["type"]!!.split(";").toTypedArray()[0]
                var urlString = stream["url"]
                if (urlString != null && MimeTypeMap.getSingleton().hasMimeType(type)) {
                    val signature = stream["sig"]
                    if (signature != null) {
                        urlString = "$urlString&signature=$signature"
                    }
                    if (getQueryMap(urlString, "UTF-8").containsKey("signature")) {
                        streamLinks.put(stream["itag"]!!.toInt(), urlString)
                    }
                }
            }
            for (videoQuality: Int in preferredVideoQualities) {
                if (streamLinks[videoQuality, null] != null) {
                    val streamLink = streamLinks[videoQuality]
                    videoUri = Uri.parse(streamLink)
                    break
                }
            }
            val mediumThumbUri =
                if (video.containsKey("iurlmq")) Uri.parse(video["iurlmq"]) else null
            val highThumbUri = if (video.containsKey("iurlhq")) Uri.parse(video["iurlhq"]) else null
            val defaultThumbUri = if (video.containsKey("iurl")) Uri.parse(video["iurl"]) else null
            val standardThumbUri =
                if (video.containsKey("iurlsd")) Uri.parse(video["iurlsd"]) else null
            return YouTubeExtractorResult(
                videoUri,
                mediumThumbUri,
                highThumbUri,
                defaultThumbUri,
                standardThumbUri
            )
        } else {
            throw YouTubeExtractorException("Status: " + video["status"] + "\nReason: " + video["reason"] + "\nError code: " + video["errorcode"])
        }
    }

    //endregion
    data class YouTubeExtractorResult(
        val videoUri: Uri?,
        val mediumThumbUri: Uri?,
        val highThumbUri: Uri?,
        val defaultThumbUri: Uri?,
        val standardThumbUri: Uri?
    )

    inner class YouTubeExtractorException(detailMessage: String?) : Exception(detailMessage)
    interface YouTubeExtractorListener {
        fun onSuccess(result: YouTubeExtractorResult?)
        fun onFailure(error: Error?)
    }

    companion object {
        //region Fields
        val YOUTUBE_VIDEO_QUALITY_SMALL_240 = 36
        val YOUTUBE_VIDEO_QUALITY_MEDIUM_360 = 18
        val YOUTUBE_VIDEO_QUALITY_HD_720 = 22
        val YOUTUBE_VIDEO_QUALITY_HD_1080 = 37

        //endregion
        //region Private
        @Throws(UnsupportedEncodingException::class)
        private fun getQueryMap(queryString: String, charsetName: String): HashMap<String, String> {
            val map = HashMap<String, String>()
            val fields = queryString.split("&").toTypedArray()
            for (field: String in fields) {
                val pair = field.split("=").toTypedArray()
                if (pair.size == 2) {
                    val key = pair[0]
                    val value = URLDecoder.decode(pair[1], charsetName).replace('+', ' ')
                    map[key] = value
                }
            }
            return map
        }
    }

}