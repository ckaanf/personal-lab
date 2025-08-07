package org.example.graphqlprac.Lecture;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LectureService {
    private final Map<String, Lecture> lectures = new ConcurrentHashMap<>();

    public List<Lecture> getAll() {
        return new ArrayList<>(lectures.values());
    }

    public Lecture add(String title, String description) {
        String id = UUID.randomUUID().toString();
        Lecture lecture = Lecture.of(id,title, description, false);
        lectures.put(id, lecture);
        return lecture;
    }

    public Lecture enroll(String id) {
        Lecture lecture = lectures.get(id);
        if (lecture != null) {
            lecture.activeEnrolled();
        }
        return lecture;
    }
}
