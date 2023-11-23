package com.tvam.health.module.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tvam.health.module.registration.MainActivity
import com.tvam.health.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tvam.health.api.APIBuilder
import com.tvam.health.api.APIInterface
import com.tvam.health.models.request.UserLoginRequest
import com.tvam.health.models.response.UserLoginResponse
import utils.AppUtils
import utils.CustomProgressBar

class LoginActivity : AppCompatActivity() {
    var loginButton :Button?=null
    var userName:EditText?=null
    var userPassword:EditText?=null
    var progressBar = CustomProgressBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton = findViewById(R.id.user_login_bt)
        userName = findViewById(R.id.user_name)
        userPassword = findViewById(R.id.user_password)

        loginButton!!.setOnClickListener(View.OnClickListener {
            if (userName!!.text.toString().isNullOrEmpty()||userName!!.text.toString().length!=10){
                Toast.makeText(this,"please enter 10 digit mobile number",Toast.LENGTH_LONG).show()

            }else if (userPassword!!.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show()

            }
            else{
                GlobalScope.launch(Dispatchers.Main) {
                    progressBar.show(this@LoginActivity,resources.getString(R.string.please_wait))
                    userLogin()
                }
            }


        })

    }

    private fun userLogin() {
        val apiBuilder = APIBuilder()
        val service = apiBuilder.retrofit3.create(APIInterface::class.java)
        val requestor =
            UserLoginRequest(
             "+91"+userName!!.text.toString(),userPassword!!.text.toString()
            )
//            val requestor =
//            UserLoginRequest(
//             "+918431040996","rcds"
//            )

        val call = service.userLogin(requestor)
        call.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.success == true) {
                        Log.d("response_", response.body().toString())

                        if (response.body() != null ) {

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)

                            intent.putExtra("CustomerName",response.body()?.customerName)
                            val appUtils = AppUtils()
                            appUtils.setSharedPreference(applicationContext, "CustomerName",response.body()?.customerName)
//                            intent.putExtra("hindiData",response.body()?.hindData)
                            startActivity(intent);


                        } else {
                            // dismissDialog()
                            progressBar.dialog?.dismiss()

                        }
                       // Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
//                            setTextViewValues(response.body()?.data!!)
                    } else {
                        progressBar.dialog?.dismiss()
//                        dismissDialog()
                        Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_LONG).show()
//                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
                progressBar.dialog?.dismiss()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.d("onFailure",t.toString())
                progressBar.dialog?.dismiss()
            }

        })
    }
    override fun onBackPressed() {
        super.onBackPressed()
        progressBar.dialog?.dismiss()
        finishAffinity()

    }

    override fun onDestroy() {
        super.onDestroy()
        progressBar.dialog?.dismiss()
    }
}