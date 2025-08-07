package org.example.graphqlprac.Lecture;

public class Lecture {
    private String id;
    private String title;
    private String description;
    private boolean enrolled;

    private Lecture(String id, String title, String description, boolean enrolled) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.enrolled = enrolled;
    }

    public static Lecture of(String id, String title, String description, boolean enrolled) {
        return new Lecture(id, title, description, enrolled);
    }

    public void activeEnrolled() {
        this.enrolled = true;
    }
}
