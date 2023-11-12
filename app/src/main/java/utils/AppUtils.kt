package utils


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Preconditions.checkArgument
import androidx.work.WorkInfo
import androidx.work.WorkManager


import com.google.common.util.concurrent.ListenableFuture
//import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.*


class AppUtils {

    fun createUUID(): String {
        val uuid: UUID = UUID.randomUUID()
        return uuid.toString()
    }

    fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy",Locale("English"))
        val date = Date()
        return formatter.format(date)
    }

//    fun setSharedPrefrance(context: Context?, PREF_NAME: String?, value: String?) {
//        try {
//            val sharedPref: SharedPreferences? =
//                context?.getSharedPreferences(PREF_NAME, Constants.PRIVATE_MODE)
//            val editor1 = sharedPref?.edit()
//            /* editor?.putString(PREF_NAME, value)
//                  editor?.apply()*/
//            var value1 = sharedPref?.getString(PREF_NAME,"")
//            if(!value1.isNullOrEmpty()){
//                editor1?.remove(PREF_NAME)?.apply()
//            }
//        } catch (e: Exception) {
//        }
//
//        try {
//            val masterKey = MasterKey.Builder(context!!, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(
//                MasterKey.KeyScheme.AES256_GCM
//            ).build()
//            val sharedPreferences = EncryptedSharedPreferences
//                .create(
//                    context!!,
//                    Constants.PREF_FILE_NAME,
//                    masterKey,
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//                ) as EncryptedSharedPreferences
//            val editor = sharedPreferences.edit()
//            editor?.putString(PREF_NAME, value)
//            editor?.apply()
//        } catch (e: Exception) {
//        }
//    }

//    fun getSharedPreferance(context: Context?, PREF_NAME: String): String {
//        /*val sharedPref: SharedPreferences? =
//            context?.getSharedPreferences(PREF_NAME, Constants.PRIVATE_MODE)
//        return sharedPref?.getString(PREF_NAME, "")!!*/
//        try {
//            val masterKey = MasterKey.Builder(context!!, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(
//                MasterKey.KeyScheme.AES256_GCM
//            ).build()
//            val sharedPref = EncryptedSharedPreferences
//                .create(
//                    context!!,
//                    Constants.PREF_FILE_NAME,
//                    masterKey,
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//                ) as EncryptedSharedPreferences
//
//            return sharedPref?.getString(PREF_NAME, "")!!
//        } catch (e: Exception) {
//            var emptyString=""
//            return emptyString
//        }
//    }

    private fun getPreferences(context: Context?, prefName: String?): SharedPreferences? {
        var lSharedPreferences: SharedPreferences? = null

        try {
            if (Constants.sharedPreferences == null) {
                lSharedPreferences =
                    context?.getSharedPreferences(Constants.TVAM_SHARED_PREFRENCE,
                        Context.MODE_PRIVATE
                    )!!
                Constants.sharedPreferences = lSharedPreferences
            } else {
                lSharedPreferences = Constants.sharedPreferences
            }
        } catch (e: Exception) {
            handleException(e.toString(), null)
        }
        return lSharedPreferences
    }
    fun handleException(error: String, t: Throwable?) {
        // Log.e(TAG, error)
    }

fun setSharedPreference(context: Context?, PREF_NAME: String?, value: String?) {
    val sharedPref: SharedPreferences? = getPreferences(context, PREF_NAME)
    val editor = sharedPref?.edit()
    editor?.putString(PREF_NAME, value)
    editor?.apply()
}

    fun getSharedPreference(context: Context?, PREF_NAME: String): String? {
        val sharedPref: SharedPreferences? = getPreferences(context, PREF_NAME)

        return sharedPref!!.getString(PREF_NAME, "")
    }
    fun showMessageOKCancel(
        mActivity: AppCompatActivity?,
        message: String?,
        okListener: DialogInterface.OnClickListener?
    ) {
        val alertDialogBuilder =
            AlertDialog.Builder(mActivity)
        alertDialogBuilder.setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            .create()
            .show()
    }

    fun getDateTimefromString(fromTimeDate: String?, toTimeDate: String?): String? {


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = sdf.parse(fromTimeDate)
        val date1 = sdf.parse(toTimeDate)


        return getDate(date, date1)

    }


    fun getHours(date: Date): String {
        val formatter3 = SimpleDateFormat("hh")
        val date3 = formatter3.format(date)
        return date3.toString()
    }

    private fun getDate(fromDate: Date, toDate: Date): String? {
        val formatter1 = SimpleDateFormat("hh")
        val formatter11 = SimpleDateFormat("a")
        val formatter = SimpleDateFormat("dd")
        val day = formatter.format(fromDate)
        val hour1 =
            formatter1.format(fromDate) + ":00 " + formatter11.format(fromDate).toLowerCase()

        val formatter2 = SimpleDateFormat("hh")
        val formatter21 = SimpleDateFormat("a")
        val hour2 = formatter2.format(toDate) + ":00 " + formatter21.format(toDate).toLowerCase()

        val formatter3 = SimpleDateFormat("MMM yyyy")
        val date3 = formatter3.format(toDate)

        return hour1 + " to " + hour2 + " , " + getDayOfMonthSuffix(day) + " " + date3
    }


    private fun getDayOfMonthSuffix(day: String): String? {
        val n: Int = day.toInt()
        checkArgument(n in 1..31, "illegal day of month: $n")
        return day + if (n in 11..13) {
            "th"
        } else when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }


