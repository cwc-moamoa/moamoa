package com.teamsparta.moamoa.domain.socialUser

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SocialUserController {

    @GetMapping("/oauth2/login/kakao")
    fun getKakaoPage(): String {
        val kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?f6656ea06fd4e1e2e928fb3b5f406b48" +
                "&redirect_uri=http://localhost:8080/oauth2/callback/kakao" +
                "&response_type=code" +
                "&scope=account_email,profile"

        return "redirect:$kakaoLoginUrl"
    }



}