package com.project.aidoctor.data.remote.home

import com.project.aidoctor.data.entities.User

import retrofit2.Response
import retrofit2.http.*

interface HomeService {
    @GET("/disease/")
    suspend fun disease(
    ): Response<DiseaseResponse>

}