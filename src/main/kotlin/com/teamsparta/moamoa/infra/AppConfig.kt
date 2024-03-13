package com.teamsparta.moamoa.infra

import com.siot.IamportRestClient.IamportClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
    @Value("\${iamport.apiKey}") private val apiKey: String,
    @Value("\${iamport.secretKey}") private val secretKey: String,
) {
    @Bean
    fun iamportClient(): IamportClient {
        return IamportClient(apiKey, secretKey)
    }
}
