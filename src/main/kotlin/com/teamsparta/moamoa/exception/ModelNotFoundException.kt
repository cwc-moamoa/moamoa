package com.teamsparta.moamoa.exception

data class ModelNotFoundException(val modelName: String, val id: Any?) :
    RuntimeException("존재하지 않거나 삭제된 $modelName id : $id")
