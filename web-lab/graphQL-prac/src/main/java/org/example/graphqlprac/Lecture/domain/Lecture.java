package org.example.graphqlprac.Lecture.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lectures")
public class Lecture {
    @Id
    private String id;
    private String title;
    private String description;
    private boolean enrolled;

    private Lecture(String title, String description, boolean enrolled) {
        this.title = title;
        this.description = description;
        this.enrolled = enrolled;
    }

    public static Lecture of(String title, String description, boolean enrolled) {
        return new Lecture(title, description, enrolled);
    }

    public void activeEnrolled() {
        this.enrolled = true;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnrolled() {
        return enrolled;
    }
}
