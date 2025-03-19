package com.library.repository;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;

public interface BookRepository {
    PageResult<SearchResponse> search(String query, int page, int size);
}
