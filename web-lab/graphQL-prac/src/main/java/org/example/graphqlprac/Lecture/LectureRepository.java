package org.example.graphqlprac.Lecture;

import org.example.graphqlprac.Lecture.output.LectureResponse;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends Repository<Lecture, String> {
    Lecture save(Lecture lecture);

    List<Lecture> findAll();

    Optional<Lecture> findById(String id);
}
