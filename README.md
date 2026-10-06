# 📱 CaloOut - Ứng Dụng Tính Calorie Out (TDEE & BMR) Chuẩn Khoa Học

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.0-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20(M3)-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20StateFlow-orange.svg?style=flat)]()
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-blue.svg)]()
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34%2B-green.svg)]()

**CaloOut** là ứng dụng Android native được phát triển hoàn toàn bằng **Kotlin** và **Jetpack Compose (Material Design 3)**, hỗ trợ tính toán chính xác tổng mức tiêu hao năng lượng hàng ngày (**TDEE - Total Daily Energy Expenditure**) và tỷ lệ trao đổi chất cơ bản (**BMR - Basal Metabolic Rate**) dựa trên công thức dinh dưỡng khoa học chuẩn xác nhất hiện nay (**Mifflin-St Jeor**).

---

## 🌟 Tính Năng Nổi Bật

* 🔬 **Công thức chuẩn y khoa Mifflin-St Jeor**: Tính toán BMR với độ chính xác cao nhất được công nhận bởi Hiệp hội Dinh dưỡng Hoa Kỳ.
* 🏃 **Phân loại 5 cấp độ vận động (Hệ số R từ 1.2 đến 1.9)**: Có mô tả chi tiết số buổi tập/tuần và số bước chân/ngày để người dùng chọn chuẩn xác nhất.
* 🎯 **Đề xuất calo theo 3 mục tiêu vóc dáng**:
  * ⚖️ **Duy trì cân nặng (Maintain)**: Nạp đúng bằng mức TDEE.
  * 📉 **Giảm cân / Giảm mỡ an toàn (Deficit)**: Thâm hụt `-500 kcal/ngày` (giảm an toàn ~0.5kg mỡ/tuần, có chốt an toàn không nạp dưới 1.200 kcal).
  * 📈 **Tăng cân / Tăng cơ nạc (Surplus)**: Thặng dư `+300 kcal/ngày` tối ưu cho việc xây dựng cơ bắp (Lean Bulk).
* 🎨 **Giao diện Material 3 hiện đại (Card-based UI)**: Hỗ trợ tự động chuyển đổi Light Theme / Dark Theme, hiệu ứng chuyển cảnh mượt mà và kiểm tra hợp lệ dữ liệu (Validation) tức thì.
* 🇻🇳 **Ngôn ngữ thuần Việt**: Thân thiện, dễ hiểu, phù hợp với mọi người dùng.

---

## 📐 Công Thức Tính Toán

### 1. BMR (Mifflin-St Jeor Formula)
* **Nam giới**:  
  $$\text{BMR} = (10 \times \text{Cân nặng [kg]}) + (6.25 \times \text{Chiều cao [cm]}) - (5 \times \text{Tuổi}) + 5$$
* **Nữ giới**:  
  $$\text{BMR} = (10 \times \text{Cân nặng [kg]}) + (6.25 \times \text{Chiều cao [cm]}) - (5 \times \text{Tuổi}) - 161$$

### 2. TDEE (Calorie Out)
$$\text{TDEE} = \text{BMR} \times \text{Hệ số hoạt động (R)}$$

| Cấp độ vận động | Hệ số R | Mô tả sinh hoạt |
| :--- | :---: | :--- |
| **Ít vận động** | `1.2` | Dân văn phòng, ít đi lại (<5.000 bước/ngày), không tập thể thao. |
| **Vận động nhẹ** | `1.375` | Tập nhẹ 1-3 ngày/tuần (45-60p) HOẶC đi bộ 6.000-8.000 bước/ngày. |
| **Vận động vừa** | `1.55` | Tập vừa 3-5 ngày/tuần (45-60p) HOẶC gym 6 ngày nhưng đa số ngồi làm việc. |
| **Vận động nhiều** | `1.725` | Tập cường độ cao 6-7 ngày/tuần HOẶC công việc lao động tay chân (>10.000 bước/ngày). |
| **Vận động cực nhiều** | `1.9` | Vận động viên chuyên nghiệp / lao động nặng nhọc + tập 2 buổi/ngày. |

---

## 🏗️ Cấu Trúc Dự Án & Công Nghệ

Dự án tuân thủ nghiêm ngặt mô hình kiến trúc **MVVM (Model - View - ViewModel)**:

```text
CaloOut/
├── .github/workflows/
│   └── build_apk.yml                     # CI/CD tự động build APK trên GitHub Actions
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml           # Khai báo cấu hình ứng dụng
│   │   ├── java/com/caloout/calculator/
│   │   │   ├── MainActivity.kt           # Activity chính & cấu hình Theme M3
│   │   │   ├── model/
│   │   │   │   └── CalorieCalculatorModel.kt  # Logic công thức toán & Entity
│   │   │   └── ui/
│   │   │       ├── CalorieViewModel.kt        # StateFlow & Validation form
│   │   │       └── CalorieCalculatorScreen.kt # UI Components bằng Jetpack Compose
│   │   └── res/                          # Vector Icons, Colors, Themes
│   └── build.gradle.kts                  # Cấu hình dependencies cho module app
├── build_apk.bat                         # Script 1-click build APK cho Windows
├── gradlew & gradlew.bat                 # Gradle Wrapper
└── settings.gradle.kts
```

---

## 🚀 Hướng Dẫn Tải & Cài Đặt File APK

### Cách 1: Tải file APK trực tiếp từ GitHub Actions (Khuyên dùng)
1. Truy cập tab **Actions**: [https://github.com/dangbinhduongwork-ai/CaloOut/actions](https://github.com/dangbinhduongwork-ai/CaloOut/actions)
2. Chọn build mới nhất và cuộn xuống mục **Artifacts**.
3. Tải file **`CaloOut-App-Debug`** về máy và giải nén lấy file `.apk` cài lên điện thoại Android.

### Cách 2: Tự build file APK trên máy tính
* **Bằng 1-Click (Windows)**: Nháy đúp chuột vào file [`build_apk.bat`](build_apk.bat).
* **Bằng Android Studio**: Mở project ➔ Chọn `Build` ➔ `Build Bundle(s) / APK(s)` ➔ `Build APK(s)`.
* **Bằng Dòng lệnh**:
  ```bash
  ./gradlew assembleDebug
  ```
  File APK xuất xưởng sẽ nằm tại: `app/build/outputs/apk/debug/app-debug.apk`.

---

## 📄 License
Phát triển bởi [dangbinhduongwork-ai](https://github.com/dangbinhduongwork-ai). Phân phối theo giấy phép MIT.
