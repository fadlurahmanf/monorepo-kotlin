package com.fadlurahmanf.mapp.di

import android.content.Context
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.core_platform.CorePlatformFakeModule
import com.fadlurahmanf.core_platform.CorePlatformModule
import com.fadlurahmanf.mapp.MainActivity
import com.fadlurahmanf.mapp.MappApplication
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@Subcomponent
interface ExampleComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create():ExampleComponent
    }

    fun inject(mainActivity: MainActivity)
}

@Component(modules = [CorePlatformFakeModule::class])
interface MappDevComponent:MappComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MappComponent
    }
}

interface MappComponent {
    fun inject(app: MappApplication)
    fun corePlatform(): CorePlatformComponent.Factory
    fun example():ExampleComponent.Factory
}
