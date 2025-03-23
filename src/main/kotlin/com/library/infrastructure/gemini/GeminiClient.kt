package com.library.infrastructure.gemini

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.app.common.llm.LLMClient
import com.library.app.common.llm.LLMCreateBookInfo
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class GeminiClient(
    @Value("\${llm.api.key}")
    private val apiKey: String,
    private val objectMapper: ObjectMapper
) : LLMClient {

    override suspend fun createBookInfo(): LLMCreateBookInfo {
        val webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}")
            .defaultHeader("Content-Type", "application/json")
            .build()

        val requestBody = """
            다음 정보를 사용하여 가짜 책 정보를 생성하고, JSON 형식으로 반환해주세요.
            (이전에 만들어 줬던 책 말고 다른 책으로 해야하고 , 직전에 알려줬던 책과는 장르와 , 책 제목 , 책 키워드 등이 아예 다르게 만들어줘)
            (최대한 다양한 키워드를 조합해서 다양한 책을 만들어줘 겹치는 내용 없이 최대한 다양한 책을 만드는게 나의 목표야)
            
            - 책 제목: 임의의 제목
            - 장르 : 최대 4개의 장르 (예: 판타지, SF, 자기 계발 등등)
            - 줄거리 : 간략한 줄거리 (최대 100자)
            - 책 내용: 책 내용의 일부 (최대한 1000자를 맞춰줘 , 기승전결에 맞게 내용이 마무리 돼야해)

            JSON 형식 예시: 
            
            {
              "title": "황금의 도시",
              "genres": ["판타지", "모험"],
              "plot": "잃어버린 황금 도시를 찾는 용감한 탐험가들의 이야기.",
              "content": "깊은 숲 속에서 고대 유적을 발견한 탐험대는..."
            }
             
        """.trimIndent()

        val request = mapOf("contents" to listOf(mapOf("parts" to listOf(mapOf("text" to requestBody)))))

        val response = webClient
            .post()
            .bodyValue(request)
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .awaitSingle()

        val jsonText = extractJsonText(response)

        return objectMapper.readValue(jsonText, LLMCreateBookInfo::class.java)
    }

    private fun extractJsonText(response: Map<String, Any>): String {
        val candidates = response["candidates"] as? List<Map<String, Any>> ?: error("Invalid response format")
        val content = candidates.firstOrNull()?.get("content") as? Map<*, *> ?: error("Missing content")
        val parts = content["parts"] as? List<Map<*, *>> ?: error("Missing parts")
        val text = parts.firstOrNull()?.get("text") as? String ?: error("Missing text")

        return text
            .replace("```json", "") // JSON 시작 부분 제거
            .replace("```", "") // JSON 끝 부분 제거
            .trim() // 양쪽 공백 제거
    }
}