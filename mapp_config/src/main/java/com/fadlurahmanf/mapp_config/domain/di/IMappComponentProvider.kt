package com.fadlurahmanf.mapp_config.domain.di

import com.fadlurahmanf.mapp_config.MappComponent

interface IMappComponentProvider {
    fun provideMappComponent():MappComponent
}