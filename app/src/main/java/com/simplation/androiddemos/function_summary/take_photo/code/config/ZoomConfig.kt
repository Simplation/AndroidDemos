package com.simplation.androiddemos.function_summary.take_photo.code.config

data class ZoomConfig(
    var outputPath: String?,
    val aspectX: Int = 1,
    val aspectY: Int = 1,
    val outputX: Int = 200,
    val outputY: Int = 200,
)
