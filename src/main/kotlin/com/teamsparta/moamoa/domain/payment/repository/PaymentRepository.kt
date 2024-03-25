package com.teamsparta.moamoa.domain.payment.repository

import com.teamsparta.moamoa.domain.payment.model.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<PaymentEntity, Long> {}
