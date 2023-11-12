package com.tvam.health

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.leopard.api.Printer
import com.leopard.api.Setup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.CustomProgressBar
import java.io.*
import java.lang.String.valueOf
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and


class SuccessActivity : AppCompatActivity() {
    // var myLabel: TextView? = null

    // will enable user to enter any text to be printed
    var myTextbox: EditText? = null


    // android built in classes for bluetooth operations
    var mBluetoothAdapter: BluetoothAdapter? = null
    var msg: Message? = Message()

    val UUID_STR = "00001101-0000-1000-8000-00805F9B34FB"
    var mosOut: OutputStream? = null
    var umbsSocket: BluetoothSocket? = null
    var misIn: InputStream? = null

    var setup: Setup? = null
    var SQUIRREL = false
    var printformatone: Vector<String>? = null
    var tatvikDeviceName = ""
    var tatvikDeviceAddress = ""
    var TatvikDevice = false
    var TatvikPrinter = false
    var MPTPrinter = false
    var printer: com.leopard.api.Printer? = null

    var sSETIMGFILENAME = ""
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 123
    var sTxnType = ""
    private val BYTE_PER_PIXEL = 3
    private val BMP_WIDTH_OF_TIMES = 4
    var engData = ""
    var en: RadioButton? = null
    var hi: RadioButton? = null
    var radio_group_languge: RadioGroup? = null
    var openButton: Button? = null
    var go_home: Button? = null

    var ReceiptCopy = ""
    var progressBar = CustomProgressBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        askPermissions()
        en = findViewById(R.id.engData)
        hi = findViewById(R.id.hindiData)
        go_home = findViewById(R.id.go_home)
        radio_group_languge = findViewById(R.id.radio_group_languge)
        //  genrateBitmap(null )
        //saveImages(applicationContext,"printfile" + ".bmp",null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // managePermissions.checkPermissions()

            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
        en!!.setOnClickListener(View.OnClickListener {
            engData = intent.getStringExtra("engData").toString()
        })

        hi!!.setOnClickListener(View.OnClickListener {
            engData = intent.getStringExtra("hindiData").toString()
        })
        go_home!!.setOnClickListener(View.OnClickListener {

            val intent = Intent(this@SuccessActivity, MainActivity::class.java)
            startActivity(intent);
        })


        //   val cardView = findViewById<CardView>(R.id.card_View)

        setupPermissions()
        openButton = findViewById(R.id.print)
        setup = Setup()
        val result: Boolean = setup!!.blActivateLibrary(this, R.raw.licence)
        if (result) {
            Toast.makeText(applicationContext, "Library Activated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Library Not Activated", Toast.LENGTH_SHORT).show()
        }

