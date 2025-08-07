package org.example.graphqlprac.Lecture.domain;

import org.example.graphqlprac.Lecture.dto.request.UserInput;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private Set<String> enrolledLectureIds = new HashSet<>();

    public User(String name) {
        this.name = name;
    }

    public static User create(UserInput request) {
        return new User(request.getName());
    }

    public void enroll(String lectureId) {
        this.enrolledLectureIds.add(lectureId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getEnrolledLectureIds() {
        return enrolledLectureIds;
    }

    public void setEnrolledLectureIds(Set<String> enrolledLectureIds) {
        this.enrolledLectureIds = enrolledLectureIds;
    }
}
