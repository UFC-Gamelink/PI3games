import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.model.PostModel
import kotlinx.coroutines.launch
import java.util.*


class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    // Atualizar a localização
    fun updateLocation(location: Location) {
        _location.value = location
    }

    fun getLocation(): Location? {
        return _location.value
    }

    // Buscar por um endereço
    fun searchAddress(addressString: String) {
        val geocoder = Geocoder(getApplication(), Locale.getDefault())
        val addressList = geocoder.getFromLocationName(addressString, 1)
        if (addressList != null) {
                val location = addressList.get(0)
                _address.value = "${location.latitude}, ${location.longitude}"

                updateLocation(Location("").apply {
                    latitude = location.latitude
                    longitude = location.longitude

                })
            }
        }





    }


