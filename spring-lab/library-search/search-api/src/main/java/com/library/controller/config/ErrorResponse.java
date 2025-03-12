package com.library.controller.config;

import com.library.ErrorType;

public record ErrorResponse(String errorMessage, ErrorType errorType) {}
