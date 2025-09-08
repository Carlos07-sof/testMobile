package com.example.foodike.domain.model

data class RemoteResult(

val status: String,
val data: List<Data>,
val message: String
)