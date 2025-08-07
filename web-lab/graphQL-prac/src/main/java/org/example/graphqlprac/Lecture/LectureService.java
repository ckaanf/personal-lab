package org.example.graphqlprac.Lecture;

import org.example.graphqlprac.Lecture.output.LectureResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<LectureResponse> getAll() {
        return lectureRepository.findAll().stream().map(LectureResponse::from).toList();
    }

    public LectureResponse add(String title, String description) {
        Lecture lecture = Lecture.of(title, description, false);
        return LectureResponse.from(lectureRepository.save(lecture));
    }

    public LectureResponse enroll(String id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found with id: " + id));

        lecture.activeEnrolled();

        return LectureResponse.from(lectureRepository.save(lecture));
    }
}
