package org.example.graphqlprac.Lecture;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @QueryMapping
    public List<Lecture> lectures() {
        return lectureService.getAll();
    }

    @MutationMapping
    public Lecture addLecture(@Argument String title, @Argument String description) {
        return lectureService.add(title, description);
    }

    @MutationMapping
    public Lecture enrollLecture(@Argument String id) {
        return lectureService.enroll(id);
    }
}
