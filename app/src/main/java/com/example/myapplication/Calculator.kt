package com.example.myapplication

class Calculator {

    private var firstOperand: Int? = null
    private var pendingOperator: String? = null
    private var isNewInput: Boolean = true

    // 用來放在顯示區域的 String
    var display: String = "0"
        private set

    // 輸入數字
    fun inputNumber(number: String) {
        if (isNewInput || display == "0") {
            display = number
        } else {
            display += number
        }
        isNewInput = false
    }

    // 輸入運算子 (+, -, ×, ÷)
    fun inputOperator(operator: String) {
        firstOperand = display.toIntOrNull()
        pendingOperator = operator
        isNewInput = true
    }

    // 計算結果（?.let 寫法）
    fun calculate() {
        val secondOperand = display.toIntOrNull()

        val result = firstOperand?.let { first ->
            secondOperand?.let { second ->
                pendingOperator?.let { op ->
                    when (op) {
                        "+" -> first + second
                        "-" -> first - second
                        "×" -> first * second
                        "÷" -> if (second != 0) first / second else null
                        else -> second
                    }
                }
            }
        } ?: secondOperand   // 如果任何一個是 null，就退回顯示第二個數字

        display = result?.toString() ?: "Error"

        // Reset 狀態
        firstOperand = null
        pendingOperator = null
        isNewInput = true
    }

    // 清除 (C)
    fun clear() {
        display = "0"
        firstOperand = null
        pendingOperator = null
        isNewInput = true
    }
}