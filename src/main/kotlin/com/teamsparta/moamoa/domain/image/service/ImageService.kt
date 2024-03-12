package com.teamsparta.moamoa.domain.image.service

import org.springframework.web.multipart.MultipartFile


interface ImageService {

    fun imageUpload (multipartFile: MultipartFile): String

}