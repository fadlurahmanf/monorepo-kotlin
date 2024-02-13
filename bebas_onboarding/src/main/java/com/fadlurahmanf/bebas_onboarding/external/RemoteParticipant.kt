package com.fadlurahmanf.bebas_onboarding.external

class RemoteParticipant(
    override val participantName: String,
    override val session: RTCSession,
    connectionIdInput: String,
) : Participant(connectionIdInput, participantName, session) {
}