//    fun isConnectedToNetwork(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        var isConnected = false
//        if (connectivityManager != null) {
//            val activeNetwork = connectivityManager.activeNetworkInfo
//            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
//        }
//
//        return isConnected
//    }

//    fun isTokenExpired(context: Context?): Boolean {
//        if (context == null) {
//            return true
//        }
//        val expiryTime = getSharedPreferance(context, Constants.EXPIRY_TIME)
//        return if (TextUtils.isEmpty(expiryTime)) {
//            true
//        } else {
//            try {
//                val sdf = SimpleDateFormat("yyyy:MM:dd:HH:mm:ss")
//                val currentDateandTime = sdf.format(Date())
//                val date = sdf.parse(currentDateandTime)
//                val tokenExpireTime = sdf.parse(expiryTime)
//                !tokenExpireTime.after(date)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                true
//            }
//
//        }
//    }

//    fun forceLogout(context: Context?) {
//        try {
//            if (context == null) {
//                return
//            }
//            val activity = context as Activity? ?: return
//            val alertDialogBuilder = AlertDialog.Builder(context)
//            alertDialogBuilder.setTitle(context.getString(R.string.app_name))
//                .setMessage("Please Log in again to continue!")
//            alertDialogBuilder.setPositiveButton(
//                context.getString(R.string.ok)
//            ) { dialogInterface, i ->
//                setSharedPrefrance(context, Constants.loginKey,Constants.logout)
//               setSharedPrefrance(
//                   context,
//                    Constants.ACCESS_TOKEN,
//                    ""
//                )
//
//                setSharedPrefrance(
//                    context,
//                    Constants.REFRESH_TOKEN,
//                    ""
//                )
//
//                setSharedPrefrance(
//                    context,
//                    Constants.EXPIRY_TIME,
//                    ""
//                )
//                val intent = Intent(context, LoginActivity::class.java)
//                context.startActivity(intent)
//                if (isWorkScheduled(Constants.LOCATION_WORK_TAG)) {
//                    WorkManager.getInstance().cancelAllWorkByTag(Constants.LOCATION_WORK_TAG)
//                }
//                activity.finish()
//            }
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.setCancelable(false)
//            alertDialog.setCanceledOnTouchOutside(false)
//            alertDialog.show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

