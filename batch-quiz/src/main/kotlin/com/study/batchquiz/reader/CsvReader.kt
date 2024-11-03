package com.study.batchquiz.reader

import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class CsvReader {

    companion object {
        const val FILE_NAME = "경기종합지수_2020100__10차__20241103203906.csv"
    }

    fun reader(): FlatFileItemReader<Map<String, String>> {
        return FlatFileItemReaderBuilder<Map<String, String>>()
            .name("csvItemReader")
            .resource(ClassPathResource(FILE_NAME))
            .encoding("EUC-KR")
            .linesToSkip(1)
            .delimited()
            .delimiter(DelimitedLineTokenizer.DELIMITER_COMMA)
            .names("month", "value")
            .fieldSetMapper { it ->
                mapOf(
                    "month" to it.readString("month"),
                    "value" to it.readString("value")
                )
            }
            .build()
    }
}
