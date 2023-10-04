package com.fadlurahmanf.mapp_example.presentation.example

import android.content.Intent
import android.util.Log
import com.fadlurahmanf.mapp_analytic.external.helper.AnalyticEvent
import com.fadlurahmanf.mapp_analytic.external.helper.AnalyticHelper
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.data.dto.model.MenuModel
import com.fadlurahmanf.mapp_example.databinding.ActivityExampleBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity
import com.fadlurahmanf.mapp_example.presentation.crypto.AesActivity
import com.fadlurahmanf.mapp_example.presentation.crypto.ED25119Activity
import com.fadlurahmanf.mapp_example.presentation.crypto.RsaActivity
import com.fadlurahmanf.mapp_example.presentation.mlkit.FaceDetectorActivity
import com.fadlurahmanf.mapp_example.presentation.mlkit.ObjectLabelingActivity
import com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.ListRoomActivity
import com.fadlurahmanf.mapp_example.presentation.shortcut.ShortcutActivity
import com.fadlurahmanf.mapp_example.presentation.vplayer.HLSPlayerActivity
import com.fadlurahmanf.mapp_example.presentation.vplayer.Mp4PlayerPlayerActivity
import com.fadlurahmanf.mapp_fcm.domain.repositories.MappFcmRepository
import javax.inject.Inject

class ExampleActivity : BaseExampleActivity<ActivityExampleBinding>(
    ActivityExampleBinding::inflate
), MenuRecycleView.CallBack {

    companion object {
        val menus = arrayListOf<MenuModel>(
            MenuModel(
                menuId = "BIOMETRIC",
                menuTitle = "Biometric",
                menuSubTitle = "Go To Biometric",
                icon = R.drawable.outline_featured_play_list_24
            ),
            MenuModel(
                menuId = "NOTIFICATION",
                menuTitle = "Notification",
                menuSubTitle = "Go To Notification",
                icon = R.drawable.outline_featured_play_list_24
            ),
            MenuModel(
                menuId = "FCM_TOKEN",
                menuTitle = "FCM TOKEN",
                menuSubTitle = "Get FCM Token",
                icon = R.drawable.outline_featured_play_list_24
            ),
            MenuModel(
                menuId = "SHORTCUT",
                menuTitle = "Shortcut",
                menuSubTitle = "Go To Shortcut",
                icon = R.drawable.outline_featured_play_list_24
            ),
            MenuModel(
                menuId = "RSA",
                menuTitle = "RSA",
                menuSubTitle = "Go To RSA SCREEN",
                icon = R.drawable.outline_lock_24
            ),
            MenuModel(
                menuId = "AES",
                menuTitle = "AES",
                menuSubTitle = "Go To AES SCREEN",
                icon = R.drawable.outline_lock_24
            ),
            MenuModel(
                menuId = "ED25119",
                menuTitle = "ED25119",
                menuSubTitle = "Go To ED25119 SCREEN",
                icon = R.drawable.outline_lock_24
            ),
            MenuModel(
                menuId = "HLS_VIDEO_PLAYER",
                menuTitle = "HLS VIDEO PLAYER",
                menuSubTitle = "Play HLS Remote Video",
                icon = R.drawable.outline_ondemand_video_24
            ),
            MenuModel(
                menuId = "MP4_VIDEO_PLAYER",
                menuTitle = "MP4 VIDEO PLAYER",
                menuSubTitle = "Play MP4 Remote Video",
                icon = R.drawable.outline_ondemand_video_24
            ),
            MenuModel(
                menuId = "OBJECT_LABELING_MLKIT",
                menuTitle = "OBJECT LABELING",
                menuSubTitle = "Go To Object Labeling MLKIT",
                icon = R.drawable.outline_camera_alt_24
            ),
            MenuModel(
                menuId = "FACE_DETECTOR_MLKIT",
                menuTitle = "FACE DETECTOR",
                menuSubTitle = "Go To Face Detector",
                icon = R.drawable.outline_camera_alt_24
            ),
            MenuModel(
                menuId = "LIST_ROOM_RTC",
                menuTitle = "RTC",
                menuSubTitle = "Go To List Room",
                icon = R.drawable.outline_featured_play_list_24
            ),
        )
    }

    override fun setup() {
        initAdapter()
    }

    private lateinit var adapter: MenuRecycleView
    private fun initAdapter() {
        adapter = MenuRecycleView()
        adapter.setList(menus)
        adapter.setCallBack(this)
        binding.rv.adapter = adapter
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onMenuClicked(menu: MenuModel) {
        when (menu.menuId) {
            "BIOMETRIC" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_biometric_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, BiometricActivity::class.java)
                startActivity(intent)
            }

            "NOTIFICATION" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_notif_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
            }

            "FCM_TOKEN" -> {
                mappFcmRepository.getFcmToken().addOnSuccessListener {
                    Log.d("MappLogger", "FCM TOKEN: $it")
                }
            }

            "SHORTCUT" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_shortcut_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, ShortcutActivity::class.java)
                startActivity(intent)
            }

            "RSA" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_rsa_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, RsaActivity::class.java)
                startActivity(intent)
            }

            "AES" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_aes_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, AesActivity::class.java)
                startActivity(intent)
            }

            "ED25119" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_ed25119_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, ED25119Activity::class.java)
                startActivity(intent)
            }

            "HLS_VIDEO_PLAYER" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_hls_vplayer_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, HLSPlayerActivity::class.java)
                startActivity(intent)
            }

            "MP4_VIDEO_PLAYER" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_mp4_vplayer_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, Mp4PlayerPlayerActivity::class.java)
                startActivity(intent)
            }

            "OBJECT_LABELING_MLKIT" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_object_labeling_mlkit_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, ObjectLabelingActivity::class.java)
                startActivity(intent)
            }

            "FACE_DETECTOR_MLKIT" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_face_detector_mlkit_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, FaceDetectorActivity::class.java)
                startActivity(intent)
            }

            "LIST_ROOM_RTC" -> {
                AnalyticHelper.logEvent(
                    AnalyticEvent.ex_list_room_rtc_clicked,
                    AnalyticEvent.defaultParamMap(this)
                )
                val intent = Intent(this, ListRoomActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Inject
    lateinit var mappFcmRepository: MappFcmRepository
}