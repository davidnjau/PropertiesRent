package com.dave.properties.helper_class

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dave.properties.MainActivity
import com.dave.properties.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Formatter {

    fun getDateDiff(dateExpire: String): DaysRemaining {

        val saveResult = DaysRemaining()
        try {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateEx = dateFormat.parse(dateExpire.substring(0, dateExpire.length))
            val currentTime = Calendar.getInstance().time

            assert(dateEx != null)
            val diffInMillies = dateEx!!.time - currentTime.time
            val seconds = diffInMillies / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            saveResult.isExpired = hours >= 1
            saveResult.remDays = days
            saveResult.remHours = hours

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return saveResult

    }

    fun createNotification(
        context: Context) {

        val mAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("properties")

        val NOTIFICATION_ID = "1".toInt()
        val CHANNEL_ID = "my_channel_01"
        val name: CharSequence = "uptech"

        val details = "Rent Due Notification."

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = details
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            //            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }

        val broadcastIntent = Intent(context, MainActivity::class.java)
        val actionIntent = PendingIntent.getBroadcast(
            context,
            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

        builder.setSmallIcon(R.mipmap.ic_launcher_icon)

        builder.setContentTitle(details)

        val currentUser = mAuth.currentUser
        if (currentUser != null) {

            val uid = currentUser.uid
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val propertyDataList = ArrayList<String?>()

                    var propertyName = ""
                    var propertyAmount = ""
                    var dueDate = ""

                    for (childDataSnapshot in dataSnapshot.children) {

                        val propertyData = childDataSnapshot.getValue(PropertyData::class.java)

                        val propertyDate = propertyData?.rent_due_at
                        val remainingDays = Formatter().getDateDiff(propertyDate.toString())
                        val isExpired = remainingDays.isExpired
                        val remDays = remainingDays.remDays
                        val remHours = remainingDays.remHours

                        if (isExpired == true){

                            propertyName = propertyData?.property_name.toString()
                            propertyAmount =  "${propertyData?.property_rent} Khs"

                            if (isExpired == true){
                                dueDate = if (remDays < 1){
                                    "$remHours Hours"
                                }else{
                                    "$remDays Days"
                                }
                            }


                            propertyDataList.add("Your rent for property $propertyName of amount $propertyAmount is due in $dueDate.")



                        }

                    }

                    var propertyData = ""
                    for (items in propertyDataList) {

                        propertyData += "$items \n"

                    }


                    builder.setContentText(propertyData)
                    builder.setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(propertyData)
                    )

                    notificationManager.notify(NOTIFICATION_ID, builder.build())


                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }



    }



}