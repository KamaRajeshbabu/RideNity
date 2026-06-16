package com.example.api

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
enum class UserType {
    RIDER, DRIVER
}

@Serializable
enum class IncidentSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

@Serializable
data class TrustProfile(
    val userId: String,
    val userType: UserType,
    val score: Double,
    val totalTrips: Int,
    val totalIncidents: Int,
    val verificationLevel: String
)

@Serializable
data class IncidentReportRequest(
    val tripId: String,
    val reportedUserId: String,
    val category: String,
    val severity: IncidentSeverity,
    val description: String,
    val evidenceUrls: List<String>
)

@Serializable
data class IncidentReportResponse(
    val incidentId: String,
    val status: String,
    val loggedAt: String
)

interface TrustLayerApiService {
    @GET("v1/trust/profiles/{userId}")
    suspend fun getTrustProfile(
        @Path("userId") userId: String,
        @Query("type") type: UserType
    ): TrustProfile

    @POST("v1/trust/incidents")
    suspend fun reportIncident(
        @Body request: IncidentReportRequest
    ): IncidentReportResponse
}
