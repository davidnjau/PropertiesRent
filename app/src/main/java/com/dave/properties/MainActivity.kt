package com.dave.properties

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dave.properties.adapter.MyPropertiesAdapter
import com.dave.properties.adapter.MyRentDurationAdapter
import com.dave.properties.helper_class.RentDuration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_my_properties.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    var myDate: String? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var uid: String? = null

    private var rentDueList = ArrayList<RentDuration>()
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        
        recyclerViewRent.layoutManager = layoutManager
        recyclerViewRent.setHasFixedSize(true)

        val testList = ArrayList<String>()
        testList.add("Monthly")
        testList.add("Quarterly")
        testList.add("Half Yearly")
        testList.add("Yearly")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, testList)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = arrayAdapter

        btnAddRentDuration.setOnClickListener {

            val duration = spinner.selectedItem.toString()
            val rentAmount = etRentAmount1.text.toString()

            if (!TextUtils.isEmpty(rentAmount)){

                val rentDuration = RentDuration(duration, rentAmount)
                rentDueList.add(rentDuration)

                val myRentDurationAdapter = MyRentDurationAdapter(rentDueList, this@MainActivity)
                recyclerViewRent.adapter = myRentDurationAdapter

            }else{
                etRentAmount1.error = "Your rent amount cannot be null."
            }

        }

        datePicker.setOnClickListener {

            myDate = null
            tvDate.text = ""

            day = 0
            month = 0
            year = 0
            hour = 0
            minute = 0
            myDay = 0
            myMonth = 0
            myYear = 0
            myHour = 0
            myMinute = 0

            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)

            val datePickerDialog =
                DatePickerDialog(
                    this@MainActivity, this@MainActivity,
                    year, month,day)
            datePickerDialog.show()


        }



        btnSaveProperty.setOnClickListener {

            val propertyName = etPropertyName.text.toString()
            val propertyLocation = etPropertyLocation.text.toString()
            val propertyDetails = etMoreDetails.text.toString()

            if (!TextUtils.isEmpty(propertyName) &&
                !TextUtils.isEmpty(propertyLocation)
                && myDate != null && uid != null && rentDueList.size > 0){

                var propertyMoreDetails = ""
                propertyMoreDetails = if (TextUtils.isEmpty(propertyDetails)){
                    ""
                }else{
                    propertyDetails
                }

                //Save to Firebase

                val pushKey = databaseReference.child("properties").child(uid!!).push().key.toString()
                val newPost = databaseReference.child("properties").child(uid!!).child(pushKey)

                val newPostRent = databaseReference.child("properties_rents").child(uid!!).child(pushKey)

                var totalAmount = 0
                for (items in rentDueList){

                    val rentData = newPostRent.push()

                    val duration = items.duration
                    val rentAmount = items.rentAmount

                    totalAmount += rentAmount.toInt()

                    rentData.child("duration").setValue(duration)
                    rentData.child("rentAmount").setValue(rentAmount)

                }

                newPost.child("property_name").setValue(propertyName)
                newPost.child("property_location").setValue(propertyLocation)
                newPost.child("property_details").setValue(propertyMoreDetails)
                newPost.child("rent_due_at").setValue(myDate.toString())
                newPost.child("property_rent").setValue(totalAmount.toString())

                Toast.makeText(this, "Property has been added successfully.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }else{

                if (TextUtils.isEmpty(propertyName)) etPropertyName.error = "Field cannot be empty."
                if (TextUtils.isEmpty(propertyLocation)) etPropertyLocation.error = "Field cannot be empty."
                if (myDate == null) Toast.makeText(this, "Please select a reasonable time", Toast.LENGTH_SHORT).show()
                if (uid == null) Toast.makeText(this, "Try Login in again.", Toast.LENGTH_SHORT).show()
                if (rentDueList.size < 1) Toast.makeText(this, "Please add rent details.", Toast.LENGTH_SHORT).show()

            }

        }

        btnViewMyProperties.setOnClickListener {

            val intent = Intent(this, ViewMyProperties::class.java)
            startActivity(intent)

        }
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month + 1

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this@MainActivity, this@MainActivity,
            hour, minute,
            false)
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

        val dateTime = "$myYear-$myMonth-$myDay $myHour:$myMinute"
        myDate = dateTime
        tvDate.text = dateTime
    }

    override fun onStart() {
        super.onStart()
        rentDueList.clear()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {

            uid = currentUser.uid

        }
    }
}