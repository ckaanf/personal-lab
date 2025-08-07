package org.example.graphqlprac.user;

public class UserInput {
    private String name;

    private UserInput(String name) {
        this.name = name;
    }

    public static UserInput create(String name) {
        return new UserInput(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

