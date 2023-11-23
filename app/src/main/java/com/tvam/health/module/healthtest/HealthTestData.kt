package com.tvam.health.module.healthtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tvam.health.R
import com.tvam.health.api.APIBuilder
import com.tvam.health.api.APIInterface
import com.tvam.health.models.request.PatientInformationDetailsRequest
import com.tvam.health.models.response.PatientInformationDetailsrResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.AppUtils
import utils.CustomProgressBar

class HealthTestData : AppCompatActivity() {
    var custmoblie = ""
    var customer_age = ""
    var customer_name = ""
    var smart_phone = ""
    var gender = ""
    var Weight: EditText? = null
    var temperature: EditText? = null
    var blood_pressure: EditText? = null
    var blood_pressure1: EditText? = null
    var pulse_rate: EditText? = null
    var hemoglobin: EditText? = null
    var fundal_height: EditText? = null
    private var fetal_heart_rate: EditText? = null
    var random_blood_sugar: EditText? = null
    var radio_group_uri_pro: RadioGroup? = null
    lateinit var uri_pro_button: RadioButton
    lateinit var uri_suger_button: RadioButton
    var radio_group_uri_suger: RadioGroup? = null
    var urine_protein_test = ""
    var urine_suger_test: String = ""
    var isSmartPhone: String = ""
    var current = ""
    var progressBar = CustomProgressBar()
    var customerName = ""
    var subscriptionsType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_test_data)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val savePrint = findViewById(R.id.save_print) as Button
        Weight = findViewById(R.id.Weight) as EditText
        temperature = findViewById(R.id.temperature) as EditText
        blood_pressure = findViewById(R.id.blood_pressure) as EditText
        blood_pressure1 = findViewById(R.id.blood_pressure1) as EditText

        pulse_rate = findViewById(R.id.pulse_rate) as EditText
        hemoglobin = findViewById(R.id.hemoglobin) as EditText
        fundal_height = findViewById(R.id.fundal_height) as EditText
        fetal_heart_rate = findViewById(R.id.fetal_heart_rate) as EditText
        random_blood_sugar = findViewById(R.id.random_blood_sugar) as EditText
        //  var fundal_height = findViewById(R.id.fundal_height) as EditText

        val appUtils = AppUtils()
        custmoblie = intent.getStringExtra("custmoblie").toString()
        customer_age = intent.getStringExtra("customer_age").toString()
        customer_name = intent.getStringExtra("customer_name").toString()
        smart_phone = intent.getStringExtra("smart_phone").toString()
        gender = intent.getStringExtra("gender").toString()
        isSmartPhone = intent.getStringExtra("smart_phone_vis").toString()
