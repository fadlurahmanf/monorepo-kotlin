package com.fadlurahmanf.mapp_example

import android.content.Context
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity
import com.fadlurahmanf.mapp_example.presentation.crypto.AesActivity
import com.fadlurahmanf.mapp_example.presentation.crypto.ED25119Activity
import com.fadlurahmanf.mapp_example.presentation.crypto.RsaActivity
import com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity
import com.fadlurahmanf.mapp_example.presentation.logger.LoggerActivity
import com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.CallActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.ListRoomActivity
import com.fadlurahmanf.mapp_example.presentation.session.ActivityAfterLogin
import com.fadlurahmanf.mapp_example.presentation.session.LoginActivity
import com.fadlurahmanf.mapp_fcm.MappFcmComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CoreCryptoComponent::class,
        CorePlatformComponent::class,
        MappConfigComponent::class,
        MappFcmComponent::class,
        MappFirebaseDatabaseComponent::class
    ]
)
interface MappExampleComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            coreCrypto: CoreCryptoComponent,
            corePlatform: CorePlatformComponent,
            mappConfig: MappConfigComponent,
            mappFcm: MappFcmComponent,
            mappFirebaseDatabase: MappFirebaseDatabaseComponent
        ): MappExampleComponent
    }

    fun inject(activity: ExampleActivity)
    fun inject(activity: BiometricActivity)
    fun inject(activity: NotificationActivity)
    fun inject(activity: ListRoomActivity)
    fun inject(activity: CallActivity)
    fun inject(activity: RsaActivity)
    fun inject(activity: AesActivity)
    fun inject(activity: ED25119Activity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ActivityAfterLogin)
    fun inject(activity: LoggerActivity)
}