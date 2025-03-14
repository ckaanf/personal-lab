package com.library.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Schema(description = "도서 검색 결과")
@Builder
public record SearchResponse(
        @Schema(description = "제목", example = "HTTP 완벽 가이드")
        String title,
        @Schema(description = "작가", example = "데이빗 고울리")
        String author,
        @Schema(description = "출판사", example = "인사이트")
        String publisher,
        @Schema(description = "출판일", example = "2015-01-01")
        LocalDate pubDate,
        @Schema(description = "isbn 번호", example = "9788966261208")
        String isbn) {
}
