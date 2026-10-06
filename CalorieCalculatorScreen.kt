package com.caloout.calculator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caloout.calculator.model.ActivityLevel
import com.caloout.calculator.model.CalorieResult
import com.caloout.calculator.model.Gender
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieCalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalorieViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "CaloOut",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Tính Calo Out (TDEE & BMR) Chuẩn Khoa Học",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        viewModel.reset()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Đặt lại",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card 1: Chọn giới tính sinh học
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Giới tính sinh học",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    GenderSelector(
                        selectedGender = uiState.gender,
                        onGenderSelected = { viewModel.onGenderChanged(it) }
                    )
                }
            }

            // Card 2: Nhập số đo cơ thể (Tuổi, Chiều cao, Cân nặng)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Thông tin chỉ số cơ thể",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Tuổi
                    OutlinedTextField(
                        value = uiState.ageInput,
                        onValueChange = { viewModel.onAgeChanged(it) },
                        label = { Text("Độ tuổi") },
                        placeholder = { Text("VD: 25") },
                        suffix = { Text("tuổi") },
                        leadingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = null)
                        },
                        isError = uiState.ageError != null,
                        supportingText = {
                            uiState.ageError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            } ?: Text("Hợp lệ: 10 - 100 tuổi")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chiều cao
                    OutlinedTextField(
                        value = uiState.heightInput,
                        onValueChange = { viewModel.onHeightChanged(it) },
                        label = { Text("Chiều cao") },
                        placeholder = { Text("VD: 172") },
                        suffix = { Text("cm") },
                        leadingIcon = {
                            Icon(Icons.Default.Height, contentDescription = null)
                        },
                        isError = uiState.heightError != null,
                        supportingText = {
                            uiState.heightError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            } ?: Text("Hợp lệ: 100 - 250 cm")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cân nặng
                    OutlinedTextField(
                        value = uiState.weightInput,
                        onValueChange = { viewModel.onWeightChanged(it) },
                        label = { Text("Cân nặng") },
                        placeholder = { Text("VD: 68.5") },
                        suffix = { Text("kg") },
                        leadingIcon = {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null)
                        },
                        isError = uiState.weightError != null,
                        supportingText = {
                            uiState.weightError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            } ?: Text("Hợp lệ: 30 - 250 kg")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.calculateTdee()
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Card 3: Mức độ hoạt động (Activity Level)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Mức độ hoạt động thể chất (Hệ số R)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Chọn mức độ phản ánh đúng nhất tuần sinh hoạt gần đây của bạn",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ActivityLevelSelector(
                        selectedLevel = uiState.activityLevel,
                        onLevelSelected = { viewModel.onActivityLevelChanged(it) }
                    )
                }
            }

            // Button Tính Toán
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.calculateTdee()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tính Calo Out (TDEE)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Kết quả hiển thị (Animated Visibility)
            AnimatedVisibility(
                visible = uiState.result != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut()
            ) {
                uiState.result?.let { result ->
                    ResultSection(result = result)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Segmented button chọn giới tính Nam / Nữ
 */
@Composable
fun GenderSelector(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Gender.entries.forEach { gender ->
            val isSelected = gender == selectedGender
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Transparent
            }
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onGenderSelected(gender) },
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (gender == Gender.MALE) Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = gender.displayName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor
                    )
                }
            }
        }
    }
}

/**
 * Danh sách thẻ chọn mức độ vận động có mô tả chi tiết & thẻ R-multiplier
 */
@Composable
fun ActivityLevelSelector(
    selectedLevel: ActivityLevel,
    onLevelSelected: (ActivityLevel) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.animateContentSize()
    ) {
        ActivityLevel.entries.forEach { level ->
            val isSelected = level == selectedLevel
            val borderStroke = if (isSelected) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            }
            val containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surface
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onLevelSelected(level) },
                shape = RoundedCornerShape(14.dp),
                color = containerColor,
                border = borderStroke
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onLevelSelected(level) },
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = level.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = level.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Phần hiển thị kết quả BMR, TDEE và các mục tiêu dinh dưỡng
 */
@Composable
fun ResultSection(result: CalorieResult) {
    val formatter = remember { DecimalFormat("#,###") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = "Kết quả tiêu hao năng lượng",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Card Nổi bật: TDEE (Calorie Out)
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "TDEE - Calorie Out Mỗi Ngày",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatter.format(result.tdeeRounded)}",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Kcal / ngày",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tổng năng lượng cơ thể bạn đốt cháy bao gồm sinh hoạt, làm việc và vận động thể thao.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                )
            }
        }

        // Card Phụ: BMR
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BMR (Trao đổi chất cơ bản)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Mức calo tối thiểu để tim đập, thở và duy trì nội tạng khi nằm nghỉ cả ngày.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${formatter.format(result.bmrRounded)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Kcal/ngày",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Tiêu đề khuyến nghị theo mục tiêu
        Text(
            text = "Lộ trình dinh dưỡng theo mục tiêu",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // 3 Cards cho mục tiêu: Duy trì, Giảm cân, Tăng cân
        GoalCard(
            title = "Duy trì cân nặng (Maintain)",
            tag = "Cân bằng năng lượng",
            calories = formatter.format(result.maintainKcal),
            description = "Nạp lượng calo đúng bằng TDEE để giữ nguyên vóc dáng hiện tại.",
            icon = Icons.Default.Balance,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )

        GoalCard(
            title = "Giảm cân / Giảm mỡ (Deficit)",
            tag = "Thâm hụt -500 kcal (~0.5 kg/tuần)",
            calories = formatter.format(result.weightLossKcal),
            description = "Tạo thâm hụt năng lượng chuẩn an toàn không gây mất cơ hay hạ thấp trao đổi chất.",
            icon = Icons.AutoMirrored.Filled.TrendingDown,
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
            badgeColor = MaterialTheme.colorScheme.error
        )

        GoalCard(
            title = "Tăng cân / Tăng cơ (Surplus)",
            tag = "Thặng dư +300 kcal (Lean Bulk)",
            calories = formatter.format(result.weightGainKcal),
            description = "Thặng dư calo vừa phải kết hợp tập kháng lực giúp tối đa hóa cơ bắp, hạn chế tích mỡ thừa.",
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f),
            badgeColor = MaterialTheme.colorScheme.tertiary
        )

        // Card Chú thích khoa học
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Lưu ý khoa học: Mifflin-St Jeor là công thức có độ chính xác cao nhất được công nhận bởi Học viện Dinh dưỡng và Ăn kiêng Hoa Kỳ. Hãy theo dõi cân nặng trung bình hàng tuần và điều chỉnh ±100-200 kcal khi cơ thể thích nghi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * Component thẻ mục tiêu dinh dưỡng cá nhân
 */
@Composable
fun GoalCard(
    title: String,
    tag: String,
    calories: String,
    description: String,
    icon: ImageVector,
    containerColor: Color,
    badgeColor: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = calories,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " kcal",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = badgeColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = badgeColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
