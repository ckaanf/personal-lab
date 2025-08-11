package org.example.graphqlprac.domain.lecture.model;

import lombok.Getter;

@Getter
public class LectureResponse {
    private String id;
    private String title;
    private String description;
    private boolean enrolled;
    private String instructorId;

    private LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.enrolled = lecture.isEnrolled();
        this.instructorId = lecture.getInstructorId();
    }

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(lecture);
    }
}


