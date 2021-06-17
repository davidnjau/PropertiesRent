package com.dave.properties

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dave.properties.adapter.MyPropertiesAdapter
import com.dave.properties.helper_class.PropertyData
import com.dave.properties.helper_class.RentDuration
import com.dave.properties.helper_class.RentDurationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_view_my_properties.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ViewMyProperties : AppCompatActivity() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceRent: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_properties)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("properties")
        databaseReferenceRent = database.getReference("properties_rents")

        layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)



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

        uid?.let {
            databaseReference.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val recyclerBinPojoList = ArrayList<PropertyData?>()


                    for (childDataSnapshot in dataSnapshot.children) {

                        val propertyDataValues = childDataSnapshot.getValue(PropertyData::class.java)
                        recyclerBinPojoList.add(propertyDataValues)

                    }

                    val routesHistoryAdapter = MyPropertiesAdapter(recyclerBinPojoList, this@ViewMyProperties)
                    recyclerView.adapter = routesHistoryAdapter


                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    }

    private suspend fun getRentAmounts(it: String, propertyKey: String) {

        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {

            databaseReferenceRent.child(it).child(propertyKey).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val rentDurationList = ArrayList<RentDuration?>()
                    for (rentSnapshot in snapshot.children){

                        val propertyRentDataValues = rentSnapshot.getValue(RentDurationData::class.java)
                        val duration = propertyRentDataValues?.duration.toString()
                        val rentAmount = propertyRentDataValues?.rentAmount.toString()

                        val rentDuration = RentDuration(duration, rentAmount)
                        rentDurationList.add(rentDuration)

                    }

                    Log.e("-*-*- ", rentDurationList.toString())




                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

        }.join()

    }
}