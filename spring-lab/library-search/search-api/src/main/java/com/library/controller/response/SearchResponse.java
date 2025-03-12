package com.library.controller.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SearchResponse(String title, String author, String publisher, LocalDate pubDate, String isbn) {
}
