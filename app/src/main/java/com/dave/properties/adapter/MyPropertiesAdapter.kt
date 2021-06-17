package com.dave.properties.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dave.properties.R
import com.dave.properties.helper_class.Formatter
import com.dave.properties.helper_class.PropertyData


class MyPropertiesAdapter(
    private var propertDataList: ArrayList<PropertyData?>,
    private val context: Context
) :
    RecyclerView.Adapter<MyPropertiesAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvPropertyName : TextView = itemView.findViewById(R.id.tvPropertyName)
        val tvLocation : TextView = itemView.findViewById(R.id.tvLocation)
        val tvRentDue : TextView = itemView.findViewById(R.id.tvRentDue)
        val tvDatePast : TextView = itemView.findViewById(R.id.tvDatePast)
        val tvDateDue : TextView = itemView.findViewById(R.id.tvDateDue)

        val btnEditDetails : ImageButton = itemView.findViewById(R.id.btnEditDetails)
   

        init {

            itemView.setOnClickListener(this)
            btnEditDetails.setOnClickListener(this)


        }

        override fun onClick(v: View?) {



        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_view_properties,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        val property_name = propertDataList[position]?.property_name
        val property_location = propertDataList[position]?.property_location
        val property_rent = "${propertDataList[position]?.property_rent} Kshs"
        val rent_due_at = propertDataList[position]?.rent_due_at

        val remainingDays = Formatter().getDateDiff(rent_due_at.toString())
        val isExpired = remainingDays.isExpired
        val remDays = remainingDays.remDays
        val remHours = remainingDays.remHours

        val pastDue = "Past Due"
        var dueDate = ""

        dueDate = if (isExpired == true){

            holder.tvDateDue.visibility = View.VISIBLE
            holder.tvDatePast.visibility = View.GONE

            if (remDays < 1){
                "$remHours Hours"
            }else{
                "$remDays Days"
            }

        }else{
            holder.tvDatePast.visibility = View.VISIBLE
            holder.tvDateDue.visibility = View.GONE
            pastDue.toString()
        }



        holder.tvPropertyName.text = property_name
        holder.tvLocation.text = property_location
        holder.tvRentDue.text = property_rent

        holder.tvDateDue.text = dueDate
        holder.tvDatePast.text = dueDate


    }

    override fun getItemCount(): Int {
        return propertDataList.size
    }

}