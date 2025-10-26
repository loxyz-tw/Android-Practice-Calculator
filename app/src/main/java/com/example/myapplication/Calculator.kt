package com.example.myapplication

class Calculator {

    private var firstOperand: Double? = null
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

    // 輸入小數點
    fun inputDecimal() {
        // 如果顯示的是運算子結果，重新開始
        if (isNewInput) {
            display = "0."
            isNewInput = false
        } else {
            // 檢查當前數字是否已經有小數點
            val currentNumber = getCurrentNumber()
            if (!currentNumber.contains(".")) {
                display += "."
            }
        }
    }

    // 取得當前正在輸入的數字（不包含運算子）
    private fun getCurrentNumber(): String {
        return if (pendingOperator != null) {
            // 如果有待處理的運算子，取得運算子後的部分
            val operatorIndex = display.lastIndexOf(pendingOperator!!)
            if (operatorIndex != -1) {
                display.substring(operatorIndex + 1)
            } else {
                display
            }
        } else {
            display
        }
    }

    // 輸入運算子 (+, -, ×, ÷)
    fun inputOperator(operator: String) {
        // 如果已經有運算子，先計算結果
        if (pendingOperator != null && !isNewInput) {
            calculate()
        }
        
        firstOperand = display.toDoubleOrNull()
        pendingOperator = operator
        isNewInput = true
        
        // 如果顯示字串最後一個字元是運算子，替換它
        if (display.isNotEmpty() && "+-×÷".contains(display.last())) {
            display = display.dropLast(1) + operator
        } else {
            display += operator
        }
    }

    // 計算結果（?.let 寫法）
    fun calculate() {
        val secondOperand = display.toDoubleOrNull()

        val result = firstOperand?.let { first ->
            secondOperand?.let { second ->
                pendingOperator?.let { op ->
                    when (op) {
                        "+" -> first + second
                        "-" -> first - second
                        "×" -> first * second
                        "÷" -> if (second != 0.0) first / second else null
                        else -> second
                    }
                }
            }
        } ?: secondOperand   // 如果任何一個是 null，就退回顯示第二個數字

        display = result?.let { formatNumber(it) } ?: "Error"

        // Reset 狀態
        firstOperand = null
        pendingOperator = null
        isNewInput = true
    }

    // 格式化數字，最多顯示9位小數
    private fun formatNumber(number: Double): String {
        // 如果是整數，不顯示小數點
        if (number == number.toLong().toDouble()) {
            return number.toLong().toString()
        }
        
        // 格式化為最多9位小數，並移除尾隨的零
        val formatted = String.format("%.9f", number)
        return formatted.trimEnd('0').trimEnd('.')
    }

    // 清除 (C)
    fun clear() {
        display = "0"
        firstOperand = null
        pendingOperator = null
        isNewInput = true
    }
}