package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculatorActivity : AppCompatActivity() {

    private lateinit var calculator: Calculator
    private lateinit var displayText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        calculator = Calculator()
        displayText = findViewById(R.id.textview_display)

        // 數字按鈕
        val numberButtons = listOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
        )

        for (id in numberButtons) {
            val button = findViewById<Button>(id)
            button.setOnClickListener {
                calculator.inputNumber(button.text.toString())
                updateDisplay()
            }
        }

        // 運算子
        findViewById<Button>(R.id.button_add).setOnClickListener { onOperatorClicked("+") }
        findViewById<Button>(R.id.button_sub).setOnClickListener { onOperatorClicked("-") }
        findViewById<Button>(R.id.button_mul).setOnClickListener { onOperatorClicked("×") }
        findViewById<Button>(R.id.button_div).setOnClickListener { onOperatorClicked("÷") }
        findViewById<Button>(R.id.button_point).setOnClickListener {
            calculator.inputDecimal()
            updateDisplay()
        }

        // 等號
        findViewById<Button>(R.id.button_equal).setOnClickListener {
            calculator.calculate()
            updateDisplay()
        }

        // 清除
        findViewById<Button>(R.id.button_clear).setOnClickListener {
            calculator.clear()
            updateDisplay()
        }

        // 初始化顯示
        updateDisplay()
    }

    private fun updateDisplay() {
        displayText.text = calculator.display
    }

    private fun onOperatorClicked(op: String) {
        calculator.inputOperator(op)
        updateDisplay()
    }

}