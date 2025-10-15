package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val username = "roy"
    val password = "123456"
    private lateinit var sharedPreferences: SharedPreferences
    val PREFS_NAME = "MyAppPreferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editText1 = findViewById<EditText>(R.id.editText1)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val checkbox = findViewById<com.google.android.material.checkbox.MaterialCheckBox>(R.id.checkbox)
        // 1. 確保取出的值絕對不是 null
        val usernameSaved = sharedPreferences.getString("username", "") ?: ""
        // 2. 檢查是否為空字串並設定
        if (usernameSaved.isNotEmpty()) {
            editText1.setText(usernameSaved)
            checkbox.isChecked = true
        }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            // Handle button click event
            if (editText1.text.toString() == username && editText2.text.toString() == password) {
                if (checkbox.isChecked) {
                    sharedPreferences.edit().apply {
                        putString("username", username)
                        apply()
                    }
                } else {
                    sharedPreferences.edit().apply {
                        remove("username")
                        apply()
                    }
                }
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                Toast.makeText(this, "登入成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "使用者名稱或密碼錯誤", Toast.LENGTH_LONG).show()
            }

        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            // 顯示功能尚未完成的對話框
            showRegistrationNotAvailableDialog()
        }

    }

    /**
     * 顯示註冊功能尚未完成的對話框
     */
    private fun showRegistrationNotAvailableDialog() {
        AlertDialog.Builder(this)
            .setTitle("功能開發中")
            .setMessage("註冊功能目前正在開發中，敬請期待！\n\n我們將在未來的版本中提供完整的註冊功能。")
            .setPositiveButton("我知道了") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true) // 允許點擊外部區域關閉
            .show()
    }

}