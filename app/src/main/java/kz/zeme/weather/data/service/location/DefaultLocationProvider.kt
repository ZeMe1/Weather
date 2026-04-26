package kz.zeme.weather.data.service.location

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kz.zeme.weather.core.repository.LocationException
import kz.zeme.weather.domain.model.Coordinates
import kz.zeme.weather.domain.service.LocationService
import java.util.Locale
import kotlin.coroutines.resume

class DefaultLocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) : LocationService {

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentCoordinates(): Coordinates {
        if (!hasAnyLocationPermission()) throw LocationException.PermissionDenied
        if (!isLocationEnabled()) throw LocationException.GpsDisabled

        return try {
            val lastLocation = fusedLocationProviderClient.lastLocation.await()
            if (lastLocation != null) {
                return Coordinates(lastLocation.latitude, lastLocation.longitude)
            }

            val cancellationSource = CancellationTokenSource()
            val location = withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationSource.token
                ).await()
            }

            if (location == null) {
                cancellationSource.cancel()
                throw LocationException.LocationUnavailable
            }

            Coordinates(location.latitude, location.longitude)

        } catch (e: LocationException) {
            throw e
        } catch (e: Exception) {
            throw LocationException.LocationUnavailable
        }
    }

    private suspend fun hasAnyLocationPermission(): Boolean = withContext(Dispatchers.IO) {
        ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun isLocationEnabled(): Boolean = withContext(Dispatchers.IO) {
        val manager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override suspend fun getCityName(latitude: Double, longitude: Double): String? = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(application, Locale.getDefault())

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (continuation.isActive) {
                                continuation.resume(addresses.firstOrNull()?.resolveCity())
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        }
                    })
                }
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)
                    ?.firstOrNull()
                    ?.resolveCity()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun Address.resolveCity(): String? = locality ?: subAdminArea ?: adminArea

    companion object {
        private const val LOCATION_TIMEOUT_MS = 5_000L
    }
}