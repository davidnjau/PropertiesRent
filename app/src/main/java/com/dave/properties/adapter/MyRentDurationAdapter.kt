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
import com.dave.properties.helper_class.RentDuration


class MyRentDurationAdapter(
    private var rentDurationList: ArrayList<RentDuration>,
    private val context: Context
) :
    RecyclerView.Adapter<MyRentDurationAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvDuration : TextView = itemView.findViewById(R.id.tvDuration)
        val tvRentAmount : TextView = itemView.findViewById(R.id.tvRentAmount)


        val btnRemoveRentDuration : ImageButton = itemView.findViewById(R.id.btnRemoveRentDuration)
   

        init {

//            itemView.setOnClickListener(this)
            btnRemoveRentDuration.setOnClickListener(this)


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
                R.layout.card_view_rent_duration,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        val duration = rentDurationList[position].duration
        val rentAmount = "${rentDurationList[position].rentAmount} Kshs"

        holder.tvDuration.text = duration
        holder.tvRentAmount.text = rentAmount



    }

    override fun getItemCount(): Int {
        return rentDurationList.size
    }

}