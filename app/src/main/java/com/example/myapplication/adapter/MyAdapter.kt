package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.CurrencyData
import java.util.Locale

// 1. 定義你的資料
class MyAdapter(private val currencyData: List<CurrencyData>, private val onItemClickListener: OnItemClickListener? = null) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // 定義點擊事件介面
    interface OnItemClickListener {
        fun onItemClick(position: Int, item: CurrencyData)
        fun onItemLongClick(position: Int, item: CurrencyData)
    }

    // 2. 定義 ViewHolder，它會持有每個清單項目的 View
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flagText: TextView = itemView.findViewById(R.id.textView_flag)
        val currencyCodeText: TextView = itemView.findViewById(R.id.textView_currency_code)
        val currencyNameText: TextView = itemView.findViewById(R.id.textView_currency_name)
        val rateText: TextView = itemView.findViewById(R.id.textView_rate)
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
        val currency = currencyData[position]
        holder.flagText.text = currency.flag
        holder.currencyCodeText.text = currency.currencyCode
        holder.currencyNameText.text = currency.currencyName
        // 計算反向匯率 (1/rate)
        val reverseRate = 1.0 / currency.rate
        holder.rateText.text = String.format(Locale.getDefault(), "%.4f/%.2f", currency.rate, reverseRate)

        // 設置點擊監聽器
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, currency)
        }

        // 設置長按監聽器
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.onItemLongClick(position, currency)
            true
        }
    }

    // 5. 告訴 RecyclerView 有多少個項目
    override fun getItemCount(): Int {
        return currencyData.size
    }
}
