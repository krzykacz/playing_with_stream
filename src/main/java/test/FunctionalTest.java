package test;

import model.Assessment;
import model.Grade;
import model.Student;
import model.filters.StudentChecker;
import model.filters.StudentFilter;
import org.junit.Test;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;


public class FunctionalTest{

    /** Count all ECTS points - Map and reduce **/
    @Test
    public void ECTSTest(){
        List<Student> students = fillStudentsList();
        int allECTS = students.stream().map(Student::getSumECTS).reduce((a,b)->a+b).orElse(0);
        assertEquals(25, allECTS);
    }

    /** Find all students whom ever get EXCELLENT Grade
     *  filter with lambada expr using method isExcellent() **/
    @Test
    public void StreamFilterTest(){
        List<Student> students = fillStudentsList();
        List<Student> studentsWhomGetExcellent = students.stream().filter(
                (a)-> a.getAssessment().stream().anyMatch(Assessment::isExcellent)
        ).collect(toList());

        assertEquals(1, studentsWhomGetExcellent.size());
    }


    /** Filter all students that passed year
     *  BiFunction, filter **/
    @Test
    public void filterTest(){
        List<Student> students = fillStudentsList();
        BiFunction<Collection<Student>, StudentChecker, Collection<Student>> filterStudents = StudentFilter::filterStudents;
        Collection<Student> studentsThatPassedYear = filterStudents.apply(students, passedYear());

        System.out.println(students.get(0).hadPassedEverything());
        System.out.println(students.get(1).hadPassedEverything());
        System.out.println(students.get(2).hadPassedEverything());

        assertEquals(2, studentsThatPassedYear.size());
        assertEquals(Grade.FAIL, students.get(2).getAssessment().get(0).getGrade());
    }

    @Test
    public void filterTest2(){
        List<Student> students = fillStudentsList();
        List<Student> studentsThatPassedYear;
        studentsThatPassedYear = students.stream().filter(Student::hadPassedEverything).collect(toList());

        assertEquals(2, studentsThatPassedYear.size());
    }

    @Test
    public void iterationTest(){
        List<Student> students = fillStudentsList();
        students.stream().forEach((k)->System.out.println(k));
    }

    private StudentChecker passedYear() {
        return student -> student.hadPassedEverything();
    }

    private List<Student> fillStudentsList(){
        List<Student> studentsList = Arrays.asList(
                new Student("117001", Collections.singletonList(new Assessment(Grade.EXCELLENT, "Math", 10))),
                new Student("117002", Collections.singletonList(new Assessment(Grade.GOOD, "Economy", 5))),
                new Student("117003", Collections.singletonList(new Assessment(Grade.FAIL, "Math", 10)))
        );
        return studentsList;
    }



}
