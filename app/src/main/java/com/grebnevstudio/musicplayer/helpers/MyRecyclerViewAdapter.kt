package com.grebnevstudio.musicplayer.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val myDataset: LiveData<List<String>>, private val itemXmlLayout: Int) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(itemXmlLayout, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset.value?.get(position)
    }

    override fun getItemCount() = myDataset.value?.size ?: 0
}

class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
