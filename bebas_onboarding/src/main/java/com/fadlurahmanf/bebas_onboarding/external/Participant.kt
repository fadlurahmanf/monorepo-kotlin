package com.fadlurahmanf.bebas_onboarding.external

import android.util.Log
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.VideoTrack


abstract class Participant(
    open val participantName: String, open val session: RTCSession
) {

    lateinit var connectionId:String
    private var iceCandidateList: List<IceCandidate> = ArrayList()
    private var peerConnection: PeerConnection? = null
    lateinit var audioTrack: AudioTrack
    lateinit var videoTrack: VideoTrack
    private lateinit var mediaStream: MediaStream

    constructor(connectionId: String, participantName: String, session: RTCSession) : this(
        participantName, session
    ){
      this.connectionId = connectionId
    }

    fun getIceCandidateList(): List<IceCandidate> {
        return this.iceCandidateList
    }

    open fun setPeerConnection(peerConnection: PeerConnection) {
        this.peerConnection = peerConnection
    }

    open fun getPeerConnection(): PeerConnection? {
        return this.peerConnection
    }

    open fun getMediaStream(): MediaStream? {
        return this.mediaStream
    }

    open fun setMediaStream(mediaStream: MediaStream) {
        this.mediaStream = mediaStream
    }

    open fun dispose() {
        try {
            peerConnection?.close()
        } catch (e: IllegalStateException) {
            Log.e("BebasLoggerRTC", e.message!!)
        }
    }

}