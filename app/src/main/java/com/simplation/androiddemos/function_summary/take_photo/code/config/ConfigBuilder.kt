package com.simplation.androiddemos.function_summary.take_photo.code.config

class ConfigBuilder {
    private var isStartZoom = false
    private var config: ZoomConfig? = null
    private var type: TakeType? = null
    private var takeVideoConfig: TakeVideoConfig? = null
    fun setIsStartZoom(isStartZoom: Boolean): ConfigBuilder {
        this.isStartZoom = isStartZoom
        return this
    }

    fun setConfig(config: ZoomConfig?): ConfigBuilder {
        this.config = config
        return this
    }

    fun setType(type: TakeType): ConfigBuilder {
        this.type = type
        return this
    }

    fun setTakeVideoConfig(takeVideoConfig: TakeVideoConfig?): ConfigBuilder {
        this.takeVideoConfig = takeVideoConfig
        return this
    }

    fun createConfig(): Config {
        return Config(isStartZoom, config, type, takeVideoConfig)
    }
}