package com.core.core_memory_sample.model

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class SomeObj(@Id(assignable = true) var id: Long = 1L) {

}