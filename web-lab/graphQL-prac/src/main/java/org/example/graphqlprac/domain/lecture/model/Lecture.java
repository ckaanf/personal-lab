package org.example.graphqlprac.domain.lecture.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "lectures")
public class Lecture {
    @Id
    private String id;
    private String title;
    private String description;
    private boolean enrolled;
    private String instructorId;

    private Lecture(String title, String description, boolean enrolled, String instructorId) {
        this.title = title;
        this.description = description;
        this.enrolled = enrolled;
        this.instructorId = instructorId;
    }

    public static Lecture of(String title, String description, boolean enrolled, String instructorId) {
        return new Lecture(title, description, enrolled, instructorId);
    }

    public void activeEnrolled() {
        this.enrolled = true;
    }

    public boolean isEnrolled() {
        return enrolled;
    }
}
