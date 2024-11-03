package com.study.batchquiz

import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Component

@Component
class CsvWriter {

    fun writer(): FlatFileItemWriter<Map<String, String>> {
        return FlatFileItemWriterBuilder<Map<String, String>>()
            .name("csvItemWriter")
            .resource(FileSystemResource("output.csv"))
            .delimited()
            .delimiter(",")
            .names("month", "value", "changeRate")
            .build()
    }
}
