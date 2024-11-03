package com.study.batchquiz

import org.springframework.stereotype.Component

@Component
class CsvProcessor {

    fun processor(): (Map<String, String>) -> Map<String, String> {
        return { item ->
            val currentMonth = item["value"]?.toDoubleOrNull() ?: 0.0
            val previousMonth = 100.0 // 실제 이전 월 값

            val changeRate = if (previousMonth != 0.0) {
                ((currentMonth - previousMonth) / previousMonth) * 100
            } else {
                0.0
            }

            item + ("changeRate" to String.format("%.2f", changeRate))
        }
    }
}
