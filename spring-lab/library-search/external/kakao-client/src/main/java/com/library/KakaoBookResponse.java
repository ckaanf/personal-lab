package com.library;

import java.util.List;

public record KakaoBookResponse(
        List<Document> documents,
        Meta meta
) {

}
