package com.core.sample.memory

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class UserInfo(
    @Id(assignable = true)
    var id: Long = ID,
    var statusBarHeight: Int,
    var language: Byte,
    var phoneNumberFromSystem: String?,
    var registrationToken: String? = null) {

    @Convert(converter = RegistrationInfoConverter::class, dbType = String::class)
    var telegramUser: RegistrationInfo? = null //tmp registrationInfo

    companion object {
        @JvmField val ID = 1L
    }
}