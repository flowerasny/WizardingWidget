package com.flowerasny.widget.dogs

import retrofit2.http.GET

interface ElixirsApi {

    @GET("/elixirs")
    suspend fun getElixirs(): List<ElixirData>
}

data class ElixirData(
    val name: String,
    val difficulty: ElixirDifficulty,
)

enum class ElixirDifficulty {
    Unknown, Advanced, Moderate, Beginner, OrdinaryWizardingLevel, OneOfAKind;
}
