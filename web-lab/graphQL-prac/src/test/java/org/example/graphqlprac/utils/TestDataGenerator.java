package org.example.graphqlprac.utils;

import org.example.graphqlprac.domain.instructor.Instructor;
import org.example.graphqlprac.domain.instructor.InstructorRepository;
import org.example.graphqlprac.domain.lecture.Lecture;
import org.example.graphqlprac.domain.lecture.LectureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class TestDataGenerator {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Test
    public void generateLargeData() {
        List<Instructor> instructors = IntStream.range(0, 100)
                .mapToObj(i -> Instructor.of("Instructor " + i, "instructor" + i + "@example.com"))
                .collect(Collectors.toList());

        for (Instructor i : instructors) {
            instructorRepository.save(i);
        }

        List<Instructor> savedInstructors = instructorRepository.findAll();

        Random random = new Random();
        List<Lecture> lectures = IntStream.range(0, 10000)
                .mapToObj(i -> Lecture.of(
                        "Lecture " + i,
                        "Description " + i,
                        true,
                        savedInstructors.get(random.nextInt(savedInstructors.size())).getId()
                ))
                .collect(Collectors.toList());
        for (Lecture l : lectures) {
            lectureRepository.save(l);
        }

        System.out.println("완료");
    }
}