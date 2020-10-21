package ru.otus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class DIYarrayListTest {

    private DIYarrayList<Integer> diYarrayList;

    @BeforeEach
    void setUp() {
        diYarrayList = new DIYarrayList<>();
    }

    @Test
    void addTest() {
        boolean expectedAddAllFunctionResult = true;
        boolean actualAddAllFunctionResult = Collections.addAll(diYarrayList, 1,2,3);

        Assertions.assertEquals(expectedAddAllFunctionResult, actualAddAllFunctionResult);
    }
}