package org.example.graphqlprac.Lecture.dto.output;

import org.example.graphqlprac.Lecture.domain.Lecture;

public class LectureResponse {
    private String id;
    private String title;
    private String description;
    private boolean enrolled;

    private LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.enrolled = lecture.isEnrolled();
    }

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(lecture);
    }
}


