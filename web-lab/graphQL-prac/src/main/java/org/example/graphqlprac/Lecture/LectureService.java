package org.example.graphqlprac.Lecture;

import org.example.graphqlprac.Lecture.output.LectureResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LectureService {
    private final Map<String, Lecture> lectures = new ConcurrentHashMap<>();

    public List<LectureResponse> getAll() {
        return lectures.values().stream().map(LectureResponse::from).toList();
    }

    public LectureResponse add(String title, String description) {
        String id = UUID.randomUUID().toString();
        Lecture lecture = Lecture.of(id, title, description, false);
        lectures.put(id, lecture);
        return LectureResponse.from(lecture);
    }

    public LectureResponse enroll(String id) {
        Lecture lecture = lectures.get(id);
        if (lecture != null) {
            lecture.activeEnrolled();
        }
        return LectureResponse.from(lecture);
    }
}
