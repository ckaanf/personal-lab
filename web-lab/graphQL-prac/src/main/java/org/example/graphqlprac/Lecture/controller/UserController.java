package org.example.graphqlprac.Lecture.controller;

import org.example.graphqlprac.Lecture.domain.User;
import org.example.graphqlprac.Lecture.dto.output.LectureResponse;
import org.example.graphqlprac.Lecture.dto.request.UserInput;
import org.example.graphqlprac.Lecture.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<LectureResponse> userLectures(@Argument String userId) {
        return userService.getEnrolledLectures(userId);
    }

    @MutationMapping
    public User registerUser(@Argument("input") UserInput request) {
        return userService.register(request);
    }

    @MutationMapping
    public User enrollUserToLecture(@Argument String userId, @Argument String lectureId) {
        return userService.enrollLecture(userId, lectureId);
    }
}
