package com.example.doghotel

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doghotel.model.Dog


class DogRvAdapter (dogContext: Context, val dogs: ArrayList<Dog>):
    RecyclerView.Adapter<DogRvAdapter.ViewHolder>(){

 //   val dogContext = dogContext
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
        //    var tvItemDogDays: TextView = itemView.findViewById(R.id.tv_item_days)

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

    override fun onBindViewHolder(holder: DogRvAdapter.ViewHolder, position: Int) {
        val dog: Dog = dogs[position]
        holder.tvItemDogNickname.text = dog.nickname
        holder.tvItemDogGender.text = dog.gender
        holder.tvItemDogAge.text = dog.age.toString()
     //   val currentTime = SystemClock.elapsedRealtime()
     //   var days = (currentTime - dog.days)/(1000*60*60*24)
     //   if (days < 1) days = 1
    //    holder.tvItemDogDays.text = days.toString()


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


}