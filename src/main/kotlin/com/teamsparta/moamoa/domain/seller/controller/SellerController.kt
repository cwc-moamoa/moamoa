package com.teamsparta.moamoa.domain.seller.controller

import com.teamsparta.moamoa.domain.seller.dto.SellerResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInRequest
import com.teamsparta.moamoa.domain.seller.dto.SellerSignInResponse
import com.teamsparta.moamoa.domain.seller.dto.SellerSignUpRequest
import com.teamsparta.moamoa.domain.seller.service.SellerService
import com.teamsparta.moamoa.infra.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sellers")
class SellerController(
    private val sellerService: SellerService,
) {
    @PostMapping("/signup")
    fun signUpSeller(
        @Valid @RequestBody sellerSignUpRequest: SellerSignUpRequest,
    ): ResponseEntity<SellerResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(sellerService.signUpSeller(sellerSignUpRequest))
    }

    @PostMapping("/signin")
    fun signInSeller(
        @RequestBody sellerSignInRequest: SellerSignInRequest,
    ): ResponseEntity<SellerSignInResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(sellerService.signInSeller(sellerSignInRequest))
    }

    @DeleteMapping
    fun deleteSeller(
        @AuthenticationPrincipal sellerPrincipal: UserPrincipal,
    ): ResponseEntity<SellerResponse> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(sellerService.deleteSeller(sellerPrincipal.id))
    }
}
