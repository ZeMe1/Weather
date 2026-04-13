package kz.zeme.weather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.service.LocationService
import kotlin.coroutines.resume

class DefaultLocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
): LocationService {
    override suspend fun getCurrentCoordinates(): Coordinates? {
        val hasAccessCoarseLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessFineLocation = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val hasLocationPermission = hasAccessFineLocation || hasAccessCoarseLocation

        if (!hasLocationPermission || !isGpsEnabled) return null

        return suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful && result != null) {
                        continuation.resume(Coordinates(result.latitude, result.longitude))
                    } else continuation.resume(null)
                    return@apply
                }
                addOnSuccessListener { location ->
                    if (location != null) continuation.resume(Coordinates(location.latitude, location.longitude))
                    else continuation.resume(null)
                }
                addOnFailureListener { continuation.resume(null) }
                addOnCanceledListener { continuation.cancel() }
            }
        }
    }
}