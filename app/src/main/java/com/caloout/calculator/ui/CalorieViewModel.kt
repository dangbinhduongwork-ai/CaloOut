package com.caloout.calculator.ui

import androidx.lifecycle.ViewModel
import com.caloout.calculator.model.ActivityLevel
import com.caloout.calculator.model.CalorieCalculator
import com.caloout.calculator.model.CalorieResult
import com.caloout.calculator.model.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CalorieUiState(
    val gender: Gender = Gender.MALE,
    val ageInput: String = "",
    val heightInput: String = "",
    val weightInput: String = "",
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val ageError: String? = null,
    val heightError: String? = null,
    val weightError: String? = null,
    val result: CalorieResult? = null
)

class CalorieViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalorieUiState())
    val uiState: StateFlow<CalorieUiState> = _uiState.asStateFlow()

    fun onGenderChanged(newGender: Gender) {
        _uiState.update { it.copy(gender = newGender) }
    }

    fun onAgeChanged(newAge: String) {
        val filtered = newAge.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                ageInput = filtered,
                ageError = validateAge(filtered, isInitialOrTyping = true)
            )
        }
    }

    fun onHeightChanged(newHeight: String) {
        val filtered = filterDecimalInput(newHeight)
        _uiState.update {
            it.copy(
                heightInput = filtered,
                heightError = validateHeight(filtered, isInitialOrTyping = true)
            )
        }
    }

    fun onWeightChanged(newWeight: String) {
        val filtered = filterDecimalInput(newWeight)
        _uiState.update {
            it.copy(
                weightInput = filtered,
                weightError = validateWeight(filtered, isInitialOrTyping = true)
            )
        }
    }

    fun onActivityLevelChanged(newLevel: ActivityLevel) {
        _uiState.update { it.copy(activityLevel = newLevel) }
    }

    fun calculateTdee() {
        val currentState = _uiState.value

        val ageError = validateAge(currentState.ageInput, isInitialOrTyping = false)
        val heightError = validateHeight(currentState.heightInput, isInitialOrTyping = false)
        val weightError = validateWeight(currentState.weightInput, isInitialOrTyping = false)

        val hasError = ageError != null || heightError != null || weightError != null

        if (hasError) {
            _uiState.update {
                it.copy(
                    ageError = ageError,
                    heightError = heightError,
                    weightError = weightError,
                    result = null
                )
            }
            return
        }

        val age = currentState.ageInput.toInt()
        val height = currentState.heightInput.toDouble()
        val weight = currentState.weightInput.toDouble()

        val calculationResult = CalorieCalculator.calculate(
            gender = currentState.gender,
            weightKg = weight,
            heightCm = height,
            age = age,
            activityLevel = currentState.activityLevel
        )

        _uiState.update {
            it.copy(
                ageError = null,
                heightError = null,
                weightError = null,
                result = calculationResult
            )
        }
    }

    fun reset() {
        _uiState.value = CalorieUiState()
    }

    private fun validateAge(input: String, isInitialOrTyping: Boolean): String? {
        if (input.isBlank()) return if (isInitialOrTyping) null else "Vui lòng nhập tuổi"
        val age = input.toIntOrNull() ?: return "Độ tuổi không hợp lệ"
        if (age < 10 || age > 100) return "Tuổi phải từ 10 đến 100"
        return null
    }

    private fun validateHeight(input: String, isInitialOrTyping: Boolean): String? {
        if (input.isBlank()) return if (isInitialOrTyping) null else "Vui lòng nhập chiều cao"
        val height = input.toDoubleOrNull() ?: return "Chiều cao không hợp lệ"
        if (height < 100.0 || height > 250.0) return "Chiều cao phải từ 100 đến 250 cm"
        return null
    }

    private fun validateWeight(input: String, isInitialOrTyping: Boolean): String? {
        if (input.isBlank()) return if (isInitialOrTyping) null else "Vui lòng nhập cân nặng"
        val weight = input.toDoubleOrNull() ?: return "Cân nặng không hợp lệ"
        if (weight < 30.0 || weight > 250.0) return "Cân nặng phải từ 30 đến 250 kg"
        return null
    }

    private fun filterDecimalInput(input: String): String {
        val clean = input.replace(',', '.')
        val sb = StringBuilder()
        var hasDot = false
        for (char in clean) {
            if (char.isDigit()) {
                sb.append(char)
            } else if (char == '.' && !hasDot) {
                sb.append(char)
                hasDot = true
            }
        }
        return sb.toString()
    }
}
