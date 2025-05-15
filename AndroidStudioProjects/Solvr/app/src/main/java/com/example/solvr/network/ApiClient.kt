import android.content.Context
import android.util.Log
import com.example.solvr.network.AuthInterceptor
import com.example.solvr.network.AuthService
import com.example.solvr.network.PlafonService
import com.example.solvr.network.UserService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object ApiClient {
    private const val BASE_URL = "http://34.45.191.98/be/api/v1/"
//    private const val BASE_URL = "https://3b08-114-125-93-58.ngrok-free.app/api/v1/"
    private const val TAG = "API_LOGGER"

    private lateinit var okHttpClient: OkHttpClient

    fun init(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }


    val authService: AuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create a proxy for logging
        retrofit.create(AuthService::class.java).let { service ->
            Proxy.newProxyInstance(
                service.javaClass.classLoader,
                arrayOf(AuthService::class.java)
            ) { _, method, args ->
                logRequest(method, args)
                val response = method.invoke(service, *(args ?: arrayOfNulls<Any>(0)))
                logResponse(method, response)
                response
            } as AuthService
        }
    }

    val plafonService: PlafonService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create a proxy for logging
        retrofit.create(PlafonService::class.java).let { service ->
            Proxy.newProxyInstance(
                service.javaClass.classLoader,
                arrayOf(PlafonService::class.java)
            ) { _, method, args ->
                logRequest(method, args)
                val response = method.invoke(service, *(args ?: arrayOfNulls<Any>(0)))
                logResponse(method, response)
                response
            } as PlafonService
        }
    }

    val userService: UserService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create a proxy for logging
        retrofit.create(UserService::class.java).let { service ->
            Proxy.newProxyInstance(
                service.javaClass.classLoader,
                arrayOf(UserService::class.java)
            ) { _, method, args ->
                logRequest(method, args)
                val response = method.invoke(service, *(args ?: arrayOfNulls<Any>(0)))
                logResponse(method, response)
                response
            } as UserService
        }
    }

    private fun logRequest(method: Method, args: Array<Any>?) {
        Log.d(TAG, "===== REQUEST: ${method.name} =====")
        args?.forEachIndexed { index, arg ->
            Log.d(TAG, "Arg $index: ${arg.toString()}")
            // Tambahkan log body jika Call<>
            if (arg is Call<*>) {
                val request = arg.request()
                Log.d(TAG, "URL: ${request.url}")
                Log.d(TAG, "Method: ${request.method}")
                Log.d(TAG, "Headers: ${request.headers}")
                val body = request.body
                Log.d(TAG, "Body: $body")
            }
        }
    }

    private fun logResponse(method: Method, response: Any?) {
        Log.d(TAG, "===== RESPONSE: ${method.name} =====")
        when (response) {
            is Call<*> -> {
                val request = response.request()
                Log.d(TAG, "Response Call URL: ${request.url}")
                Log.d(TAG, "Method: ${request.method}")
            }
            else -> {
                Log.d(TAG, "Response: ${response?.toString()}")
            }
        }
    }

}
