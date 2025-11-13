package com.example.frontendgestoreta.data.models

import java.time.LocalDate
import java.time.LocalTime

class EventFilterDTO (
    val beforeTime: LocalTime? = null,
    val afterTime: LocalTime? = null,
    val beforeDate : LocalDate? = null,
    val afterDate: LocalDate? = null,
    val public : Boolean? = null,
    val maxPeople: Long? = null,
    val maxPeopleNumber: Long? = null,
    val minPeopleNumber: Long? = null,
    val location : String? = null,
    val title : String? = null,
    val description : String? = null,
    val fallaId: Long? = null,
    val tagId: Long? = null,
)
