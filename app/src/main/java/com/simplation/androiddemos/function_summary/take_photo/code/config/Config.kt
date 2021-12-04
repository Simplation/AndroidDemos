package com.simplation.androiddemos.function_summary.take_photo.code.config

data class Config(
    val isStartZoom: Boolean,
    val zoomConfig: ZoomConfig?,
    val type: TakeType? = TakeType.PICK_GALLERY_PHOTO,
    val takeVideoConfig: TakeVideoConfig?
)