//    fun isInternetAvailable(context: Context): Boolean {
//        var result = false
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val networkCapabilities = connectivityManager.activeNetwork ?: return false
//            val actNw =
//                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
//            result = when {
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        } else {
//            val networkInfo = connectivityManager.activeNetworkInfo
//            return networkInfo != null && networkInfo.isConnected
//        }
//
//        return result
//    }

//fun replaceOldPref(context: Context?, PREF_NAME: String){
//    try {
//        if (context == null) {
//            return
//        }
//        val sharedPref: SharedPreferences? =
//            context?.getSharedPreferences(PREF_NAME, Constants.PRIVATE_MODE)
//        var oldvalue = sharedPref?.getString(PREF_NAME, "")!!
//        var encryptedValue = getSharedPreferance(context, PREF_NAME)
//        if (!oldvalue.isNullOrEmpty() && encryptedValue.isNullOrEmpty()) {
//            setSharedPrefrance(context, PREF_NAME, oldvalue)
//        }
//    }catch (e: Exception) {
//    }
//}

//    fun handleResponse(
//        context: Context,
//        code: Int,
//        error: String,
//        snackbarRequired: Boolean,
//        view: View
//    ) {
//        try {
//            var error_message = ""
//            when (code) {
//                400 -> {
//                    error_message = context?.getString(R.string.bad_request)
//                }
//                401 -> {
//                    error_message = context?.getString(R.string.authentication_failure)
//                }
//                403 -> {
//                    error_message = context?.getString(R.string.forbidden)
//                }
//                404 -> {
//                    error_message = context?.getString(R.string.not_found)
//                }
//                500,501, 502, 503 -> {
//                    error_message = context?.getString(R.string.server_error_text)
//                }
//                else -> {
//                    error_message = context?.getString(R.string.something_went_wrong)
//                }
//
//            }
//            if (!snackbarRequired || view == null) {
//                if (context != null && !TextUtils.isEmpty(error_message)) {
//                    Toast.makeText(context!!, error_message, Toast.LENGTH_SHORT).show()
//                }
//
//            } else {
//                if(code == 401 && !TextUtils.isEmpty(error_message)){
//                    showAutoSnackBar(error_message, view,context)
//                }else if (!TextUtils.isEmpty(error_message)) {
//                    showSnackBar(error_message, view,context)
//                }
//            }
//        } catch (e: Exception) {
//        }
//    }

//    fun handleFailure(
//        context: Context,
//        throwable: Throwable,
//        snackbarRequired: Boolean,
//        view: View
//    ) {
//        try {
//            var error_message = ""
//            var local_message = throwable.localizedMessage
//            when (throwable) {
//                is SocketTimeoutException -> {
//                    error_message = context?.getString(R.string.connection_time_out)
//                }
//
//                is IOException -> {
//                    error_message = context?.getString(R.string.connection_error)
//                }
//
//                else -> {
//                    error_message = context?.getString(R.string.some_error_occured)
//                }
//            }
//            if (!snackbarRequired || view == null) {
//                if (context != null && !TextUtils.isEmpty(error_message)) {
//                    Toast.makeText(context!!, error_message, Toast.LENGTH_SHORT).show()
//                }
//
//            } else {
//                if (!TextUtils.isEmpty(error_message)) {
//                    showSnackBar(error_message, view,context)
//                }
//            }
//        } catch (e: Exception) {
//        }
//
//    }

//    fun showSnackBar(mesaage: String, view: View, context:Context) {
//        if(context == null){
//            return
//        }
//        try {
//            val snackBar = Snackbar.make(
//                view, mesaage,
//                Snackbar.LENGTH_INDEFINITE
//            )
//            snackBar.setAction("Ok", View.OnClickListener { snackBar.dismiss() })
//            snackBar.setActionTextColor(Color.WHITE)
//            val snackBarView = snackBar.view
//            snackBarView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null))
//            val textView =
//                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//            textView.setTextColor(Color.WHITE)
//            snackBar.show()
//        } catch (e: Exception) {
//        }
//    }
//    fun showAutoSnackBar(mesaage: String, view: View, context:Context) {
//        if(context == null){
//            return
//        }
//        try {
//            val snackBar = Snackbar.make(
//                view, mesaage,
//                Snackbar.LENGTH_SHORT
//            )
//            snackBar.setActionTextColor(Color.WHITE)
//            val snackBarView = snackBar.view
//            snackBarView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null))
//            val textView =
//                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
//            textView.setTextColor(Color.WHITE)
//            snackBar.show()
//        } catch (e: Exception) {
//        }
//    }

    fun getTimefromString(timeDate: String?): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")
            val date = sdf.parse(timeDate)
            val formatter1 = SimpleDateFormat("hh:mm")
            val formatter11 = SimpleDateFormat("a")
            val formatter = SimpleDateFormat("dd")
            val day = formatter.format(date)
            val hour1 =
                formatter1.format(date) + " " + formatter11.format(date).toLowerCase()
            val formatter3 = SimpleDateFormat("MMM yyyy")
            val date3 = formatter3.format(date)

            return hour1 + " , " + getDayOfMonthSuffix(day) + " " + date3
        } catch (e: Exception) {
            return timeDate
        }


    }

     fun isWorkScheduled(tag: String): Boolean {
        val instance = WorkManager.getInstance()
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            running
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
