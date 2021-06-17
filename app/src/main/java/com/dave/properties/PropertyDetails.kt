package com.dave.properties

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dave.properties.adapter.MyRentDurationAdapter
import com.dave.properties.adapter.MyRentDurationViewAdapter
import com.dave.properties.helper_class.PropertyData
import com.dave.properties.helper_class.RentDuration
import com.dave.properties.helper_class.RentDurationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.activity_view_my_properties.*

class PropertyDetails : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceRent: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var uid: String? = null
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("properties")
        databaseReferenceRent = database.getReference("properties_rents")
        layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerViewRent1.layoutManager = layoutManager
        recyclerViewRent1.setHasFixedSize(true)

    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser
        if (currentUser != null) {

            uid = currentUser.uid
            getData()
        }

    }

    private fun getData() {

        val propertyKey = intent.getStringExtra("key")
        var rentDueList = ArrayList<RentDurationData?>()

        Log.e("-*-*-", propertyKey.toString())
        if (propertyKey != null){
            uid?.let {
                databaseReference.child(it).child(propertyKey).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        rentDueList.clear()

                        val propertyDataValues = dataSnapshot.getValue(PropertyData::class.java)
                        val property_name = propertyDataValues?.property_name.toString()
                        val property_location = propertyDataValues?.property_location.toString()
                        val rent_due_at = propertyDataValues?.rent_due_at.toString()
                        val property_details = propertyDataValues?.property_details.toString()
                        val property_rent = propertyDataValues?.property_rent.toString()
                        val propertyRentKey = propertyDataValues?.key.toString()

                        tvPropertyName.text = property_name
                        tvPropertyLocation.text = property_location
                        tvPropertyDueDate.text = rent_due_at
                        tvPropertyRentAmount.text = property_rent
                        tvMoreDetails.text = property_details

                        databaseReferenceRent.child(it).child(propertyRentKey).addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (rentSnapShot in snapshot.children){

                                    val propertyRentDataValues = rentSnapShot.getValue(
                                        RentDurationData::class.java)
                                    rentDueList.add(propertyRentDataValues)

                                }

                                val myRentDurationAdapter = MyRentDurationViewAdapter(rentDueList, this@PropertyDetails)
                                recyclerViewRent1.adapter = myRentDurationAdapter

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })



                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }


        }else{

            val intent = Intent(this, ViewMyProperties::class.java)
            startActivity(intent)
            finish()

        }


    }
}