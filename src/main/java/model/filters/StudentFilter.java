package model.filters;

import model.Student;

import java.util.ArrayList;
import java.util.Collection;

public class StudentFilter {

    public static Collection<Student> filterStudents(Collection<Student> students,
                                                     StudentChecker checker) {
        Collection<Student> result = new ArrayList<>();
        for (Student student : students ) {
            if (checker.check(student)) {
                result.add(student);
            }
        }
        return result;
    }


}
