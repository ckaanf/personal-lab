package org.example.graphqlprac.domain.instructor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "instructors")
public class Instructor {
    @Id
    private String id;
    private String name;
    private String email;

    private Instructor(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static Instructor of(String name, String email) {
        return new Instructor(name, email);
    }
}
