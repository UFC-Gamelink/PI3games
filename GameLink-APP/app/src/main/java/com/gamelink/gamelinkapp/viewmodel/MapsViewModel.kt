import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.PostRepository
import kotlinx.coroutines.launch
import java.util.*


class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository = PostRepository(application.applicationContext)

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _postSave = MutableLiveData<ValidationModel>()
    val postSave: LiveData<ValidationModel> = _postSave

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
        var addressList = mutableListOf<Address>()
        if (Build.VERSION.SDK_INT < 33) {
            addressList = geocoder.getFromLocationName(addressString, 1)!!
        } else {
            geocoder.getFromLocationName(addressString, 1) { addresses ->
                addressList = addresses
            }
        }


        if (addressList.size != 0) {
            val location = addressList[0]
            _address.value = "${location.latitude}, ${location.longitude}"

            updateLocation(Location("").apply {
                latitude = location.latitude
                longitude = location.longitude

            })
        }
    }

    fun save(post: PostModel) {
        viewModelScope.launch {
            if (post.description.isEmpty()) {
                _postSave.value = ValidationModel("Campo descrição obrigatório")
            } else {
                postRepository.saveEvent(post, object : APIListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        _postSave.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _postSave.value = ValidationModel(message)
                    }

                })
            }
        }

    }
}


