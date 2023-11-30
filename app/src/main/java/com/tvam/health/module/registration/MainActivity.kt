package com.tvam.health.module.registration

import SubscriptionAdapter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tvam.health.R
import com.tvam.health.api.APIBuilder
import com.tvam.health.api.APIInterface
import com.tvam.health.models.response.DataSubscription
import com.tvam.health.models.response.SubscriptionsResponse
import com.tvam.health.module.healthtest.HealthTest
import com.tvam.health.module.healthtest.HealthTestData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.AppUtils
import utils.CustomProgressBar
import java.util.UUID


class MainActivity : AppCompatActivity() {

    var btnSearch: Button? = null
    var custmoblie: EditText? = null
    var smart_phone: EditText? = null
    var customer_age: EditText? = null
    var customer_name: EditText? = null
    var subscrtion_text: TextView? = null


    var male: RadioButton? = null
    var female: RadioButton? = null
    var other: RadioButton? = null

    var yesRadio: RadioButton? = null
    var noRadio: RadioButton? = null
    var ll_smart_phone: LinearLayout? = null
    var radio_group: RadioGroup? = null
    var radio1_group: RadioGroup? = null
    lateinit var isConfirmSmartMobile: RadioButton
    lateinit var customer_gender: RadioButton
    var smart_phone_vis: String = "true"
    var gender: String = ""
    var customerName = ""
    var subscriptionsType= ""
    var progressBar = CustomProgressBar()
    private var list: ArrayList<DataSubscription> = ArrayList()
    private var subscriptionAdapter: SubscriptionAdapter? = null
    private var spinner: Spinner? = null
    private var mSelectedPostion = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        btnSearch = findViewById(R.id.procced)
        custmoblie = findViewById(R.id.custome_moble)
        yesRadio = findViewById(R.id.yes)
        noRadio = findViewById(R.id.no)
        smart_phone = findViewById(R.id.smart_phone)
        customer_age = findViewById(R.id.customer_age)
        customer_name = findViewById(R.id.customer_name)
        ll_smart_phone = findViewById(R.id.ll_smart_phone)

        male = findViewById(R.id.male)
        female = findViewById(R.id.female)
        other = findViewById(R.id.other)
        radio_group = findViewById(R.id.radio_group)
        radio1_group = findViewById(R.id.radio1_group)
//        spinner = findViewById(R.id.spinnerDescription)
//        subscrtion_text = findViewById(R.id.subscrtion_text)


//        val intSelectButton: Int = radio_group!!.checkedRadioButtonId
//        isConfirmSmartMobile = findViewById(intSelectButton)
//        if (isConfirmSmartMobile.text.toString() == "Yes") {
//            smart_phone_vis = "true"
//        } else {
//            smart_phone_vis = "false"
//        }

        noRadio!!.setOnClickListener(View.OnClickListener {
            ll_smart_phone!!.visibility = View.VISIBLE
            smart_phone_vis = "false"
        })
        yesRadio!!.setOnClickListener(View.OnClickListener {
            ll_smart_phone!!.visibility = View.GONE
            smart_phone_vis = "true"
        })

        customerName = intent.getStringExtra("CustomerName").toString()
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.show(this@MainActivity, resources.getString(R.string.please_wait))
            subscrptionData()
        }
    //    val languages = resources.getStringArray(R.array.Languages)
//        val reasonListCustomAdapter = SubscriptionAdapter(
//            applicationContext,
//            R.layout.adapter_reason_list,
//            R.id.title_subscrption,
//            list
//        )
//        spinner?.setAdapter(reasonListCustomAdapter)



//        val adapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item, languages
//        )
     //   spinner!!.adapter = adapter
//        spinner!!.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//                Toast.makeText(
//                    this@MainActivity,
//                    getString(R.string.selected_item) + " " +
//                            "" + languages[position], Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }

