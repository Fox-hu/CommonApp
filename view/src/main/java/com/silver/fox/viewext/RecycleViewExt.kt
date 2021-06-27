package com.silver.fox.viewext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silver.fox.view.R

/**
 * @Author fox
 * @Date 2020/4/3 20:11
 */

class NormalAdapter(private val mDatas: List<String>) :
    RecyclerView.Adapter<NormalAdapter.VH?>() {
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById<View>(R.id.card_text) as TextView

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.title.text = mDatas[position]
        holder.itemView.setOnClickListener { }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.view_item_card, parent,
            false
        )
        return VH(v)
    }
}

fun RecyclerView.createTest(context: Context) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.isSmoothScrollbarEnabled = true
    setLayoutManager(layoutManager)
    setHasFixedSize(true)
    adapter = NormalAdapter(listOf<String>("a", "b", "c", "d", "e", "f", "g", "h", "i"))
}

