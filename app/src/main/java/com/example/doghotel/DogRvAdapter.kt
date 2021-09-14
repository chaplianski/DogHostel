package com.example.doghotel

import android.app.Application
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeUnit
import android.media.Image
import android.os.Build
import android.os.SystemClock
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


class DogRvAdapter (dogContext: Context, val dogs: ArrayList<Dog>):
    RecyclerView.Adapter<DogRvAdapter.ViewHolder>(){

    val dogContext = dogContext
    private var dogList = emptyList<Dog>()

   lateinit var dListner: onClickDogListner

    interface onClickDogListner {
        fun onItemClick(position: Int)
    }

    fun setOnClickDogListner (listner: onClickDogListner) {
        dListner = listner
    }


    class ViewHolder (itemView: View, listner: onClickDogListner) : RecyclerView.ViewHolder(itemView){
       //     var tvDodDelete: TextView = itemView.findViewById(R.id.bt_dog_card_delete)
            var tvItemDogNickname: TextView = itemView.findViewById(R.id.tv_item_dog_nickname)

                //itemView.findViewById<View>(R.id.tv_dog_card_nickname).toString()
            var tvItemDogGender: TextView = itemView.findViewById(R.id.tv_item_dog_gender)
            var tvItemDogAge: TextView = itemView.findViewById(R.id.tv_item_dog_age)
            var tvItemDogDays: TextView = itemView.findViewById(R.id.tv_item_dog_days)
            var tvItemDogCage: TextView = itemView.findViewById(R.id.tv_item_dog_cage)
            var ivItemDogPhoto: ImageView = itemView.findViewById(R.id.iv_item_dog_photo)
        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogRvAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dog_rv_item, parent, false)
        return ViewHolder(v, dListner)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: DogRvAdapter.ViewHolder, position: Int) {
        val dog: Dog = dogs[position]
        holder.tvItemDogNickname.text = dog.nickname
        holder.tvItemDogGender.text = dog.gender


        holder.tvItemDogAge.text = dog.age.toString()
      //  holder.tvItemDogDays.text = dog.days.toString()
        holder.tvItemDogCage.text = dog.cage.toString()

        val currentTime = Date().time
        var days = ((currentTime - dog.days)/(1000*60*60*24)).toString().toInt()
        Log.d("MyLog", "days:  ${days}")
        if (days < 1) days = 1
        holder.tvItemDogDays.text = days.toString()
        Log.d("MyLog", "Current Time:  ${currentTime}")
        Log.d("MyLog", "Begin Time:  ${dog.days}")


        Glide.with(dogContext).load(dog.photo)
            .error(R.drawable.ic_avatar_dog)
            .centerCrop()
        //    .crossFade()
            .placeholder(R.drawable.ic_avatar_dog)
            .into(holder.ivItemDogPhoto);


     //   Log.d ("MyLog", currentTime.toString())
     //   Log.d ("MyLog", days.toString())

     //   Log.d ("MyLog", dog.days.toString())

        //  holder.itemView.setOnClickListener(listnerDogItem(dog))

      /*  fun deleteHolderItem (){
            if(MainActivity.dbHelper.deleteDog(dog.dogID)){
                dogs.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, dogs.size)


            }
        } */
    }

    override fun getItemCount(): Int {
        return dogs.size
    }

    fun setData (dog: List<Dog>){
        this.dogList = dog
        notifyDataSetChanged()
    }
   // public fun daysBetween( startDate: Calendar, endDate: Calendar): Long {
 //       val end: Long = endDate.timeInMillis
 //       val start = startDate.timeInMillis
  //      return java.util.concurrent.TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
  //  }



}