package com.dave.properties

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.dave.properties.helper_class.DatesWorkerClass

class Splash : AppCompatActivity() {

    private lateinit var mWorkManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({

            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()

//            val preferences = this.getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
//            val boolValue: Boolean = preferences.getBoolean("isLoggedIn", false)
//            if (boolValue){
//
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            }else{
//                val intent = Intent(this, Registration::class.java)
//                startActivity(intent)
//                finish()
//            }


        }, 3000)
    }

    override fun onStart() {
        super.onStart()

//        mWorkManager = WorkManager.getInstance()
//        val mRequest = OneTimeWorkRequest.Builder(DatesWorkerClass::class.java).build()
//        mWorkManager.enqueue(mRequest)


    }
}