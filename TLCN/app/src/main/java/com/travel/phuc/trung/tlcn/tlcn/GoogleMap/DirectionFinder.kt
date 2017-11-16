package docongphuc.pttravle.Maps

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class DirectionFinder(listener: MapsActivity, origin: String, destination: String) {
    private val DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?"
    private val GOOGLE_API_KEY = "AIzaSyCvDyqpAG0Sq-A7GNkKxfWw85jaE2hQ2Yo"
    private var listener: DirectionFinderListener? = listener
    private var origin: String? = origin
    private var destination: String? = destination
    @Throws(UnsupportedEncodingException::class)
    fun execute() {
        listener!!.onDirectionFinderStart()
       DownloadRawData().execute(createUrl())
    }
    @Throws(IOException::class)
    private fun createUrl(): String? {
        val urlOrigin = URLEncoder.encode(origin, "utf-8")
        val urlDestination = URLEncoder.encode(destination, "utf-8")

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY
    }

    @SuppressLint("StaticFieldLeak")
    inner class DownloadRawData: AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val content: StringBuffer = StringBuffer()
            val url:URL= URL(p0[0])
            val urlConection:HttpURLConnection=url.openConnection()as HttpURLConnection
            val inputStreamReader:InputStreamReader=InputStreamReader(urlConection.inputStream)
            val bufferReader:BufferedReader= BufferedReader(inputStreamReader as Reader?)
            var line:String=""
            try {
                do {
                    line=bufferReader.readLine()
                    if (line!=null){
                        content.append(line)
                    }

                }while (line!=null)
                bufferReader.close()

            }catch (e:Exception){
                Log.d("AAA",e.toString())
            }

            return content.toString()
        }

       @Throws(IOException::class)
       override fun onPostExecute(data: String?) {
           super.onPostExecute(data)
           if (data == null)
               return;
           val routes = ArrayList<Route>()
           val jsonData = JSONObject(data)
           val jsonRoutes = jsonData.getJSONArray("routes")
           for (i in 0 until jsonRoutes.length()) {
               val jsonRoute = jsonRoutes.getJSONObject(i)
               val route = Route()

               val overview_polylineJson = jsonRoute.getJSONObject("overview_polyline")
               val jsonLegs = jsonRoute.getJSONArray("legs")
               val jsonLeg = jsonLegs.getJSONObject(0)
               val jsonDistance = jsonLeg.getJSONObject("distance")
               val jsonDuration = jsonLeg.getJSONObject("duration")
               val jsonEndLocation = jsonLeg.getJSONObject("end_location")
               val jsonStartLocation = jsonLeg.getJSONObject("start_location")

               route.distance = Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"))
               route.duration = Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"))
               route.endAddress = jsonLeg.getString("end_address")
               route.startAddress = jsonLeg.getString("start_address")
               route.startLocation = LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"))
               route.endLocation = LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"))
               route.points = decodePolyLine(overview_polylineJson.getString("points"))

               routes.add(route)
           }

           listener?.onDirectionFinderSuccess(routes)
       }

   }

    private fun decodePolyLine(encoded: String?): List<LatLng>? {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded!!.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }



}