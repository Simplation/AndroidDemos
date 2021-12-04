package com.simplation.androiddemos.function_summary.take_photo.code.config


enum class TakeType(val type: Int) {
    PICK_GALLERY_PHOTO(0),
    PICK_GALLERY_VIDEO(1),
    PICK_GALLERY_PHOTO_VIDEO(2),
    TAKE_CAMERA_PHOTO(3),
    TAKE_CAMERA_VIDEO(4)
}
