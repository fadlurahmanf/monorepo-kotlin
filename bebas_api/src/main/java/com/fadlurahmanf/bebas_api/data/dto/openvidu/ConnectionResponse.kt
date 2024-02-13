package com.fadlurahmanf.bebas_api.data.dto.openvidu

import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.CustomIceServers
import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.Publishers
import com.fadlurahmanf.bebas_api.data.dto.openvidu.nested_model.Subscribers
import com.google.gson.annotations.SerializedName

data class ConnectionResponse(
    var id: String? = null,
    @SerializedName("object")
    var objects: String? = null,
    var type: String? = null,
    var status: String? = null,
    var sessionId: String? = null,
    var createdAt: Long? = null,
    var activeAt: Long? = null,
    var location: String? = null,
    var ip: String? = null,
    var platform: String? = null,
    var token: String? = null,
    var serverData: String? = null,
    var clientData: String? = null,
    var record: Boolean? = null,
    var role: String? = null,
//    var kurentoOptions: KurentoOptions? = KurentoOptions(),
    var rtspUri: String? = null,
    var adaptativeBitrate: String? = null,
    var onlyPlayWithSubscribers: String? = null,
    var networkCache: String? = null,
    var publishers: ArrayList<Publishers> = arrayListOf(),
    var subscribers: ArrayList<Subscribers> = arrayListOf(),
    var customIceServers: ArrayList<CustomIceServers> = arrayListOf()
)
