package com.teamsparta.moamoa.domain.user.controller

import com.teamsparta.moamoa.domain.user.dto.UserResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignInRequest
import com.teamsparta.moamoa.domain.user.dto.UserSignInResponse
import com.teamsparta.moamoa.domain.user.dto.UserSignUpRequest
import com.teamsparta.moamoa.domain.user.service.UserService
import com.teamsparta.moamoa.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController (
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun userSignUp(@RequestBody userSignUpRequest: UserSignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.userSignUp(userSignUpRequest))
    }

    @PostMapping("/signin")
    fun userSignIn(@RequestBody userSignInRequest: UserSignInRequest): ResponseEntity<UserSignInResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.userSignIn(userSignInRequest))
    }

    @DeleteMapping
    fun userDelete(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(userService.userDelete(userPrincipal.id))
    }

}