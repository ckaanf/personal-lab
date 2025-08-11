package org.example.graphqlprac.domain.instructor.service;

import lombok.RequiredArgsConstructor;
import org.example.graphqlprac.domain.instructor.repository.InstructorRepository;
import org.example.graphqlprac.domain.instructor.model.Instructor;
import org.example.graphqlprac.domain.lecture.model.LectureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;

   public List<Instructor> findAllByIds(Collection<String> ids) {
       return instructorRepository.findAllByIdIn(ids);
   }

   public Instructor findById(String id) {
       return instructorRepository.findById(id).orElseThrow(() -> new RuntimeException());
   }

   public List<Instructor> findAll() {
       return instructorRepository.findAll();
   }

    public Map<LectureResponse, Instructor> mapLecturesToInstructors(List<LectureResponse> lectures) {
        List<String> instructorIds = lectures.stream()
                .map(LectureResponse::getInstructorId)
                .toList();

        List<Instructor> instructors = instructorRepository.findAllByIdIn(instructorIds);

        Map<String, Instructor> instructorMap = instructors.stream()
                .collect(Collectors.toMap(Instructor::getId, inst -> inst));

        return lectures.stream()
                .collect(Collectors.toMap(lec -> lec,
                        lec -> instructorMap.get(lec.getInstructorId())));
    }
}
