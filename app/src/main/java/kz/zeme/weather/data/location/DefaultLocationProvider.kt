package kz.zeme.weather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.service.LocationService
import java.util.Locale
import kotlin.coroutines.resume

class DefaultLocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) : LocationService {

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentCoordinates(): Coordinates {
        if (!hasAnyLocationPermission()) throw LocationException.PermissionDenied
        if (!isLocationEnabled()) throw LocationException.GpsDisabled

        return try {
            val lastLocation = fusedLocationProviderClient.lastLocation.await()
            if (lastLocation != null) {
                return Coordinates(lastLocation.latitude, lastLocation.longitude)
            }

            val location = fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).await()

            location?.let { Coordinates(it.latitude, it.longitude) }
                ?: throw LocationException.LocationUnavailable
        } catch (e: LocationException) {
            throw e
        } catch (e: Exception) {
            throw LocationException.LocationUnavailable
        }
    }

    private fun hasAnyLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun isLocationEnabled(): Boolean {
        val manager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override suspend fun getCityName(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(application, Locale.getDefault())
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    continuation.resume(addresses.firstOrNull()?.resolveCity())
                }
            }
        } else {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latitude, longitude, 1)
                ?.firstOrNull()
                ?.resolveCity()
        }
    }

    private fun Address.resolveCity(): String? = locality ?: subAdminArea ?: adminArea
}