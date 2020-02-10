package com.core.core_memory_sample.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

@Entity
class ResponseCache(@Id(assignable = true) var id: Long = 1L, var date: Date? = null,
                    var additionalData: String? = null) {

    var someObj: ToOne<SomeObj> = ToOne(this, ResponseCache_.someObj)

}