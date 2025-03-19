package com.library.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_stat")
public class DailyStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query")
    private String query;

    @Column(name = "eventDateTime")
    private LocalDateTime eventDateTime;

    public DailyStat(String query, LocalDateTime eventDateTime) {
        this.query = query;
        this.eventDateTime = eventDateTime;
    }
}
