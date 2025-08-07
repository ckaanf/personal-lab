package org.example.graphqlprac.lecture;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LectureRepository extends Repository<Lecture, String> {
    Lecture save(Lecture lecture);

    List<Lecture> findAll();

    Optional<Lecture> findById(String id);

    List<Lecture> findAllByIdIn(Set<String> enrolledLectureIds);
}
