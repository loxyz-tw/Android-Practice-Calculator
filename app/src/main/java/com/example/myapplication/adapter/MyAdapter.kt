package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

// 1. 定義你的資料
class MyAdapter(private val myData: List<String>, private val onItemClickListener: OnItemClickListener? = null) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // 定義點擊事件介面
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: String)
        fun onItemLongClick(position: Int, item: String)
    }

    // 2. 定義 ViewHolder，它會持有每個清單項目的 View
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView_title)
    }

    // 3. 創建 ViewHolder
    // 每次需要一個新的清單項目 View 時，這個方法會被呼叫
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    // 4. 將資料綁定到 ViewHolder
    // 當清單項目 View 準備好時，這個方法會被呼叫，將資料填入到 View 中
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myData[position]

        // 設置點擊監聽器
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, myData[position])
        }

        // 設置長按監聽器
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.onItemLongClick(position, myData[position])
            true
        }
    }

    // 5. 告訴 RecyclerView 有多少個項目
    override fun getItemCount(): Int {
        return myData.size
    }
}
