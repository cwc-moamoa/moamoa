package com.teamsparta.moamoa.domain.image.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*

@Service
class ImageServiceImpl(
    private val amazonS3Client: AmazonS3Client,
) : ImageService {
    @Value("\${bucket}")
    lateinit var bucket: String

    @Value("\${dir}")
    lateinit var dir: String

    override fun imageUpload(multipartFile: MultipartFile): String {
        val fileName = UUID.randomUUID().toString() + "-" + multipartFile.originalFilename
        val objMeta = ObjectMetadata()

        val bytes = IOUtils.toByteArray(multipartFile.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        amazonS3Client.putObject(
            PutObjectRequest(bucket, dir + fileName, byteArrayIs, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead),
        )

        return amazonS3Client.getUrl(bucket, dir + fileName).toString()
    }
}
