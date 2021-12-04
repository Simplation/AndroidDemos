package com.simplation.androiddemos.function_summary.take_photo.code.config


class TakeVideoConfigBuilder {
    private var videoPath: String? = null
    private var quality = 1f
    private var length = 5

    fun setVideoPath(videoPath: String?): TakeVideoConfigBuilder {
        this.videoPath = videoPath
        return this
    }

    fun setQuality(quality: Float): TakeVideoConfigBuilder {
        this.quality = quality
        return this
    }

    fun setLength(length: Int): TakeVideoConfigBuilder {
        this.length = length
        return this
    }

    fun createTakeVideoConfig(): TakeVideoConfig {
        return TakeVideoConfig(videoPath!!, quality, length)
    }
}