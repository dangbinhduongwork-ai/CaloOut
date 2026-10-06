package com.caloout.calculator.model

import kotlin.math.roundToInt

/**
 * Giới tính sinh học dùng cho công thức Mifflin-St Jeor
 */
enum class Gender(val displayName: String) {
    MALE("Nam"),
    FEMALE("Nữ")
}

/**
 * Hệ số mức độ hoạt động thể chất (Activity Multiplier - R)
 *
 * @property multiplier Hệ số R nhân với BMR
 * @property title Tiêu đề hiển thị ngắn gọn
 * @property description Mô tả chi tiết mức độ hoạt động và số bước chân/buổi tập
 */
enum class ActivityLevel(
    val multiplier: Double,
    val title: String,
    val description: String
) {
    SEDENTARY(
        multiplier = 1.2,
        title = "Ít vận động (R = 1.2)",
        description = "Công việc bàn giấy, ít đi lại (<5.000 bước/ngày), không tập thể thao."
    ),
    LIGHT(
        multiplier = 1.375,
        title = "Vận động nhẹ (R = 1.375)",
        description = "Tập nhẹ 1-3 ngày/tuần (45-60 phút) HOẶC văn phòng + 6.000-8.000 bước/ngày."
    ),
    MODERATE(
        multiplier = 1.55,
        title = "Vận động vừa (R = 1.55)",
        description = "Tập vừa đến nặng 3-5 ngày/tuần (45-60 phút) HOẶC tập gym 6 ngày nhưng đa số thời gian ngồi."
    ),
    HIGH(
        multiplier = 1.725,
        title = "Vận động nhiều (R = 1.725)",
        description = "Tập cường độ cao 6-7 ngày/tuần (60-90 phút) HOẶC lao động chân tay (>10.000 bước/ngày)."
    ),
    VERY_HIGH(
        multiplier = 1.9,
        title = "Vận động cực nhiều (R = 1.9)",
        description = "Vận động viên chuyên nghiệp / lao động nặng nhọc + tập 2 buổi/ngày."
    )
}

/**
 * Dữ liệu kết quả tính toán BMR & TDEE cùng các khuyến nghị mức calo theo mục tiêu
 */
data class CalorieResult(
    val bmr: Double,
    val tdee: Double
) {
    val bmrRounded: Int get() = bmr.roundToInt()
    val tdeeRounded: Int get() = tdee.roundToInt()

    // Mục tiêu duy trì cân nặng
    val maintainKcal: Int get() = tdeeRounded

    // Mục tiêu giảm cân (Deficit ~500 kcal/ngày giúp giảm an toàn ~0.5kg/tuần)
    // Giới hạn cận dưới an toàn: tối thiểu 1.200 kcal/ngày
    val weightLossKcal: Int get() = (tdeeRounded - 500).coerceAtLeast(1200)

    // Mục tiêu giảm cân chậm / giữ cơ (Deficit ~250-300 kcal/ngày)
    val mildWeightLossKcal: Int get() = (tdeeRounded - 300).coerceAtLeast(1200)

    // Mục tiêu tăng cân / tăng cơ nạc (Surplus ~300 kcal/ngày)
    val weightGainKcal: Int get() = tdeeRounded + 300

    // Mục tiêu tăng cân nhanh hơn (Surplus ~500 kcal/ngày)
    val fastWeightGainKcal: Int get() = tdeeRounded + 500
}

/**
 * Bộ xử lý logic tính toán dinh dưỡng theo chuẩn Mifflin-St Jeor Formula
 */
object CalorieCalculator {

    /**
     * Tính BMR (Basal Metabolic Rate - Tỷ lệ trao đổi chất cơ bản)
     * Công thức Mifflin-St Jeor:
     * - Nam:  BMR = (10 * weight_kg) + (6.25 * height_cm) - (5 * age) + 5
     * - Nữ:   BMR = (10 * weight_kg) + (6.25 * height_cm) - (5 * age) - 161
     */
    fun calculateBMR(
        gender: Gender,
        weightKg: Double,
        heightCm: Double,
        age: Int
    ): Double {
        val baseBmr = (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age)
        return when (gender) {
            Gender.MALE -> baseBmr + 5.0
            Gender.FEMALE -> baseBmr - 161.0
        }
    }

    /**
     * Tính TDEE (Total Daily Energy Expenditure - Tổng tiêu hao năng lượng hàng ngày)
     * TDEE = BMR * R (Activity Multiplier)
     */
    fun calculateTDEE(bmr: Double, activityLevel: ActivityLevel): Double {
        return bmr * activityLevel.multiplier
    }

    /**
     * Tính toán đầy đủ kết quả Calorie
     */
    fun calculate(
        gender: Gender,
        weightKg: Double,
        heightCm: Double,
        age: Int,
        activityLevel: ActivityLevel
    ): CalorieResult {
        val bmr = calculateBMR(gender, weightKg, heightCm, age)
        val tdee = calculateTDEE(bmr, activityLevel)
        return CalorieResult(bmr = bmr, tdee = tdee)
    }
}
