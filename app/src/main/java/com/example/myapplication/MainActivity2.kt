package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.MyAdapter

class MainActivity2 : AppCompatActivity(), MyAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. 準備固定的50筆資料
        val myDataList = (1..50).map { "項目 $it" }

        // 2. 找到 RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)

        // 3. 設定 LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. 設定 Adapter
        recyclerView.adapter = MyAdapter(myDataList, this)

        val buttonCal = findViewById<Button>(R.id.buttonCal)
        buttonCal.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int, item: String) {
        Toast.makeText(this, "點擊了: $item (位置: $position)", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(position: Int, item: String) {
        AlertDialog.Builder(this)
            .setTitle("選擇操作")
            .setItems(arrayOf("編輯", "刪除")) { _, which ->
                when (which) {
                    0 -> editItem(position, item)
                    1 -> deleteItem(position, item)
                }
            }
            .show()
    }

    private fun editItem(position: Int, item: String) {
        Toast.makeText(this, "編輯: $item", Toast.LENGTH_SHORT).show()
    }

    private fun deleteItem(position: Int, item: String) {
        Toast.makeText(this, "刪除: $item", Toast.LENGTH_SHORT).show()
    }
}