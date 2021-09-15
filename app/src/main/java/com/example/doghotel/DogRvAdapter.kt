package com.example.doghotel

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doghotel.model.Dog
import java.util.*
import kotlin.collections.ArrayList


class DogRvAdapter (dogContext: Context, private val dogs: ArrayList<Dog>):
    RecyclerView.Adapter<DogRvAdapter.ViewHolder>(){

    private val dogContext = dogContext

   private lateinit var dListener: onClickDogListner

    interface onClickDogListner {
        fun onItemClick(position: Int)
    }

    fun setOnClickDogListener (listener: onClickDogListner) {
        dListener = listener
    }

    class ViewHolder (itemView: View, listener: onClickDogListner) : RecyclerView.ViewHolder(itemView){

            var tvItemDogNickname: TextView = itemView.findViewById(R.id.tv_item_dog_nickname)
            var tvItemDogGender: TextView = itemView.findViewById(R.id.tv_item_dog_gender)
            var tvItemDogAge: TextView = itemView.findViewById(R.id.tv_item_dog_age)
            var tvItemDogDays: TextView = itemView.findViewById(R.id.tv_item_dog_days)
            var tvItemDogCage: TextView = itemView.findViewById(R.id.tv_item_dog_cage)
            var ivItemDogPhoto: ImageView = itemView.findViewById(R.id.iv_item_dog_photo)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dog_rv_item, parent, false)
        return ViewHolder(v, dListener)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dog: Dog = dogs[position]
        holder.tvItemDogNickname.text = dog.nickname
        holder.tvItemDogGender.text = dog.gender
        holder.tvItemDogAge.text = dog.age.toString()
        holder.tvItemDogCage.text = dog.cage.toString()

        val currentTime = Date().time.toInt()
        var days = ((currentTime - dog.days)/(1000*60*60*24)).toString().toInt()
        if (days < 1) days = 1
        holder.tvItemDogDays.text = days.toString()

        Glide.with(dogContext).load(dog.photo)
            .error(R.drawable.ic_avatar_dog)
            .centerCrop()
            .placeholder(R.drawable.ic_avatar_dog)
            .into(holder.ivItemDogPhoto)
    }

    override fun getItemCount(): Int {
        return dogs.size
    }
}