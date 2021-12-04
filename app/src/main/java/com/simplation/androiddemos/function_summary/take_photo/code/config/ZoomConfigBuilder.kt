package com.simplation.androiddemos.function_summary.take_photo.code.config


class ZoomConfigBuilder {
    private var outputPath: String? = null
    private var aspectX = 1
    private var aspectY = 1
    private var outputX = 200
    private var outputY = 200

    fun setOutputPath(outputPath: String?): ZoomConfigBuilder {
        this.outputPath = outputPath
        return this
    }

    fun setAspectX(aspectX: Int): ZoomConfigBuilder {
        this.aspectX = aspectX
        return this
    }

    fun setAspectY(aspectY: Int): ZoomConfigBuilder {
        this.aspectY = aspectY
        return this
    }

    fun setOutputX(outputX: Int): ZoomConfigBuilder {
        this.outputX = outputX
        return this
    }

    fun setOutputY(outputY: Int): ZoomConfigBuilder {
        this.outputY = outputY
        return this
    }

    fun createZoomConfig(): ZoomConfig {
        return ZoomConfig(outputPath!!, aspectX, aspectY, outputX, outputY)
    }
}