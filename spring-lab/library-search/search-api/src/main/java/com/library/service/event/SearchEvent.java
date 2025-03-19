package com.library.service.event;

import java.time.LocalDateTime;

public record SearchEvent(String query, LocalDateTime timeStamp) {
}
