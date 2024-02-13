package com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model

data class MediaOptions(
    var hasAudio: Boolean? = null,
    var audioActive: Boolean? = null,
    var hasVideo: Boolean? = null,
    var videoActive: Boolean? = null,
    var typeOfVideo: String? = null,
    var frameRate: Int? = null,
    var videoDimensions: String? = null,
//    @SerializedName("filter") var filter: Filter? = Filter()
)
