package collection.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImmutableMain {

    public static void main(String[] args) {
        //불변 리스트 생성
        List<Integer> list = List.of(1, 2, 3);

        //가변 리스트
        ArrayList<Integer> mutableList = new ArrayList<>(list);
        mutableList.add(4);
        System.out.println("mutableList = " + mutableList);
        System.out.println("mutableList class = " + mutableList.getClass());

        //불변 리스트 전환
        List<Integer> unmodifiableList = Collections.unmodifiableList(mutableList);
        System.out.println("unmodifiableList class = " + unmodifiableList.getClass());

        //예외 발생 java.lang.UnsupportedOperationException
        // unmodifiableList.add(5);
    }
}