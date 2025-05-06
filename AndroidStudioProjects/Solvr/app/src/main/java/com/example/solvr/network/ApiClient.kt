import com.example.solvr.network.AuthService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object ApiClient {
    private const val BASE_URL = "http://34.170.91.53/be/api/v1/"
    private const val TAG = "API_LOGGER"

    val instance: AuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
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

    private fun logRequest(method: Method, args: Array<Any>?) {
        println("$TAG - REQUEST: ${method.name}")
        args?.forEachIndexed { i, arg ->
            println("$TAG - Arg $i: ${arg?.toString()}")
        }
    }

    private fun logResponse(method: Method, response: Any?) {
        println("$TAG - RESPONSE: ${method.name}")
        println()
        when (response) {
            is Call<*> -> {
                println("$TAG - Call created for ${response.request().url}")
            }
            else -> {
                println("$TAG - Response: ${response?.toString()}")
            }
        }
    }
}
