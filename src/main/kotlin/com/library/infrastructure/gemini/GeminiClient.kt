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
            아래는 키워드 예시야 ! 이런 키워드를 조합해서 책을 만들어줘 ! 여기 없는 키워드여도 괜찮아 
            (마법, 드래곤, 이세계, 기사단, 마법학교, 전설의 무기, 운명, 신화, 대륙 전쟁, 인공지능, 시간여행, 우주선, 클론, 평행세계, 유전자 조작, 지구 종말, 가상현실, 첫사랑, 썸, 고백, 재회, 연애 계약, 금지된 사랑, 삼각관계, 츤데레, 운명적인 만남, 용의자, 단서, 반전, 추격, 경찰, 증거, 트릭, 목격자, 타임 리밋, 명탐정, 닫힌 방, 밀실, 실종, 죽음의 편지, 과거의 비밀, 유산, 금지된 방, 귀신, 악령, 저주, 빙의, 제물, 꿈속의 괴물, 불 꺼진 집, 끊긴 전화, 우정, 졸업, 첫 연애, 진로, 가족과 갈등, 교내 사건, 학교폭력, 사춘기, 자아찾기, 위로, 나답게, 혼자만의 시간, 감정기록, 관계의 온도, 치유, 소소한 행복, 목표 달성, 시간 관리, 마인드셋, 돈, 루틴, 퍼스널 브랜딩, 스타트업, 창업, 제국, 혁명, 전쟁, 황제, 장군, 민초의 삶, 국난 극복, 문화유산, 삶의 의미, 자아, 공감, 무의식, 불안, 행복론, 선택, 내면의 대화, 상실, 사랑과 이별, 가족의 서사, 일상의 비극, 관계의 균열, 기억, 노년, 계절, 그리움, 바람, 노을, 사랑의 파편, 짧은 말 긴 울림, 검객, 내공, 사파, 무림, 절정고수, 비급, 복수, 혈전, 강호, 회귀, 빙의, 주인공 성장, 먼치킨, 숨겨진 혈통, 후회남, 여주각성, 랭킹, 파티, 로그라이크, 스킬트리, 전설템, PvP, 레이드, 버그, 승부욕, 노력, 팀워크, 리더십, 부상, 역전승, 멘탈, 경기장의 함성, 낯선 도시, 문화충격, 여행자의 시선, 로컬푸드, 길 위에서, 모험, 프로그래머 , 코딩, 직장인)
            
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