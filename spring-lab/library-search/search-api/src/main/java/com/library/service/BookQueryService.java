package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookQueryService {
    private final BookRepository bookRepository;

    public PageResult<SearchResponse> search(String query, int page, int size) {
        return bookRepository.search(query, page, size);
    }
}
