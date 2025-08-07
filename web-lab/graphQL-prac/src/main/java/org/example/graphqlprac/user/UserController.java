package org.example.graphqlprac.user;

import graphql.GraphQLContext;
import org.example.graphqlprac.lecture.LectureResponse;
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

    @QueryMapping
    public List<LectureResponse> myLectures(GraphQLContext context) {
        String userId = context.get("userId");
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

    @MutationMapping
    public User enrollMyselfToLecture(@Argument String lectureId, GraphQLContext contenxt) {
        String userId = contenxt.get("userId");
        return userService.enrollLecture(userId, lectureId);
    }
}
