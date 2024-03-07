package com.teamsparta.moamoa.exception

data class ReviewDeleteException(
    var reviewId: Long,
) :
    RuntimeException(
            "Deleted review with ID $reviewId cannot be updated.",
        )
