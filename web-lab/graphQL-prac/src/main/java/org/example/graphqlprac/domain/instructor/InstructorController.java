package org.example.graphqlprac.domain.instructor;

import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.example.graphqlprac.domain.lecture.Lecture;
import org.example.graphqlprac.domain.lecture.LectureResponse;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

//    @SchemaMapping(typeName = "LectureResponse", field = "instructor")
//    public Instructor instructor(LectureResponse lecture, DataFetchingEnvironment env) {
//        return instructorService.findById(lecture.getInstructorId());
//    }

    @BatchMapping
    public Map<LectureResponse, Instructor> instructor(List<LectureResponse> lectures) {
        List<String> instructorIds = lectures.stream().map(LectureResponse::getInstructorId).toList();

        List<Instructor> instructors = instructorService.findAllByIds(instructorIds);
        Map<String, Instructor> instructorMap = instructors.stream()
                .collect(Collectors.toMap(Instructor::getId, inst -> inst));

        return lectures.stream()
                .collect(Collectors.toMap(lec -> lec,
                        lec -> instructorMap.get(lec.getInstructorId())));
    }
}
