package com.fadlurahmanf.bebas_api.data.dto.openvidu

import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.CustomIceServers

data class ConnectionRequest(
    var type: String = "WEBRTC",
    var data: String? = null,
    var record: Boolean = false,
    var role: String = "PUBLISHER",
    var customIceServers: ArrayList<CustomIceServers>? = null
)
