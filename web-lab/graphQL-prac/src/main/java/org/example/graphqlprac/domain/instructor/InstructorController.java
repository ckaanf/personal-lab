package org.example.graphqlprac.domain.instructor;

import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.example.graphqlprac.domain.lecture.LectureResponse;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @SchemaMapping(typeName = "LectureResponse", field = "instructor")
    public Instructor instructor(LectureResponse lecture, DataFetchingEnvironment env) {
        return instructorService.findById(lecture.getInstructorId());
    }
}
