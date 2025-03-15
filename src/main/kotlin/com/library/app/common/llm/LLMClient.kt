package com.library.app.common.llm

interface LLMClient {

    suspend fun createBookInfo(): LLMCreateBookInfo
}