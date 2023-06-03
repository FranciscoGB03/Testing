package com.luv2code.junitdemo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
//@TestMethodOrder(MethodOrderer.MethodName.class) ordena por nombre de metodo
//@TestMethodOrder(MethodOrderer.DisplayName.class) ordena por displayname
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach(){
        demoUtils=new DemoUtils();
    }

    @Test
    @DisplayName("Equals and Not Equals")
    @Order(1)
    //void test_Equals_And_Not_Equals(){
    void testEqualsAndNotEquals(){
        //System.out.println("Running test: testEqualsAndNotEquals");                
        assertEquals(6, demoUtils.add(2, 4),"2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9),"1+9 must not be 6");
    }

    @Test
    @DisplayName("Null and Not Null")
    @Order(0)
    //void test_Null_And_Not_Null(){
    void testNullAndNotNull(){
        //System.out.println("Running test: testNullAndNotNull");
        String str1 = null;
        String str2 = "luv2code";
        
        assertNull(demoUtils.checkNull(str1),"Object should be null");
        assertNotNull(demoUtils.checkNull(str2),"Object should not be null");
    }
    
    @DisplayName("Same and Not Same")
    @Test    
    void testSameAndNotSame(){
        String str="luv2code";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Objects should refer to same object");
        assertNotSame(str, demoUtils.getAcademy(),"Objects should not refer to same object");
    }

    @DisplayName("True and False")
    @Test
    void testTrueFalse(){
     int gradeOne=10;
     int gradeTwo=5;

     assertTrue(demoUtils.isGreater(gradeOne, gradeTwo),"This should return true");
     assertFalse(demoUtils.isGreater(gradeTwo, gradeOne), "This should return false");
    }

    @DisplayName("Array Equals")
    @Test
    void testArrayEquals(){
        String[] stringArray={"A", "B", "C"};
        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be the same");
    }

    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals(){
        List<String> theList=List.of("luv","2","code");
        assertIterableEquals(theList, demoUtils.getAcademyInList(), "Expected list should be same as actual list");
    }

    @DisplayName("Lines match")
    @Test
    void testLinesMatch(){
        List<String> theList=List.of("luv","2","code");

        assertLinesMatch(theList, demoUtils.getAcademyInList(),"Lines should match");
    }

    @DisplayName("Throws and Does not Throw")
    @Test
    void testThrowAndNotThrow(){

        assertThrows(Exception.class,()->{demoUtils.throwException(-1);},"Should throw exception ");
        assertDoesNotThrow(()->{demoUtils.throwException(9);},"Should not throw exception");
    }

    @DisplayName("Timeout")
    @Test
    void testTimeout(){

        assertTimeoutPreemptively(Duration.ofSeconds(3), ()->{demoUtils.checkTimeout();},"Method should execute in 3 seconds");

    }

    @DisplayName("Multiply")
    @Test
    void testMultiply(){
        assertEquals(12, demoUtils.multiply(3, 4),"should correct the operation");
    }

    /*
    @AfterEach
    void tearDownAfterEach(){
        System.out.println("Running @AfterEach");
        System.out.println();
    }

    @BeforeAll
    static void setupBeforeEachClass(){
        System.out.println("@BeforeAll executes only once before all test methods execution in the class");
    }

    @AfterAll
    static void tearDownAfterAll(){
        System.out.println("@AfterAll executes only once after all test methods execution in the class");
    }
    */
}
