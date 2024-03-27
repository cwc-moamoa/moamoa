package com.teamsparta.moamoa.exception

data class ModelNotFoundException(val modelName: String, val id: Long?) :
    RuntimeException("존재하지 않는 $modelName id : $id")
