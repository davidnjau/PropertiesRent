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
import com.dave.properties.helper_class.RentDurationData


class MyRentDurationViewAdapter(
    private var rentDurationList: ArrayList<RentDurationData?>,
    private val context: Context
) :
    RecyclerView.Adapter<MyRentDurationViewAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvDuration : TextView = itemView.findViewById(R.id.tvDuration)
        val tvRentAmount : TextView = itemView.findViewById(R.id.tvRentAmount)




        init {

//            itemView.setOnClickListener(this)


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
                R.layout.card_view_rent_duration1,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        val duration = rentDurationList[position]?.duration
        val rentAmount = "${rentDurationList[position]?.rentAmount} Kshs"

        holder.tvDuration.text = duration
        holder.tvRentAmount.text = rentAmount



    }

    override fun getItemCount(): Int {
        return rentDurationList.size
    }

}