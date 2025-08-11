package org.example.graphqlprac.domain.instructor.repository;

import org.example.graphqlprac.domain.instructor.model.Instructor;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends Repository<Instructor, String> {
    Instructor save(Instructor lecture);

    Optional<Instructor> findById(String id);

    List<Instructor> findAll();
    List<Instructor> findAllByIdIn(Collection<String> ids);

}