//        subscriptionsType = intent.getStringExtra("SubscriptionsType").toString()
        customerName = appUtils.getSharedPreference(applicationContext, "CustomerName").toString()
        radio_group_uri_pro = findViewById(R.id.radio_group_uri_pro)
        radio_group_uri_suger = findViewById(R.id.radio_group_uri_suger)







        savePrint.setOnClickListener {
            if (isValidMail()) {
                val intSelectButton: Int = radio_group_uri_pro!!.checkedRadioButtonId
                uri_pro_button = findViewById(intSelectButton)
                urine_protein_test = uri_pro_button.text as String

                val intSelectButton1: Int = radio_group_uri_suger!!.checkedRadioButtonId
                uri_suger_button = findViewById(intSelectButton1)
                urine_suger_test = uri_suger_button.text as String
                GlobalScope.launch(Dispatchers.Main) {
                    progressBar.show(this@HealthTestData, resources.getString(R.string.please_wait))
                    printData()
                }
            }


        }

    }

    fun isValidMail(): Boolean {
    //    var fetal_heart_rate = fetal_heart_rate!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
        var Weight1 = 1000
//       // var temperature = temperature!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
//        var pulse_rate = pulse_rate!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
//        var hemoglobin = hemoglobin!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
//        var fundal_height = fundal_height!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
//        var random_blood_sugar =
//            random_blood_sugar!!.text.toString().matches("\\d+(\\.\\d+)".toRegex())
        if( Weight!!.text.toString().isNullOrEmpty()|| Weight!!.text.toString().toDouble()>=Weight1.toDouble()){
            Toast.makeText(this, "please enter valid Weight ", Toast.LENGTH_LONG).show()
            return false
        }

        else if (temperature!!.text.toString().isNullOrEmpty()||temperature!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid temperature ", Toast.LENGTH_LONG).show()
            return false
        } else if (pulse_rate!!.text.toString().isNullOrEmpty()||pulse_rate!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid pulse rate ", Toast.LENGTH_LONG).show()
            return false
        } else if (hemoglobin!!.text.toString().isNullOrEmpty()||hemoglobin!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid hemoglobin ", Toast.LENGTH_LONG).show()
            return false
        } else if (fundal_height!!.text.toString().isNullOrEmpty()||fundal_height!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid fundal height ", Toast.LENGTH_LONG).show()
            return false
        } else if (fetal_heart_rate!!.text.toString().isNullOrEmpty()||fetal_heart_rate!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid fetal heart rate ", Toast.LENGTH_LONG).show()
            return false
        } else if (random_blood_sugar!!.text.toString().isNullOrEmpty()||random_blood_sugar!!.text.toString().toDouble()>=Weight1.toDouble()) {
            Toast.makeText(this, "please enter valid random blood sugar ", Toast.LENGTH_LONG).show()
            return false
        } else {
            return true
        }

    }

    private fun printData() {
        val apiBuilder = APIBuilder()
        val appUtils = AppUtils()
        var date = appUtils.getCurrentDateTime()
        val service = apiBuilder.retrofit3.create(APIInterface::class.java)
        val requestor =
            PatientInformationDetailsRequest(
                customer_name,
                custmoblie,
                isSmartPhone,
                smart_phone,
                customer_age,
                gender,
                date,
                Weight!!.getText().toString(),
                temperature!!.getText().toString(),
                blood_pressure!!.getText().toString(),
                blood_pressure1!!.getText().toString(),
                customerName,
                pulse_rate!!.getText().toString(),
                hemoglobin!!.getText().toString(),
                fundal_height!!.getText().toString(),
                fetal_heart_rate!!.getText().toString(),
                random_blood_sugar!!.getText().toString(),
                urine_protein_test,
                urine_suger_test,
                subscriptionsType
            )

        val call = service.patientInformation(requestor)
        call.enqueue(object : Callback<PatientInformationDetailsrResponse> {
            override fun onResponse(
                call: Call<PatientInformationDetailsrResponse>,
                response: Response<PatientInformationDetailsrResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.success == true) {
                        progressBar.dialog?.dismiss()
                        Log.d("response_", response.body().toString())

                        if (response.body() != null && response.body()?.engData != null && response.body()?.hindData != null) {
                            // callIvrApi(response.body()?.data!!)
                            val intent = Intent(this@HealthTestData, SuccessActivity::class.java)

                            intent.putExtra("engData", response.body()?.engData)
                            intent.putExtra("hindiData", response.body()?.hindData)
                            startActivity(intent);


                        } else {
                            progressBar.dialog?.dismiss()
                            // dismissDialog()
                        }
//                            setTextViewValues(response.body()?.data!!)
                    } else {
                        progressBar.dialog?.dismiss()
//                        dismissDialog()
//                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT)
//                            .show()
                    }
                }
                progressBar.dialog?.dismiss()
            }

            override fun onFailure(call: Call<PatientInformationDetailsrResponse>, t: Throwable) {
                Log.d("onFailure", t.toString())
                progressBar.dialog?.dismiss()
            }

        })
    }

    //
    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
        progressBar.dialog?.dismiss()

    }

    override fun onDestroy() {
        super.onDestroy()
        progressBar.dialog?.dismiss()

    }


//    fun dismissDialog() {
//        if (MainActivity.thi == null) {
//            return
//        }
//        val window = activity?.getWindow() ?: return
//        val decor = window!!.getDecorView()
//        if (decor != null && decor!!.getParent() != null) {
//            progressBar.dialog?.dismiss()
//        }
//    }
}