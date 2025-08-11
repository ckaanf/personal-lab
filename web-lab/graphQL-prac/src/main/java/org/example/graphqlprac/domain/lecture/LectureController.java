package org.example.graphqlprac.domain.lecture;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LectureController {
    private final LectureService lectureService;
    private final LectureStatService lectureStatService;

    public LectureController(LectureService lectureService, LectureStatService lectureStatService) {
        this.lectureService = lectureService;
        this.lectureStatService = lectureStatService;
    }

    @QueryMapping
    public List<LectureResponse> lectures() {
        return lectureService.getAll();
    }

    @QueryMapping
    public List<LectureStats> lectureStats() {
        return lectureStatService.getEnrollmentStats();
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
