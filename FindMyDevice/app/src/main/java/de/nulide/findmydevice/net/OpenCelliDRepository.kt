package de.nulide.findmydevice.net

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import de.nulide.findmydevice.utils.CellParameters
import de.nulide.findmydevice.utils.PatchedVolley
import de.nulide.findmydevice.utils.SingletonHolder
import de.nulide.findmydevice.utils.log


// See the API docs: https://wiki.opencellid.org/wiki/API
class OpenCelliDRepository private constructor(private val spec: OpenCelliDSpec) {

    companion object :
        SingletonHolder<OpenCelliDRepository, OpenCelliDSpec>(::OpenCelliDRepository) {
        val TAG = OpenCelliDRepository::class.simpleName
    }

    private val context = spec.context
    private val requestQueue: RequestQueue = PatchedVolley.newRequestQueue(spec.context)

    fun getCellLocation(
        paras: CellParameters,
        apiAccessToken: String,
        onSuccess: (OpenCelliDSuccess) -> Unit,
        onError: (OpenCelliDError) -> Unit,
    ) {
        val url =
            "https://opencellid.org/cell/get?key=$apiAccessToken&mcc=${paras.mcc}&mnc=${paras.mnc}&lac=${paras.lac}&cellid=${paras.cid}&radio=${paras.radio.uppercase()}&format=json"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                if (response.has("lat") && response.has("lon")) {
                    val lat = response.getString("lat")
                    val lon = response.getString("lon")
                    onSuccess(OpenCelliDSuccess(lat, lon, url))
                } else {
                    val message = if (response.has("error")) {
                        response.getString("error")
                    } else "Missing lat or lon in response"

                    context.log()
                        .w(TAG, "OpenCelliD API call failed: $message\n${paras.prettyPrint()}")
                    onError(OpenCelliDError(message, url))
                }
            },
            { error ->
                context.log().w(TAG, "Request failed: ${error.message}")
                onError(
                    OpenCelliDError(error.message ?: "", url)
                )
            },
        )
        requestQueue.add(request)
    }
}

class OpenCelliDSpec(val context: Context)

data class OpenCelliDSuccess(val lat: String, val lon: String, val url: String)
data class OpenCelliDError(val error: String, val url: String)
