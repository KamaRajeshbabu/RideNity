package com.example.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rideType: String,
    val status: String,
    val date: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false,
    val passenger: String = "",
    val driver: String = ""
)

@Entity(tableName = "trust_profiles")
data class LocalTrustProfile(
    @PrimaryKey val userId: String,
    val score: Double,
    val totalTrips: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Dao
interface RideDao {
    @Query("SELECT * FROM rides ORDER BY date DESC")
    fun getAllRides(): Flow<List<Ride>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: Ride)
}

@Dao
interface TrustProfileDao {
    @Query("SELECT * FROM trust_profiles WHERE userId = :userId")
    fun getProfile(userId: String): Flow<LocalTrustProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: LocalTrustProfile)
}

@Database(entities = [Ride::class, LocalTrustProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
    abstract fun trustProfileDao(): TrustProfileDao
}
