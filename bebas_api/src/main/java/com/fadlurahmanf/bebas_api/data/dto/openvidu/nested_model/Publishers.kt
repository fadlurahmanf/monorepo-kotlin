package com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model

data class Publishers(
    var createdAt: Long? = null,
    var streamId: String? = null,
    var mediaOptions: MediaOptions? = MediaOptions()
)
