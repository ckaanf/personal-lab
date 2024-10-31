package collection.map.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MovieManager {
    public static void main(String[] args) {
        Map<String, String> movieMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("영화 제목을 입력하세요 (종료는 'q'): ");
            String title = scanner.nextLine();

            if (title.equals("q")) {
                break;
            }

            System.out.print("영화 감독을 입력하세요: ");
            String director = scanner.nextLine();

            movieMap.put(title, director);
        }

        while (true) {
            System.out.print("영화 목록을 확인할 감독을 입력하세요 (종료는 'q'): ");
            String director = scanner.nextLine();

            if (director.equals("q")) {
                break;
            }

            List<String> movieList = new ArrayList<>();
            for (Map.Entry<String, String> entry : movieMap.entrySet()) {
                if (entry.getValue().equals(director)) {
                    movieList.add(entry.getKey());
                }
            }

            if (movieList.isEmpty()) {
                System.out.println(director + " 감독이 제작한 영화가 없습니다.");
            } else {
                System.out.println(director + " 감독이 제작한 영화 목록: " + movieList);
            }
        }
    }
}