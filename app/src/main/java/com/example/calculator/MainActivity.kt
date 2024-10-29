package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.text.isDigitsOnly
import com.example.calculator.R.id.displayText

class MainActivity : AppCompatActivity() {

    private lateinit var displayText: TextView
    private var currentInput: String = ""
    private var operator: Char? = null
    private var result: Double = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayText = findViewById(R.id.displayText)
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonEqual, R.id.buttonClear, R.id.buttonDelete, R.id.buttonDot
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { handleButtonClick(it as Button) }
        }
    }

    private fun handleButtonClick(button: Button) {
        when (val buttonText = button.text.toString()) {
            "C" -> clear()
            "⌫" -> deleteLast()
            "=" -> calculateResult()
            "÷" -> processOperator('/')
            "×" -> processOperator('*')
            "-" -> processOperator('-')
            "+" -> processOperator('+')
            "." -> appendDecimal()
            else -> if (buttonText.isDigitsOnly()) appendNumber(buttonText)
        }
    }

    private fun clear() {
        currentInput = ""
        operator = null
        result = 0.0
        isNewOperation = true
        displayText.text = "0"
    }

    private fun deleteLast() {
        currentInput = if (currentInput.isNotEmpty()) currentInput.dropLast(1) else ""
        displayText.text = if (currentInput.isNotEmpty()) currentInput else "0"
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = ""
            isNewOperation = false
        }
        currentInput += number
        displayText.text = currentInput
    }

    private fun appendDecimal() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            displayText.text = currentInput
        }
    }

    private fun processOperator(selectedOperator: Char) {
        if (currentInput.isNotEmpty()) {
            if (operator != null) {
                calculateIntermediateResult()
            } else {
                result = currentInput.toDouble()
            }
            operator = selectedOperator
            isNewOperation = true
        }
    }

    private fun calculateIntermediateResult() {
        val secondNumber = currentInput.toDouble()
        result = when (operator) {
            '+' -> result + secondNumber
            '-' -> result - secondNumber
            '*' -> result * secondNumber
            '/' -> if (secondNumber != 0.0) result / secondNumber else 0.0
            else -> result
        }
        displayText.text = result.toString()
    }

    private fun calculateResult() {
        if (operator != null && currentInput.isNotEmpty()) {
            calculateIntermediateResult()
            operator = null
            isNewOperation = true
            currentInput = result.toString()
        }
    }
}
