package org.example.graphqlprac.domain.user;

import org.example.graphqlprac.domain.lecture.Lecture;
import org.example.graphqlprac.domain.lecture.LectureResponse;
import org.example.graphqlprac.domain.lecture.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    public UserService(UserRepository userRepository, LectureRepository lectureRepository) {
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
    }

    public User register(UserInput request) {
        return userRepository.save(User.create(request));
    }

    public User enrollLecture(String userId, String lectureId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found with id: " + lectureId));

        user.enroll(lectureId);
        return userRepository.save(user);
    }

    public List<LectureResponse> getEnrolledLectures(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return lectureRepository.findAllByIdIn(user.getEnrolledLectureIds()).stream().map(LectureResponse::from).toList();
    }
}
