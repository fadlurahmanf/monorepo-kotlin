package com.fadlurahmanf.mapp_example.presentation.rtc

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityCallBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.viewmodel.CallViewModel
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver
import javax.inject.Inject

class CallActivity : BaseExampleActivity<ActivityCallBinding>(
    ActivityCallBinding::inflate
) {
    override fun injectActivity() {
        component.inject(this)
    }

    var viewModel: CallViewModel = CallViewModel()

    override fun setup() {
        viewModel.setContext(this, object : PeerConnection.Observer {
            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
                TODO("Not yet implemented")
            }

            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
                TODO("Not yet implemented")
            }

            override fun onIceConnectionReceivingChange(p0: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
                TODO("Not yet implemented")
            }

            override fun onIceCandidate(p0: IceCandidate?) {
                TODO("Not yet implemented")
            }

            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
                TODO("Not yet implemented")
            }

            override fun onAddStream(p0: MediaStream?) {
                TODO("Not yet implemented")
            }

            override fun onRemoveStream(p0: MediaStream?) {
                TODO("Not yet implemented")
            }

            override fun onDataChannel(p0: DataChannel?) {
                TODO("Not yet implemented")
            }

            override fun onRenegotiationNeeded() {
                TODO("Not yet implemented")
            }

            override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
                TODO("Not yet implemented")
            }

        })
        checkCameraPermission()
        binding.localCreateOffer.setOnClickListener {

        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                onSuccessCheckCamera()
            }
        }

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            println("MASUK AUDIO PERMISSION = $it")
            if (it) {
                onSuccessCheckAudio()
            }
        }

    private fun checkCameraPermission() {
        when (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                onSuccessCheckCamera()
            }

            else -> {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun onSuccessCheckCamera() {
        checkAudioPermission()
    }

    private fun checkAudioPermission() {
        when (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)) {
            PackageManager.PERMISSION_GRANTED -> {
                onSuccessCheckAudio()
            }

            else -> {
                audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun onSuccessCheckAudio() {
        println("MASUK SUCCESS CHECK AUDIO")
    }
}