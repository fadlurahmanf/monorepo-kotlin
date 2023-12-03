package com.fadlurahmanf.bebas_api.data.dto.openvidu

import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.Connections
import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.DefaultRecordingProperties
import com.google.gson.annotations.SerializedName

data class SessionResponse(
    var id: String? = null,
    @SerializedName("object")
    var objects: String? = null,
    var createdAt: Long? = null,
    var mediaMode: String? = null,
    var recordingMode: String? = null,
    var defaultRecordingProperties: DefaultRecordingProperties? = null,
    var customSessionId: String? = null,
    var connections: Connections? = null,
    var recording: Boolean? = null,
    var broadcasting: Boolean? = null,
    var forcedVideoCodec: String? = null,
    var allowTranscoding: Boolean? = null,
    var mediaNodeId: String? = null
)
