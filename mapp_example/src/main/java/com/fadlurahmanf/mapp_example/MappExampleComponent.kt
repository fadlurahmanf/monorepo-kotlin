package com.fadlurahmanf.mapp_example

import com.fadlurahmanf.mapp_config.MappComponent
import com.fadlurahmanf.mapp_example.presentation.ExampleActivity
import dagger.Component

@Component(dependencies = [MappComponent::class])
interface MappExampleComponent {
    @Component.Factory
    interface Factory {
        fun create(mappComponent: MappComponent): MappExampleComponent
    }

    fun inject(activity: ExampleActivity)
}