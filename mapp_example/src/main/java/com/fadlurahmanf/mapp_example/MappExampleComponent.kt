package com.fadlurahmanf.mapp_example

import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity
import com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity
import com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.CallActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.ListRoomActivity
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent
import dagger.Component

@Component(
    dependencies = [
        CorePlatformComponent::class,
        MappConfigComponent::class,
        MappFirebaseDatabaseComponent::class
    ]
)
interface MappExampleComponent {

    @Component.Factory
    interface Factory {
        fun create(
            corePlatform: CorePlatformComponent,
            mapp: MappConfigComponent,
            mappFirebaseDatabase: MappFirebaseDatabaseComponent
        ): MappExampleComponent
    }

    fun inject(activity: ExampleActivity)
    fun inject(activity: BiometricActivity)
    fun inject(activity: NotificationActivity)
    fun inject(activity: ListRoomActivity)
    fun inject(activity: CallActivity)
}