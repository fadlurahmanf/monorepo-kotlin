package com.fadlurahmanf.mapp_example

import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity
import com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity
import dagger.Component

@Component(
    dependencies = [MappConfigComponent::class, CorePlatformComponent::class]
)
interface MappExampleComponent {

    @Component.Factory
    interface Factory {
        fun create(mapp: MappConfigComponent, corePlatform: CorePlatformComponent): MappExampleComponent
    }

    fun inject(activity: ExampleActivity)
    fun inject(activity: BiometricActivity)
}