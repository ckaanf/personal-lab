package org.example.graphqlprac.Lecture;

import org.example.graphqlprac.Lecture.output.LectureResponse;
import org.example.graphqlprac.Lecture.request.LectureInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @QueryMapping
    public List<LectureResponse> lectures() {
        return lectureService.getAll();
    }

    @MutationMapping
    public LectureResponse addLecture(@Argument("input") LectureInput request) {
        return lectureService.add(request.getTitle(), request.getDescription());
    }

    @MutationMapping
    public LectureResponse enrollLecture(@Argument String id) {
        return lectureService.enroll(id);
    }
}
