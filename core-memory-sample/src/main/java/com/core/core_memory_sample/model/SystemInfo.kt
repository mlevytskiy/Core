package com.core.core_memory_sample.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id



@Entity
data class SystemInfo(var statusBarHeight: Int) {
    companion object {
        @JvmField val ID = 1L
    }
    @Id(assignable = true)
    var id: Long = ID
}