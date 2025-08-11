package org.example.graphqlprac.domain.lecture;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureStats {
    private String title;
    private long enrolledCount;

    public LectureStats(String title, long enrolledCount) {
        this.title = title;
        this.enrolledCount = enrolledCount;
    }
}
