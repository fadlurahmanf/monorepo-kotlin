package com.fadlurahmanf.mapp_example

import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.core_platform.CorePlatformModule
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.mapp_config.MappComponent
import com.fadlurahmanf.mapp_example.presentation.ExampleActivity
import dagger.Component

@Component(
    dependencies = [MappComponent::class, CorePlatformComponent::class]
)
interface MappExampleComponent {

    @Component.Factory
    interface Factory {
        fun create(mapp: MappComponent, corePlatform: CorePlatformComponent): MappExampleComponent
    }

    fun inject(activity: ExampleActivity)
}