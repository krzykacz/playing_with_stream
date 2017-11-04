package model.filters;

import model.Student;

@FunctionalInterface
public interface StudentChecker {
    public boolean check(Student student);

}
