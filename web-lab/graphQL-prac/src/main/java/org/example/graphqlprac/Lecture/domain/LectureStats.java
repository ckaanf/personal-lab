package org.example.graphqlprac.Lecture.domain;

public class LectureStats {
    private String title;
    private long enrolledCount;

    public LectureStats(String title, long enrolledCount) {
        this.title = title;
        this.enrolledCount = enrolledCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(long enrolledCount) {
        this.enrolledCount = enrolledCount;
    }
}
