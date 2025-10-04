package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.MyAdapter
import com.google.android.material.imageview.ShapeableImageView
import androidx.core.net.toUri

class MainActivity2 : AppCompatActivity(), MyAdapter.OnItemClickListener {

    private lateinit var profileImageView: ShapeableImageView

    // 常量定義，在 Kotlin 中通常放在 companion object 內
    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    profileImageView.setImageURI(selectedImageUri)
                    Toast.makeText(this, "成功選擇圖片！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "無法取得圖片。", Toast.LENGTH_SHORT).show()
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "取消選擇圖片。", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profileImageView = findViewById(R.id.profileImageView)
        profileImageView.setOnClickListener {
            checkAndRequestStoragePermission()
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

    /**
     * 檢查並請求讀取外部儲存空間的權限。
     */
    private fun checkAndRequestStoragePermission() {
        // 對於 Android 14 (API 34) 及以上，使用 READ_MEDIA_VISUAL_USER_SELECTED 權限
        // 對於較舊版本，使用 READ_EXTERNAL_STORAGE 權限
        val permission = if (android.os.Build.VERSION.SDK_INT >= 34) {
            "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // 權限尚未被授予，請求權限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // 權限已被授予，直接開啟圖庫
            openGallery()
        }
    }

    /**
     * 處理權限請求的結果。
     * 當使用者在權限對話框中做出選擇後，系統會呼叫此方法。
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 檢查 grantResults 是否不為空且第一個結果是授予
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 權限被授予
                Toast.makeText(this, "圖片權限已獲取，可以選擇圖片。", Toast.LENGTH_SHORT).show()
                openGallery()
            } else {
                // 權限被拒絕
                showPermissionDeniedDialog()
            }
        }
    }

    /**
     * 顯示權限被拒絕的對話框，提供使用者選項
     */
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("需要圖片權限")
            .setMessage("為了選擇圖片，應用程式需要存取您的圖片權限。請前往設定頁面開啟權限，或選擇「不再詢問」來取消操作。")
            .setPositiveButton("前往設定") { _, _ ->
                // 開啟應用程式設定頁面
                openAppSettings()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "已取消選擇圖片", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }

    /**
     * 開啟應用程式的設定頁面
     */
    private fun openAppSettings() {
        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    /**
     * 開啟系統圖庫選擇圖片。
     */
    private fun openGallery() {
        // 嘗試多種方式開啟圖片選擇器
        val intents = mutableListOf<Intent>()
        
        // 方式1: 使用 ACTION_PICK (推薦)
        val pickIntent = Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        intents.add(pickIntent)
        
        // 方式2: 使用 ACTION_GET_CONTENT (備用)
        val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
        getContentIntent.type = "image/*"
        intents.add(getContentIntent)
        
        // 尋找第一個可用的 Intent
        var selectedIntent: Intent? = null
        for (intent in intents) {
            if (intent.resolveActivity(packageManager) != null) {
                selectedIntent = intent
                break
            }
        }
        
        if (selectedIntent != null) {
            // 使用找到的 Intent 啟動圖片選擇器
            pickImageLauncher.launch(selectedIntent)
        } else {
            // 如果所有方式都失敗，顯示錯誤訊息並提供替代方案
            showNoGalleryAppDialog()
        }
    }
    
    /**
     * 當沒有找到圖片選擇器應用程式時顯示對話框
     */
    private fun showNoGalleryAppDialog() {
        AlertDialog.Builder(this)
            .setTitle("無法選擇圖片")
            .setMessage("您的裝置上沒有找到圖片選擇器應用程式。請安裝一個圖庫應用程式（如 Google Photos）或檔案管理器，然後重試。")
            .setPositiveButton("前往 Play 商店") { _, _ ->
                openPlayStore()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
    
    /**
     * 開啟 Play 商店搜尋圖庫應用程式
     */
    private fun openPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "market://search?q=gallery+photo+manager".toUri()
            startActivity(intent)
        } catch (e: Exception) {
            // 如果 Play 商店應用程式不可用，使用網頁版
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://play.google.com/store/search?q=gallery%20photo%20manager".toUri()
            startActivity(intent)
        }
    }
}