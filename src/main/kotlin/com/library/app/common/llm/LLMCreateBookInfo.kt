package com.library.app.common.llm

class LLMCreateBookInfo(
    val title: String,
    val plot: String,
    val genres: List<String>,
    val contents: String,
) {
}