//if (noRadio!!.isChecked){
//    ll_smart_phone!!.visibility=View.VISIBLE
//}else if(yesRadio!!.isChecked){
//    ll_smart_phone!!.visibility=View.GONE
//}else
//{
//    ll_smart_phone!!.visibility=View.GONE
//}

        // btnSearch.setOnClickListener()
        btnSearch!!.setOnClickListener(View.OnClickListener { view: View? ->
            validate()

        })


    }

    private fun validate() {
       var customername= customer_name!!.getText().toString().matches("/^[A-Z@~`!@#\$%^&*()_=+\\\\\\\\';:\\\"\\\\/?>.<,-]*\$/i".toRegex())

        if (radio_group!!.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select either one ", Toast.LENGTH_LONG).show();
        } else if (custmoblie!!.getText().toString().isEmpty()) {
                Toast.makeText(this, "please enter mobile number ", Toast.LENGTH_LONG).show()
            }else if (custmoblie!!.getText().toString().length !=10) {
                Toast.makeText(this, "please enter 10 digit mobile number", Toast.LENGTH_LONG)
                    .show()

            }else if ( smart_phone_vis=="false"&&smart_phone!!.getText().toString().length !=10){
                Toast.makeText(this, "please enter 10 digit mobile number", Toast.LENGTH_LONG)
                    .show()
            }else if (customer_name!!.getText().toString().isEmpty()) {
                Toast.makeText(this, "please enter customer name", Toast.LENGTH_LONG).show()

            }
            else if (customer_age!!.getText().toString().isEmpty()) {
                Toast.makeText(this, "please enter age", Toast.LENGTH_LONG).show()

            }  else if (customer_age!!.getText().toString()=="00") {
                Toast.makeText(this, "please enter valid age", Toast.LENGTH_LONG)
                    .show()

            }
//            else if(mSelectedPostion==0){
//                Toast.makeText(this, "please select Subscriptions Type", Toast.LENGTH_LONG)
//                    .show()
//            }
            else {
                val intSelectGenderButton: Int = radio1_group!!.checkedRadioButtonId
                customer_gender = findViewById(intSelectGenderButton)
                gender = customer_gender.text.toString()
                val guid = UUID.randomUUID().toString()
                val intent = Intent(this, HealthTest::class.java)
//                intent.putExtra("custmoblie", custmoblie!!.getText().toString())
                intent.putExtra("customer_age", customer_age!!.text.toString())
                intent.putExtra("customer_name", customer_name!!.text.toString())
//                intent.putExtra("smart_phone", smart_phone!!.getText().toString())
                intent.putExtra("uid", guid)
//                intent.putExtra("smart_phone_vis", smart_phone_vis)
                intent.putExtra("customerName", customerName)
//                intent.putExtra("SubscriptionsType",subscriptionsType)





                try{
                    startActivity(intent);
                } catch(e: ActivityNotFoundException) {
                    Toast.makeText(this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
    }


    private fun subscrptionData() {
        val apiBuilder = APIBuilder()
        val appUtils = AppUtils()
       // var date = appUtils.getCurrentDateTime()
        val service = apiBuilder.retrofit3.create(APIInterface::class.java)


        val call = service.getSubscriptions()
        call.enqueue(object : Callback<SubscriptionsResponse> {
            override fun onResponse(
                call: Call<SubscriptionsResponse>,
                response: Response<SubscriptionsResponse>
            ) {
                if (response.code() == 200) {
                    var subscription = response.body()?.data
                    if (subscription != null) {

                        var list1 = DataSubscription(0,"Subscription Type","","","")

                       // list.addAll("")
                        subscription.add(0,list1)
                        list.addAll(subscription)
                       // Log.d("reasonListCustomAdapter", list.toString())
                        val reasonListCustomAdapter = SubscriptionAdapter(
                            applicationContext,
                            list
                        )
                        spinner?.setAdapter(reasonListCustomAdapter)
                        spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                mSelectedPostion = position
                               var subscription=  list.get(position).Subscription
                                var subscriptionsType1 = list.get(position).SubscriptionType
                                subscrtion_text?.text=subscription
                                if (subscriptionsType1 != null) {
                                    subscriptionsType=subscriptionsType1
                                }

                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        })

                    }


                }else{

                }
                progressBar.dialog?.dismiss()

            }

            override fun onFailure(call: Call<SubscriptionsResponse>, t: Throwable) {
                Log.d("onFailure", t.toString())
                progressBar.dialog?.dismiss()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }

}




