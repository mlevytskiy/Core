package com.core.core_memory_sample.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
class ResponseCache(@Id(assignable = true) var id: Long, var date: Date? = null,
                    var additionalData: String? = null)