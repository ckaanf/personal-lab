package org.example.graphqlprac.domain.lecture;

import lombok.RequiredArgsConstructor;
import org.example.graphqlprac.domain.instructor.Instructor;
import org.example.graphqlprac.domain.instructor.InstructorService;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final InstructorService instructorService;


    public List<LectureResponse> getAll() {
        return lectureRepository.findAll().stream().map(LectureResponse::from).toList();
//        return lectureRepository.findAll();
    }

    public LectureResponse add(String title, String description) {
        Lecture lecture = Lecture.of(title, description, false, null);
        return LectureResponse.from(lectureRepository.save(lecture));
    }

    public LectureResponse enroll(String id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found with id: " + id));

        lecture.activeEnrolled();

        return LectureResponse.from(lectureRepository.save(lecture));
    }

    @BatchMapping
    public Map<Lecture, Instructor> batchInstructor(List<Lecture> lectures) {
        Set<String> instructorIds = lectures.stream().map(Lecture::getInstructorId).collect(Collectors.toSet());

        List<Instructor> instructors = instructorService.findAllByIds(instructorIds);
        Map<String, Instructor> instructorMap = instructors.stream()
                .collect(Collectors.toMap(Instructor::getId, instructor -> instructor));

        return lectures.stream()
                .collect(Collectors.toMap(l -> l, l -> instructorMap.get(l.getInstructorId())));
    }
}
