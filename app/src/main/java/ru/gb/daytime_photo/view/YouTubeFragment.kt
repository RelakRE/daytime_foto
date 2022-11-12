package ru.gb.daytime_photo.view

import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import java.util.regex.Pattern

class YouTubeFragment : YouTubePlayerSupportFragmentX(), YouTubePlayer.OnInitializedListener {

    var url: String? = ""

    fun getUrlId(fullUrl: String): String {
        val regex = "(?<=watch\\?v=|videos\\/|embed\\/)[^#\\&\\?]*"
        val pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher = pattern.matcher(fullUrl)

        return if (matcher.find()) matcher.group().toString()
        else ""
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        p2: Boolean
    ) {
        player?.loadVideo(url)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
    }
}