package com.study.kopringbatch.batchsmaple.jobs.jdbc.config

import com.study.kopringbatch.domain.Customer
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource

class CustomerItemSqlParameterSourceProvider : ItemSqlParameterSourceProvider<Customer> {
    override fun createSqlParameterSource(item: Customer): SqlParameterSource {
        return BeanPropertySqlParameterSource(item)
    }
}
