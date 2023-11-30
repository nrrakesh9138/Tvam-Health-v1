package com.tvam.health.module.healthtest

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tvam.health.R
import utils.AppUtils

class HealthTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_test)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val appUtils = AppUtils()
        var btnFetosense = findViewById<Button>(R.id.btnFetosense)
        val uid = intent.getStringExtra("uid").toString()
        val customer_age = intent.getStringExtra("customer_age").toString()
        val customer_name = intent.getStringExtra("customer_name").toString()
        val customerName = appUtils.getSharedPreference(applicationContext, "CustomerName").toString()

        btnFetosense.setOnClickListener{
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setPackage("com.carenx.fetosense.device.ctg");
            intent.putExtra("uId","TestTVAM");
            intent.putExtra("name","Test TVAM");
            intent.putExtra("age",30);
            intent.putExtra("lmpDate",1679587477);
            intent.putExtra("eddDate",1703347477);
            try{
                startActivity(intent);
            } catch(e: ActivityNotFoundException) {
                Toast.makeText(this, "There is no package available in android", Toast.LENGTH_LONG).show();
            }
        }
    }
}