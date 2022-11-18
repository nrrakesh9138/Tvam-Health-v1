package com.example.tvamhealth.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.R
import android.os.Build
import android.widget.Toast
import com.example.tvamhealth.BuildConfig

import okhttp3.*
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class APIBuilder() {
   // val appUtils  =AppUtils()
    //var context:Context= context!!

    private var baseUrl ="https://healthappmicroservice.azurewebsites.net";




   var retrofit3: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(getOkhttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()





    private fun getOkhttpClient(): OkHttpClient? {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
           // .certificatePinner(pinner)
            // .retryOnConnectionFailure(true)
          //  .addInterceptor(getInterceptor(access_token,context)!!)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }








    /*fun readKeyStore(context: Context): KeyStore {
        var ks: KeyStore? = null
        var `in`: InputStream? = null
        try {
            ks = KeyStore.getInstance("PKCS12")
            //ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // get user password and file input stream
            val password = "XXXX".toCharArray()
            `in` = context.resources.openRawResource(R.raw.xxxxxx)
            ks!!.load(`in`, password)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return ks!!
    }*/
    private fun getOkhttpClientNew(): OkHttpClient? {
//        var pinner = CertificatePinner.Builder().add(BuildConfig.DomainName,BuildConfig.PIN_KEY).build()
       // val access_token = appUtils.getSharedPreferance(context, Constants.ACCESS_TOKEN)
        //   val access_token = preferences.getString(Constants.ACCESS_TOKEN, "")
        val httpLoggingInterceptor = HttpLoggingInterceptor()

//        if (BuildConfig.DEBUG) {
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        }
////        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            }
        )
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        return OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
//            .certificatePinner(pinner)
            // .retryOnConnectionFailure(true)
           // .addInterceptor(getInterceptor(access_token, context)!!)
             .addInterceptor(httpLoggingInterceptor)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .build()
    }




}
