package com.fadlurahmanf.bebas_storage.domain.converter

import androidx.room.TypeConverter
import com.fadlurahmanf.bebas_shared.data.flow.onboarding.OnboardingFlow

class BebasConverters {
    @TypeConverter
    fun toOnboardingFlow(value: String?): OnboardingFlow? {
        return if (value != null) {
            enumValueOf<OnboardingFlow>(value)
        } else {
            null
        }
    }

    @TypeConverter
    fun fromOnboardingFlow(value: OnboardingFlow?): String? {
        return if (value != null) {
            return value.name
        } else {
            return null
        }
    }

}