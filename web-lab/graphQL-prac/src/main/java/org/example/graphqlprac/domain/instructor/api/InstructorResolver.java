package org.example.graphqlprac.domain.instructor.api;

import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.example.graphqlprac.domain.instructor.model.Instructor;
import org.example.graphqlprac.domain.instructor.service.InstructorService;
import org.example.graphqlprac.domain.lecture.model.LectureResponse;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InstructorResolver {
    private final InstructorService instructorService;

    @SchemaMapping(typeName = "LectureResponse", field = "instructor")
    public CompletableFuture<Instructor> instructor(LectureResponse lecture, DataFetchingEnvironment env) {
        DataLoader<String, Instructor> dataLoader = env.getDataLoader("instructorDataLoader");
        return dataLoader.load(lecture.getInstructorId());
    }

    @BatchMapping
    public Map<LectureResponse, Instructor> instructor(List<LectureResponse> lectures) {
        return instructorService.mapLecturesToInstructors(lectures);
    }
}
