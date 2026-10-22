package com.setometro.app.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceSearchApiService {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("format") format: String = "jsonv2",
        @Query("countrycodes") countryCodes: String = "es",
        @Query("limit") limit: Int = 8,
        @Query("addressdetails") addressDetails: Int = 1
    ): Response<List<PlaceSearchResult>>
}

data class PlaceSearchResult(
    val name: String?,
    @SerializedName("display_name")
    val displayName: String?,
    val lat: String,
    val lon: String,
    val type: String?,
    val address: PlaceAddress?
)

data class PlaceAddress(
    val province: String?,
    val state: String?,
    val county: String?
)
