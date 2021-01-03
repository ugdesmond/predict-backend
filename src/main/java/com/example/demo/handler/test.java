package com.example.demo.handler;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();

        integerList.add(1);
        integerList.add(1);
        integerList.add(1);
        integerList.add(1);
        integerList.add(2);
        integerList.add(2);
        integerList.add(2);
        integerList.add(3);
        integerList.add(3);
        integerList.add(3);
        integerList.add(4);
        integerList.add(4);
        integerList.add(4);
        integerList.add(5);
        integerList.add(5);
        integerList.add(5);
        integerList.add(6);
        integerList.add(6);
        integerList.add(6);
        integerList.add(7);
        integerList.add(7);
        peformMagic((integerList));
    }


    public static void peformMagic(List<Integer> test) {


        int oneTest = checkIfExceedsOne(test);
        int tenTest = checkIfExceedsTen(test);
        int totalTest = totalTest(test);
        int total = oneTest + tenTest + totalTest;
        System.out.println("values====" + total);

    }

    public static int checkIfExceedsOne(List<Integer> test) {
        int count = 0;
        int truncatedCount = 0;
        for (int x : test) {

            if (x <= 1) {
                count++;
                if (count > 3) {
                    truncatedCount++;
                }
            }


        }


        return truncatedCount;
    }

    public static int checkIfExceedsTen(List<Integer> test) {
        int upperCase = 0;
        int lowerCase = 0;
        int count = 0;
        int truncatedCount = 0;
        for (int x : test) {

            if (x <= 10) {
                count++;
                if (count > 20) {
                    truncatedCount++;
                }
            } else {
                upperCase = x;
                lowerCase = x - 9;
                truncatedCount += checkIfWithingRange(lowerCase, upperCase, test);

            }


        }


        return truncatedCount;
    }

    public static int checkIfWithingRange(int lowerCase, int upperCase, List<Integer> check) {

        int count = 0;
        int truncatedCount = 0;
        for (int x : check) {

            if (x >= lowerCase && x <= upperCase) {
                count++;
                if (count > 20) {
                    truncatedCount++;
                }
            }


        }

        return truncatedCount;
    }

    public static int totalTest(List<Integer> test) {
        int arrayLength = test.size();
        if (arrayLength > 60) {
            return arrayLength - 60;
        }

        return 0;
    }

}