        openButton!!.setOnClickListener {

            if (radio_group_languge!!.getCheckedRadioButtonId() == -1) {
                Toast.makeText(
                    getApplicationContext(),
                    "Please select either one",
                    Toast.LENGTH_SHORT
                ).show();

            } else {


                progressBar.show(this@SuccessActivity, resources.getString(R.string.please_wait))
                CoroutineScope(Dispatchers.Main).launch {

                    findBT()


                }
            }


            // openBT()
        }
    }

    protected fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val requestCode = 200
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "hello.bmp"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/bmp")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
//            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//            val image = File(imagesDir, filename)
//            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //granted
            } else {
                //deny
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    requestBluetooth.launch(enableBtIntent)
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    private fun setupPermissions() {

    }


    suspend fun findBT() {
        // Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();

        withContext(Dispatchers.IO) {
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                if (mBluetoothAdapter == null) {
                    // myLabel!!.text = "No bluetooth adapter available"
                }
                if (!mBluetoothAdapter!!.isEnabled()) {
                    val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBluetooth, 1)
                }

                val pairedDevices = mBluetoothAdapter?.getBondedDevices()


                if (pairedDevices?.size!! > 0) {
                    for (device in pairedDevices) {
                        // Toast.makeText(this, "Exception_4", Toast.LENGTH_LONG).show()
//


                        //  Toast.makeText(this, device.getAddress().toString(),Toast.LENGTH_LONG).show();
                        // RPP300 is the name of the bluetooth printer device
                        // we got this name from the list of paired devices
//                    if (device.name == "RPP300") {
//
//                        break
//                    }

                    if (device.name.startsWith("ES")) {
                        printReceipt2(device.address)



                        break

                    }


                    }
                }
                // myLabel!!.text = "Bluetooth device found."
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
        //  progressDialog?.dismiss()
    }


    fun printReceipt2(saddress: String?) {
        // Toast.makeText(this, "Exception_1", Toast.LENGTH_LONG).show()

        try {

            val BFONT: Byte = 0X03
            val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(saddress)
            val uuidComm = UUID.fromString(UUID_STR)
            if (umbsSocket == null) {
                umbsSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm)
            }
            if (!umbsSocket!!.isConnected()) {

                var coonectcounter = 0
                while (true) {
                    try {
                        umbsSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm)
                        umbsSocket!!.connect()
                    } catch (e: Exception) {
                        progressBar.dialog?.dismiss()
                        Log.d("Exception", e.toString())
                    }
                    Thread.sleep(3000)
                    try {
                        if (umbsSocket!!.isConnected()) {
                            progressBar.dialog?.dismiss()
                            break
                        }
                        progressBar.dialog?.dismiss()
                        coonectcounter++
                        if (coonectcounter == 7) {
                            progressBar.dialog?.dismiss()
                            break
                        }
                    } catch (e: Exception) {
                        progressBar.dialog?.dismiss()
                    }

                }
                if (coonectcounter == 7) {
                    msg = Message()
                    msg!!.obj = "BLNF"
                    //  errhandler.sendMessage(msg!!)
                    return
                }
                mosOut = umbsSocket!!.getOutputStream() //Get global output stream object
                misIn = umbsSocket!!.getInputStream()

                // Ptr = new com.impress.api.Printer(setup, mosOut, misIn);
            } else {

                var coonectcounter = 0
                while (true) {
                    try {
                        umbsSocket = device.createInsecureRfcommSocketToServiceRecord(uuidComm)
                        umbsSocket!!.connect()
                    } catch (e: java.lang.Exception) {
                    }
                    Thread.sleep(3000)
                    if (umbsSocket!!.isConnected()) {
                        progressBar.dialog?.dismiss()
                        break
                    }
                    coonectcounter++
                    if (coonectcounter == 7) {
                        progressBar.dialog?.dismiss()
                        break
                    }
                    mosOut = umbsSocket!!.getOutputStream() //Get global output stream object
                    misIn = umbsSocket!!.getInputStream()
                }

            }
            try {

                if (printer == null) {
                    printer = Printer(setup, mosOut!!, misIn!!)
                    progressBar.dialog?.dismiss()
                }
            } catch (e: Exception) {
                progressBar.dialog?.dismiss()
            }


            var iReturnvalue = 0;
            val Implementation = ""
            var sADDSTRING = ""
            var sFooter_Print = ""
            val TxnTime = "" // printdate(createddate);

            val TxnDate = ""

            val sBCName = ""
            printformatone = Vector()
            // var sdeviceprintformate = "SETIMAGE~SETFILENAME=[IMG]~PRINT|SETSTRING~SETFONT=01~ADDSTRING= #N00~PRINT~ADDSTRING=Patient Name: Asha Kumri#M42~PRINT~ADDSTRING=Patient Mobile: 9876543210#M42~PRINT~ADDSTRING=Test Date: 27-Aug-22#M42~PRINT~ADDSTRING= #N00~PRINT~ADDSTRING=Weight: 67 KG#N00~SETALIGN#N00~PRINT~ADDSTRING=Temperature: 99.5 F#N00~SETALIGN#N00~PRINT~ADDSTRING=Blood Pressure: 118/78#N00^[AI] #N00~SETALIGN#N00~PRINT~ADDSTRING=Pulse Rate: #N00^[BL]#N00~SETALIGN#N00~PRINT~ADDSTRING=Hemoglobin: #N00^[DI]#N00~SETALIGN#N00~PRINT~ADDSTRING=Fundal Height: #N00^[XA] #N00~SETALIGN#N00~PRINT~ADDSTRING=Fetal Heart Rate: #N00^[CN]#N00~SETALIGN#N00~PRINT~ADDSTRING=Random Blood Sugar: 123 #N00^[SN]#N00~SETALIGN#N00~PRINT~ADDSTRING=Urine Test: #N00^[RR] #N00~SETALIGN#N00~PRINT~ADDSTRING=Protein (mg/dL) 30 secs: #N00^[SCN]#N00~SETALIGN#N00~PRINT~ADDSTRING=Urine Sugar(mg/dl) 30 secs: #N00^[TS]#N00~SETALIGN#N00~PRINT~SETSTRING~SETFONT=01~Footer= #N00~PRINT~Footer=**These are precautionary tests. Please connect with doctors for any further details#M42~PRINT"
            // var sdeviceprintformate = "SETIMAGE~SETFILENAME=[IMG]~ PRINT|SETSTRING~SETFONT=01~ADDSTRING=    PRINT~ADDSTRING=रोगी का नाम: Asha Kumri#M42~ PRINT~ADDSTRING=रोगी मोबाइल: 9876543210#M42~ PRINT~ADDSTRING=परीक्षा की तारीख: 27-Aug-22#M42~ PRINT~ADDSTRING=   #N00~PRINT~ADDSTRING=वज़न:                67 KG#N00~SETALIGN#N00~ PRINT~ADDSTRING=तापमान:        99.5 F#N00~SETALIGN#N00~ PRINT~ADDSTRING=रक्त चाप:        118/78#N00^[AI]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=प्लस रेट:                    #N00^[BL]#N00~SETALIGN#N00~ PRINT~ADDSTRING=हीमोग्लोबिन:                    #N00^[DI]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मौलिक ऊंचाई:                #N00^[XA]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=भ्रूण की हृदय गति:            #N00^[CN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=यादृच्छिक रक्त शर्करा:     123 #N00^[SN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मूत्र परीक्षण:                    #N00^[RR]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=प्रोटीन (mg/dL) 30 secs:    #N00^[SCN]#N00~SETALIGN#N00~\n PRINT~ADDSTRING=मूत्र शर्करा(mg/dl) 30 secs: #N00^[TS]#N00~SETALIGN#N00~ PRINT~SETSTRING~SETFONT=01~Footer=     #N00~ PRINT~Footer=**These are precautionary tests. Please connect with doctors for any further details#M42~PRINT"
            //   var sdeviceprintformate ="SETIMAGE~SETFILENAME=[IMG]~ PRINT~ADDSTRING=Patient Name: Asha Kumri#M42~ PRINT~ADDSTRING=Patient Mobile No: 9876543210#M42~ PRINT~ADDSTRING=Test Date: 27-Aug-22#M42~ PRINT~ADDSTRING=Gender: Male, Age:27#M42~ PRINT~ADDSTRING=   #N00~ PRINT~ADDSTRING=वजन:                                                67 KG#N00~SETALIGN#N00~ PRINT~ADDSTRING=तापमान:                     99.5 F#N00~SETALIGN#N00~ PRINT~ADDSTRING=रक्त चाप:                     118/78#N00^[AI]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=पल्स दर:                             #N00^[BL]#N00~SETALIGN#N00~ PRINT~ADDSTRING=हीमोग्लोबिन:                                  #N00^[DI]#N00~SETALIGN#N00~ PRINT~ADDSTRING=रैंडम ब्लड शुगर:                      115#N00^[XA]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=पमूत्र प्रोटीन (mg/dL) 30 secs:#N00^[SCN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मूत्र शुगर (mg/dL) 30 secs: #N00^[TS]#N00~SETALIGN#N00~ PRINT~SETSTRING~SETFONT=01~Footer=   #N00~ PRINT~Footer=**उपरोक्त परीक्षण रिपोर्ट एक पेशेवर राय है और अंतिम निदान के लिए अन्य नैदानिक इतिहास और अन्य प्रासंगिक जांच के साथ सहसंबद्ध होने की आवश्यकता है।#M42~PRINT"
            // var sdeviceprintformate = "SETIMAGE~SETFILENAME=[IMG]~ PRINT|SETSTRING~SETFONT=01~ADDSTRING =Patient Name: Asha Kumri#M42~ PRINT~ADDSTRING=Patient Mobile No: 9876543210#M42~ PRINT~ADDSTRING=Test Date: 27-Aug-22#M42~ PRINT~ADDSTRING=Gender: Male, Age:27#M42~ PRINT~ADDSTRING=   #N00~ PRINT~ADDSTRING=वजन:                                                67 KG#N00~SETALIGN#N00~ PRINT~ADDSTRING=तापमान:                     99.5 F#N00~SETALIGN#N00~ PRINT~ADDSTRING=रक्त चाप:                     118/78#N00^[AI]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=पल्स दर:                             #N00^[BL]#N00~SETALIGN#N00~ PRINT~ADDSTRING=हीमोग्लोबिन:                                  #N00^[DI]#N00~SETALIGN#N00~ PRINT~ADDSTRING=रैंडम ब्लड शुगर:                      115#N00^[XA]   #N00~SETALIGN#N00~  PRINT~ADDSTRING=पमूत्र प्रोटीन (mg/dL) 30 secs:#N00^[SCN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मूत्र शुगर (mg/dL) 30 secs: #N00^[TS]#N00~SETALIGN#N00~ PRINT~SETSTRING~SETFONT=01~Footer=   #N00~PRIT~Footer=**उपरोक्त परीक्षण रिपोर्ट एक पेशेवर राय है और अंतिम निदान के लिए अन्य नैदानिक इतिहास और अन्य प्रासंगिक जांच के साथ सहसंबद्ध होने की आवश्यकता है।#M42~PRINT"
            // var sdeviceprintformate = "SETIMAGE~SETFILENAME=[IMG]~ PRINT|SETSTRING~SETFONT=01~ADDSTRING=    PRINT~ADDSTRING=Patient Name: Asha Kumri#M42~ PRINT~ADDSTRING=Patient Mobile No: 9876543210#M42~ PRINT~ADDSTRING=Test Date: 27-Aug-22#M42~ PRINT~ADDSTRING=Gender: Male, Age:27#M42~ PRINT~ADDSTRING=   ~ PRINT~ADDSTRING=वजन:                                                67 KG~SETALIGN#N00~ PRINT~ADDSTRING=तापमान:                     99.5 F~SETALIGN#N00~ PRINT~ADDSTRING=रक्त चाप:                     118/78#N00^[AI]   ~SETALIGN#N00~ PRINT~ADDSTRING=पल्स दर:                             ^[BL]#N00~SETALIGN#N00~ PRINT~ADDSTRING=हीमोग्लोबिन:                                  ^[DI]#N00~SETALIGN#N00~ PRINT~ADDSTRING=रैंडम ब्लड शुगर:                      115^[XA]   ~SETALIGN#N00~  PRINT~ADDSTRING=पमूत्र प्रोटीन (mg/dL) 30 secs:#N00^[SCN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मूत्र शुगर (mg/dL) 30 secs:                                                                              ^[TS]#N00~SETALIGN#N00~ PRINT~SETSTRING~SETFONT=01~Footer=   PRINT~Footer=**उपरोक्त परीक्षण रिपोर्ट एक पेशेवर राय है और अंतिम निदान के लिए अन्य नैदानिक इतिहास और अन्य प्रासंगिक जांच के साथ सहसंबद्ध होने की आवश्यकता है।#M42~PRINT"
//            var sdeviceprintformate =
//                "SETIMAGE~SETFILENAME=[IMG]~ PRINT|SETSTRING~SETFONT=01~ADDSTRING=    PRINT~ADDSTRING=Patient Name: Asha Kumri#M42~ PRINT~ADDSTRING=Patient Mobile No: 9876543210#M42~ PRINT~ADDSTRING=Test Date: 27-Aug-22#M42~ PRINT~ADDSTRING=Gender: Male, Age:27#M42~ PRINT~ADDSTRING=   #N00~ PRINT~ADDSTRING=वजन:         67 KG#N00~SETALIGN#N00~ PRINT~ADDSTRING=तापमान:                     99.5 F#N00~SETALIGN#N00~ PRINT~ADDSTRING=रक्त चाप:                     118/78#N00^[AI]   #N00~SETALIGN#N00~ PRINT~ADDSTRING=पल्स दर:                    #N00^[BL]#N00~SETALIGN#N00~ PRINT~ADDSTRING=हीमोग्लोबिन:                       #N00^[DI]#N00~SETALIGN#N00~ PRINT~ADDSTRING=रैंडम ब्लड शुगर:             115#N00^[XA]   #N00~SETALIGN#N00~  PRINT~ADDSTRING=पमूत्र प्रोटीन (mg/dL) 30 secs:#N00^[SCN]#N00~SETALIGN#N00~ PRINT~ADDSTRING=मूत्र शुगर      (mg/dL) 30 secs:                                                                              #N00^[TS]#N00~SETALIGN#N00~ PRINT~SETSTRING~SETFONT=01~Footer=   #N00~PRINT~Footer=**उपरोक्त परीक्षण रिपोर्ट एक पेशेवर राय है और अंतिम निदान के लिए अन्य नैदानिक इतिहास और अन्य प्रासंगिक जांच के साथ सहसंबद्ध होने की आवश्यकता है।#M42~PRINT"
            var sdeviceprintformate = engData

            sdeviceprintformate = sdeviceprintformate.replace("\r", "")
            sdeviceprintformate = sdeviceprintformate.replace("\n", "")
            sdeviceprintformate = sdeviceprintformate.replace("[IMG]", Implementation)
            sdeviceprintformate = sdeviceprintformate.replace("[DT]", TxnDate)
            sdeviceprintformate = sdeviceprintformate.replace("[TI]", TxnTime)
            sdeviceprintformate = sdeviceprintformate.replace("[BC]", sBCName)
            sdeviceprintformate = sdeviceprintformate.replace("[AI]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[DI]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[XA]", "");
            sdeviceprintformate = sdeviceprintformate.replace("[XA]", "");
            sdeviceprintformate = sdeviceprintformate.replace("[XA]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[CA]", "1234567899");
            sdeviceprintformate = sdeviceprintformate.replace("[CA]", "1234567899");
            sdeviceprintformate = sdeviceprintformate.replace("[TA]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[AB]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[UAC]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[BL]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[SCN]", "")
            sdeviceprintformate = sdeviceprintformate.replace("[TS]", "")

            //val sFrmtArr: Array<String> = sdeviceprintformate.split("\\|")


            val sFrmtArr: Array<String> = sdeviceprintformate.split("|").toTypedArray()
            val imagePath = sFrmtArr[0].split("~").toTypedArray()
            // val imagePath: List<String> = sFrmtArr.get(0).split("~")
            for (img in imagePath.indices) {
                if (imagePath[img].contains("=")) {
                    var img1 = imagePath[img].split("=")
                    sSETIMGFILENAME = img1[1]
                    // sSETIMGFILENAME = imagePath[img].split("=")[1];
                }
            }
            var SETFONT = ""
            var bFont: Byte = 0
            val sProcData: Array<String> = sFrmtArr.get(1).split("~").toTypedArray()
            val valnew: Byte = 0X03
            try {
                for (prin in sProcData.indices) {
                    if (sProcData[prin].contains("SETFONT")) {
                        SETFONT = sProcData[prin].split("=").toTypedArray()[1]
                        bFont = java.lang.Byte.valueOf(SETFONT)
                    } else if (sProcData[prin].contains("ADDSTRING")) {
                        sADDSTRING = ProcessPrintData(sProcData[prin])
                    } else if (sProcData[prin].contains("SETALIGN")) {
                        // take care of entire line alignment
                        var sAln = sProcData[prin].split("#").toTypedArray()[1]
                        if (sAln.contains("M")) {
                            var iTotal = Integer.valueOf(sAln.substring(1))
                            var m = Math.round((((iTotal - sADDSTRING!!.length) / 2)).toDouble())
                                .toInt()
                            sADDSTRING = java.lang.String.format(
                                "%" + (m + sADDSTRING.length).toString() + "s",
                                sADDSTRING
                            )
                            sADDSTRING = java.lang.String.format("%-" + iTotal + "s", sADDSTRING)
                        } else if (sAln.contains("R")) {
                            var iR = Integer.valueOf(sAln.substring(1))
                            sADDSTRING = java.lang.String.format(
                                "%-" + (iR + sADDSTRING.length).toString() + "s",
                                sADDSTRING
                            )
                        } else if (sAln.contains("L")) {
                            var iL = Integer.valueOf(sAln.substring(1))
                            sADDSTRING = java.lang.String.format(
                                "%" + (iL + sADDSTRING.length).toString() + "s",
                                sADDSTRING
                            )
                        }
                    } else if (sProcData[prin].contains("Footer")) {
                        sFooter_Print = ProcessPrintData(sProcData[prin])

                    } else if (sProcData[prin].contains("PRINT")) {

                        if (sADDSTRING !== "") {

                            if (TatvikDevice) {
//
                            } else if (SQUIRREL) {
//
                            } else {
                                if (en!!.isChecked == true) {
                                    val j: Int = printer!!.iPrinterAddData(valnew, sADDSTRING)
                                } else {
                                    printformatone!!.add(sADDSTRING)
                                }
                                //  printer!!.iPrinterAddData(valnew, sADDSTRING)
                            }
//                                if (iReturnvalue !== 0) {
//                                    throw java.lang.Exception("Print Unscuss")
//                                }
                            sADDSTRING = ""
                        } else if (sFooter_Print !== "") {
                            if (en!!.isChecked == true) {
                                printer!!.iPrinterAddData(BFONT, sFooter_Print)
                            } else {
                                printformatone!!.add(sFooter_Print)
                            }
                        }
                        sFooter_Print = ""

                    }
                }

                if (en!!.isChecked == true) {
                    printer!!.iBmpPrint(getApplicationContext(), R.drawable.tvam_logo);
                    printer!!.iStartPrinting(1);

                } else {
                    printer!!.iBmpPrint(getApplicationContext(), R.drawable.tvam_logo);
                    Thread.sleep(1000)
                    genrateBitmap(printformatone);
                    //  val bitmap: Bitmap? =null
//                    val canvas = bitmap?.let { Canvas(it) }
//                    val loadBitMap =loadBitmap(this,"printfilenew.bmp")
//                   // printer!!.iBmpPrint(loadBitMap)

////
                    val path = Environment.getExternalStorageDirectory().absolutePath
                    val bmpFile = File(path, "printfilenew.bmp")
                    printer!!.iBmpPrint(bmpFile)
                    printer!!.iFlushBuf()
                    printer!!.iPrinterAddData(BFONT, "         ");
                    progressBar.dialog?.dismiss()
                    Thread.sleep(1000)
                    Thread.sleep(2000)

//                    var fos: OutputStream? = null
//                    this.contentResolver?.also { resolver ->
//
//                        // Content resolver will process the contentvalues
//                        val contentValues = ContentValues().apply {
//
//                            // putting file information in content values
//                            put(MediaStore.MediaColumns.DISPLAY_NAME, "printfilenew.bmp")
//                            put(MediaStore.MediaColumns.MIME_TYPE, "image/bmp")
//                            put(
//                                MediaStore.MediaColumns.RELATIVE_PATH,
//                                Environment.DIRECTORY_PICTURES
//                            )
//                        }
//                        val imageUri: Uri? = resolver.insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                            contentValues
//                        )
//
//                        // Opening an outputstream with the Uri that we got
//                        fos = imageUri?.let { resolver.openOutputStream(it) }
//                    }
//                    fos?.use {
//                        // Finally writing the bitmap to the output stream that we opened
//                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
//                        Toast.makeText(this , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
//                    }

                }


                progressBar.dialog?.dismiss()

                msg = Message()
                msg!!.obj = "close"
                // handlerprocess.sendMessage(msg!!)

            } catch (e: Exception) {
                progressBar.dialog?.dismiss()
                Log.d("Exception1", e.toString())
                // Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_LONG).show()
                printer!!.iFlushBuf()
                msg = Message()
                msg!!.obj = "PRECAP"
                // errhandler.sendMessage(msg!!)
            }


        } catch (e: Exception) {
            Log.d("Exception_", e.toString())
        }
    }


    fun saveMediaToStorage(bitmap: Bitmap, context: Context, onUriCreated: (Uri) -> Unit) {
        var fos: OutputStream? = null

//Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.jpg")

                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = Objects.requireNonNull(imageUri)?.let {
                    resolver.openOutputStream(it)
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                Objects.requireNonNull(fos)

                imageUri?.let { onUriCreated(it) }
            } else {
                //These for devices running on android < Q
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)

                fos.use {
                    //Finally writing the bitmap to the output stream that we opened
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }

                onUriCreated(Uri.fromFile(image))
            }
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
    }

    @Throws(IOException::class)
    private fun saveImages(mContext: Context, name: String, bmpdata: Vector<String>?): Uri? {
        var bitmap: Bitmap? = null
        var resized: Bitmap
        val saved: Boolean
        val fos: OutputStream?
        var image: File? = null
        var imageUri: Uri? = null
        var imagesDir: String? = null


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Bitmap.createBitmap(
                384, 700,
                Bitmap.Config.ARGB_4444
            )
            val resolver = mContext.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = resolver.openOutputStream(imageUri!!)
            val canvas = bitmap?.let { Canvas(it) }
            val paint = Paint()
            canvas?.drawText("hello", 50F, 20F, paint)
            canvas?.drawText("hi", 50F, 20F, paint)
            canvas?.drawText("hiiiii", 50F, 20F, paint)
            canvas?.drawText("hiiii", 50F, 20F, paint)
            var veal = 30
//            for (i in 0 until bmpdata!!.size) {
//                bmpdata[i]?.let { canvas?.drawText(it, 10F, veal.toFloat(), paint) }
//                veal = veal + 30
//            }
            //   bmpdata?.get(0)?.let { canvas?.drawText(it, 10F, veal.toFloat(), paint)}
            val outnew = ByteArrayOutputStream()
            val decoded: Bitmap = BitmapFactory
                .decodeStream(ByteArrayInputStream(outnew.toByteArray()))
            resized = ThumbnailUtils.extractThumbnail(decoded, 384, 1000);
        } else {
            imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ).toString() + File.separator
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            image = File(imagesDir, "$name.png")
            fos = FileOutputStream(image)
        }

        saved = bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos!!.flush()
        fos.close()
        return imageUri
    }


    private fun genrateBitmap(bmpdata: Vector<String>?) {
        val bitmap: Bitmap
        val resized: Bitmap

//        val saved: Boolean
//        val fos: OutputStream?
//        var image: File? = null
//        var imageUri: Uri? = null
//        var imagesDir: String? = null
//        val resolver = applicationContext.contentResolver
//        val contentValues = ContentValues()
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "printfile" + ".bmp")
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/bmp")
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,  Environment.DIRECTORY_PICTURES)
//        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        fos = resolver.openOutputStream(imageUri!!)

        val path = Environment.getExternalStorageDirectory()
        val myDir = File(valueOf(path))
        myDir.mkdirs()
        val fname = "printfile" + ".bmp"

        val file = File(myDir, fname)

        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            val paint = Paint()
            bitmap = if (!sTxnType.equals("MS", false)) {
                Bitmap.createBitmap(
                    384, 700,
                    Bitmap.Config.ARGB_4444
                )
            } else {
                Bitmap.createBitmap(
                    384, 1000,
                    Bitmap.Config.ARGB_4444
                )
            }
            val canvas = Canvas(bitmap)
            paint.setColor(Color.WHITE)
            canvas.drawRect(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
            paint.setColor(Color.BLACK)
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            paint.setTextSize(20F)
            var veal = 30
            for (i in 0 until bmpdata!!.size) {
                bmpdata[i]?.let { canvas.drawText(it, 10F, veal.toFloat(), paint) }
                veal = veal + 30
            }
//            canvas.drawText("hello", 50F, 20F,paint)
//            canvas.drawText("hi", 50F, 20F,paint)
//            canvas.drawText("hiiiii", 50F, 20F,paint)
//            canvas.drawText("hiiii", 50F, 20F,paint)

            /*
			 * canvas.drawText("नकद आहरण के साथ बचत", 50, 20, paint);
			 * canvas.drawText("तारीख:", 10, 55, paint);
			 * canvas.drawText("20/08/2015", 70, 55, paint);
			 * canvas.drawText("समय:", 190, 55, paint);
			 * canvas.drawText("13:37 PM", 240, 55, paint);
			 * canvas.drawText("बी.सी नाम", 10, 85, paint); canvas.drawText(":",
			 * 150, 85, paint); canvas.drawText("किरण", 160, 85, paint);
			 * canvas.drawText("एजेंट आईडी", 10, 115, paint);
			 * canvas.drawText(":", 150, 115, paint); canvas.drawText("1234",
			 * 160, 115, paint); canvas.drawText("बी.सी स्थान", 10, 145, paint);
			 * canvas.drawText(":", 150, 145, paint); canvas.drawText("बंगलौर",
			 * 160, 145, paint); canvas.drawText("टर्मिनल आईडी", 10, 175,
			 * paint); canvas.drawText(":", 150, 175, paint);
			 * canvas.drawText("12345", 160, 175, paint);
			 * canvas.drawText("खाता क्रमांक", 10, 205, paint);
			 * canvas.drawText(":", 150, 205, paint);
			 * canvas.drawText("574857455", 160, 205, paint);
			 * canvas.drawText("ग्राहक का नाम", 10, 235, paint);
			 * canvas.drawText(":", 150, 235, paint); canvas.drawText("शरण",
			 * 160, 235, paint); canvas.drawText("स्टेन", 10, 265, paint);
			 * canvas.drawText(":", 150, 265, paint); canvas.drawText("143435",
			 * 160, 265, paint); canvas.drawText("आर.आर.एन ", 10, 295, paint);
			 * canvas.drawText(":", 150, 295, paint);
			 * canvas.drawText("4412343435", 160, 295, paint);
			 * canvas.drawText("स्मार्ट कार्ड नंबर", 10, 325, paint);
			 * canvas.drawText(":", 150, 325, paint); canvas.drawText("5354665",
			 * 160, 325, paint); canvas.drawText("टी.एक्स.एन स्थिति", 10, 355,
			 * paint); canvas.drawText(":", 150, 1355, paint);
			 * canvas.drawText("000", 160, 355, paint);
			 * canvas.drawText("टी.एक्स.एन मात्रा", 10, 385, paint);
			 * canvas.drawText(":", 150, 385, paint); canvas.drawText("10.00",
			 * 160, 385, paint); canvas.drawText("खाते में शेष ", 10, 415,
			 * paint); canvas.drawText(":", 150, 415, paint);
			 * canvas.drawText("13000", 160, 415, paint);
			 * canvas.drawText("atyati Ganaseva द्वारा संचालित", 30, 465,
			 * paint);
			 */
            val outnew = ByteArrayOutputStream()
//            fos?.use {
//                // Finally writing the bitmap to the output stream that we opened
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outnew)
//               // Toast.makeText(this , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
//                val decoded = BitmapFactory
//                    .decodeStream(ByteArrayInputStream(outnew.toByteArray()))
//                save(decoded, "printfile.bmp")
//                resized = if (!sTxnType.equals("MS", false)) {
//                    ThumbnailUtils.extractThumbnail(decoded, 384, 700)
//                } else {
//                    ThumbnailUtils.extractThumbnail(decoded, 384, 1000)
//                }
//
//                // Bitmap hj= getResizedBitmap(resized,384);
//                save(resized, "printfilenew.bmp")
//            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outnew)
            val decoded = BitmapFactory
                .decodeStream(ByteArrayInputStream(outnew.toByteArray()))
            save(decoded, "printfile.bmp")
            resized = if (!sTxnType.equals("MS", false)) {
                ThumbnailUtils.extractThumbnail(decoded, 384, 700)
            } else {
                ThumbnailUtils.extractThumbnail(decoded, 384, 1000)
            }

            //     Bitmap hj= getResizedBitmap(resized,384);
            save(resized, "printfilenew.bmp")
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
//            Toast.makeText(applicationContext, "Exception_3" + e.toString(), Toast.LENGTH_LONG)
//                .show()
            Log.d("Exception@", e.toString())
        }
    }

    fun saveFile(context: Context, b: Bitmap, picName: String?) {
        var fos: FileOutputStream? = null
        try {
            fos = context.openFileOutput(picName, MODE_PRIVATE)
            b.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "file not found")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d(TAG, "io exception")
            e.printStackTrace()
        } finally {
            fos!!.close()
        }
    }

    fun loadBitmap(context: Context, picName: String?): Bitmap? {
        var b: Bitmap? = null
        var fis: FileInputStream? = null
        try {
            fis = context.openFileInput(picName)
            b = BitmapFactory.decodeStream(fis)
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "file not found")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d(TAG, "io exception")
            e.printStackTrace()
        } finally {
            fis!!.close()
        }
        return b
    }


    @Throws(IOException::class)
    fun save(orgBitmap: Bitmap?, filePath: String?): Boolean {
        try {
            val start = System.currentTimeMillis()
            if (orgBitmap == null) {
                return false
            }
            if (filePath == null) {
                return false
            }
            val isSaveSuccess = true

            // image size
            val width = orgBitmap.width
            val height = orgBitmap.height

            // image dummy data size
            // reason : the amount of bytes per image row must be a multiple of 4
            // (requirements of bmp format)
            var dummyBytesPerRow: ByteArray? = null
            var hasDummy = false
            val rowWidthInBytes: Int = BYTE_PER_PIXEL * width // source image width *
            // number of bytes to
            // encode one pixel.
            if (rowWidthInBytes % BMP_WIDTH_OF_TIMES > 0) {
                hasDummy = true
                // the number of dummy bytes we need to add on each row
                dummyBytesPerRow =
                    ByteArray(BMP_WIDTH_OF_TIMES - rowWidthInBytes % BMP_WIDTH_OF_TIMES)
                // just fill an array with the dummy bytes we need to append at the
                // end of each row
                for (i in dummyBytesPerRow.indices) {
                    dummyBytesPerRow[i] = 0xFF.toByte()
                }
            }

            // an array to receive the pixels from the source image
            val pixels = IntArray(width * height)

            // the number of bytes used in the file to store raw image data
            // (excluding file headers)
            val imageSize =
                (rowWidthInBytes + if (hasDummy) dummyBytesPerRow!!.size else 0) * height
            // file headers size
            val imageDataOffset = 0x36

            // final size of the file
            val fileSize = imageSize + imageDataOffset

            // Android Bitmap Image Data
            orgBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

            // ByteArrayOutputStream baos = new ByteArrayOutputStream(fileSize);
            val buffer: ByteBuffer = ByteBuffer.allocate(fileSize)
            /**
             * BITMAP FILE HEADER Write Start
             */
            buffer.put(0x42.toByte())
            buffer.put(0x4D.toByte())

            // size
            buffer.put(writeInt(fileSize))

            // reserved
            buffer.put(writeShort(0.toShort()))
            buffer.put(writeShort(0.toShort()))

            // image data start offset
            buffer.put(writeInt(imageDataOffset))
            /** BITMAP FILE HEADER Write End  */

            // *******************************************
            /** BITMAP INFO HEADER Write Start  */
            // size
            buffer.put(writeInt(0x28))

            // width, height
            // if we add 3 dummy bytes per row : it means we add a pixel (and the
            // image width is modified.
            buffer.put(
                writeInt(
                    width
                            + if (hasDummy) if (dummyBytesPerRow!!.size == 3) 1 else 0 else 0
                )
            )
            buffer.put(writeInt(height))

            // planes
            buffer.put(writeShort(1.toShort()))

            // bit count
            buffer.put(writeShort(24.toShort()))

            // bit compression
            buffer.put(writeInt(0))

            // image data size
            buffer.put(writeInt(imageSize))

            // horizontal resolution in pixels per meter
            buffer.put(writeInt(0))

            // vertical resolution in pixels per meter (unreliable)
            buffer.put(writeInt(0))
            buffer.put(writeInt(0))
            buffer.put(writeInt(0))
            /** BITMAP INFO HEADER Write End  */
            var row = height
            val col = width
            var startPosition = (row - 1) * col
            var endPosition = row * col
            while (row > 0) {
                for (i in startPosition until endPosition) {
                    buffer.put((pixels[i] and 0x000000FF).toByte())
                    buffer.put(((pixels[i] and 0x0000FF00) shr 8).toByte())
                    buffer.put(((pixels[i] and 0x00FF0000) shr 16).toByte())
                }
                if (hasDummy) {
                    buffer.put(dummyBytesPerRow)
                }
                row--
                endPosition = startPosition
                startPosition = startPosition - col
            }
            // saveFile(this,orgBitmap,filePath)
            val path = Environment.getExternalStorageDirectory().absolutePath
            val bmpFile = File(path, filePath)
            val fos = FileOutputStream(bmpFile)
            fos.write(buffer.array())
            fos.close()


            //   Log.v("AndroidBmpUtil", System.currentTimeMillis() - start.toString() + " ms")
            return isSaveSuccess
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Exception_3" + e.toString(), Toast.LENGTH_LONG)
                .show()
        }
        return null == true
    }

    @Throws(IOException::class)
    private fun writeInt(value: Int): ByteArray? {
        val b = ByteArray(4)
        b[0] = (value and 0x000000FF).toByte()
        b[1] = (value and 0x0000FF00 shr 8).toByte()
        b[2] = (value and 0x00FF0000 shr 16).toByte()
        b[3] = (value and -0x1000000 shr 24).toByte()
        return b
    }


    @Throws(IOException::class)
    private fun writeShort(value: Short): ByteArray? {
        val b = ByteArray(2)
        b[0] = (value and 0x00FF).toByte()
        b[1] = (value and 0xFF00.toShort()).toByte()

        return b
    }

    fun ProcessPrintData(data: String?): String {
        // ADDSTRING=Saving withdrawal by cash#M42    imagePath[img].split("\\=")[1];
        var sSendData = ""
        try {
            val sData: String = data!!.split("=").get(1)
            val sAlignment = sData.split("^").toTypedArray()
            var sFinalData = ""

            var noP: Int
            for (sprin in sAlignment.indices) {
                val sAlign = sAlignment[sprin].split("#").toTypedArray()[1]
                if (sAlign.contains("M")) {
                    //Process for middle align
                    noP = Integer.valueOf(sAlign.substring(1))
                    sFinalData = sAlignment[sprin].split("#").toTypedArray()[0]
                    val m =
                        Math.round(java.lang.Double.valueOf(((noP - sFinalData.length) / 2).toDouble()))
                            .toInt()
                    sFinalData = String.format("%" + (m + sFinalData.length) + "s", sFinalData)
                    // sFinalData = String.fosFinalData.PadLeft(m + sFinalData.length(), ' ');
                    sFinalData = String.format("%-" + noP + "s", sFinalData)
                    // sFinalData = sFinalData.PadRight(noP, ' ');
                } else if (sAlign.contains("R")) {
                    //Process for right align
                    noP = Integer.valueOf(sAlign.substring(1))
                    sFinalData = sAlignment[sprin].split("#").toTypedArray()[0]
                    //sFinalData = sFinalData.PadRight(noP + sFinalData.length(), ' ');
                    sFinalData = String.format("%-" + (noP + sFinalData.length) + "s", sFinalData)
                } else if (sAlign.contains("L")) {
                    //process for left align
                    noP = Integer.valueOf(sAlign.substring(1))
                    sFinalData = sAlignment[sprin].split("#").toTypedArray()[0]
                    //sFinalData = sFinalData.PadLeft(noP + sFinalData.Length, ' ');
                    sFinalData = String.format("%" + (noP + sFinalData.length) + "s", sFinalData)
                } else {
                    sFinalData = sAlignment[sprin].split("#").toTypedArray()[0]
                }
                sSendData = sSendData + sFinalData
            }
            sSendData
        } catch (e: Exception) {

        }
        return sSendData
    }


}