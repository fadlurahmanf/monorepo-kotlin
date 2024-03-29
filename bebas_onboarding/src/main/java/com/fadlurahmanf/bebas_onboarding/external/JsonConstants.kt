package com.fadlurahmanf.bebas_onboarding.external

object JsonConstants {
    const val PARTICIPANT_JOINED = "participantJoined"
    const val PARTICIPANT_LEFT = "participantLeft"
    const val PARTICIPANT_PUBLISHED = "participantPublished"
    const val ICE_CANDIDATE = "iceCandidate"
    const val PARTICIPANT_UNPUBLISHED = "participantUnpublished"
    const val PARTICIPANT_EVICTED = "participantEvicted"
    const val RECORDING_STARTED = "recordingStarted"
    const val RECORDING_STOPPED = "recordingStopped"
    const val SEND_MESSAGE = "sendMessage"
    const val STREAM_PROPERTY_CHANGED = "streamPropertyChanged"
    const val FILTER_EVENT_DISPATCHED = "filterEventDispatched"
    const val MEDIA_ERROR = "mediaError"

    // RPC outgoing methods
    const val PING_METHOD = "ping"
    const val JOINROOM_METHOD = "joinRoom"
    const val LEAVEROOM_METHOD = "leaveRoom"
    const val PUBLISHVIDEO_METHOD = "publishVideo"
    const val ONICECANDIDATE_METHOD = "onIceCandidate"
    const val PREPARERECEIVEVIDEO_METHOD = "prepareReceiveVideoFrom"
    const val RECEIVEVIDEO_METHOD = "receiveVideoFrom"
    const val UNSUBSCRIBEFROMVIDEO_METHOD = "unsubscribeFromVideo"
    const val SENDMESSAGE_ROOM_METHOD = "sendMessage"
    const val UNPUBLISHVIDEO_METHOD = "unpublishVideo"
    const val STREAMPROPERTYCHANGED_METHOD = "streamPropertyChanged"
    const val NETWORKQUALITYLEVELCHANGED_METHOD = "networkQualityLevelChanged"
    const val FORCEDISCONNECT_METHOD = "forceDisconnect"
    const val FORCEUNPUBLISH_METHOD = "forceUnpublish"
    const val APPLYFILTER_METHOD = "applyFilter"
    const val EXECFILTERMETHOD_METHOD = "execFilterMethod"
    const val REMOVEFILTER_METHOD = "removeFilter"
    const val ADDFILTEREVENTLISTENER_METHOD = "addFilterEventListener"
    const val REMOVEFILTEREVENTLISTENER_METHOD = "removeFilterEventListener"

    const val JSON_RPCVERSION = "2.0"

    const val VALUE = "value"
    const val PARAMS = "params"
    const val METHOD = "method"
    const val ID = "id"
    const val RESULT = "result"
    const val ERROR = "error"
    const val MEDIA_SERVER = "mediaServer"

    const val SESSION_ID = "sessionId"
    const val SDP_ANSWER = "sdpAnswer"
    const val METADATA = "metadata"

    const val ICE_SERVERS = "customIceServers"
}