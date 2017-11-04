package test;


import model.Assessment;
import model.Grade;
import model.Student;
import model.filters.StudentChecker;
import model.filters.StudentFilter;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;


/** Class with performance Tests operating on collections.
 *  Comparison include 5 type of dealing with problem.
 *  From results of tests we can observe that best solution of the problem depends on number of elements in collection.
 *
 */
public class PerformanceTest{


    /** Performance test actions on collection.
     * Test time of computing average from small amount (1000) of list elements.
     *  We consider 4 cases: Stream, Stream int, Parallel Stream and classic for **/
    @Test
    public void compareTestFromSmallAmount(){
        List<Assessment> assessments = getAssessments(1000);
        compareTest(assessments);
    }

    /** Performance test actions on collection.
     * Test time of computing average from medium amount (10000) of list elements.
     *  We consider 4 cases: Stream, Stream int, Parallel Stream and classic for **/
    @Test
    public void compareTestFromMediumAmount(){
        List<Assessment> assessments = getAssessments(10000);
        compareTest(assessments);
    }

    /** Performance test actions on collection.
     * Test time of computing average from big amount (100000) of list elements.
     *  We consider 4 cases: Stream, Stream int, Parallel Stream and classic for **/
    @Test
    public void compareTestFromBigAmount(){
        //List<Assessment> assessments = getAssessments(100000);
        //compareTest(assessments);
    }

    @Test
    public void filterComparisonTest(){
        Collection<Student> students = getStudents(100);
        Collection<Student> studentsThatPassed = new ArrayList<>(100);

        long time = measureTime(
                ()->students.stream().filter(Student::hadPassedEverything)
                        .collect(toCollection(()->studentsThatPassed))
        );
        System.out.println("Stream filter" + time);


        BiFunction<Collection<Student>, StudentChecker, Collection<Student>> filterStudents= StudentFilter::filterStudents;
        time = measureTime(
                ()->filterStudents.apply(students, passedYear()).stream()
                        .collect(Collectors.toCollection(()->studentsThatPassed))
        );
        System.out.println("Bifunction filter " + time);
    }


    private void compareTest(List<Assessment> assessments){

        // Stream
        long time = measureTime(() -> assessments.stream().map(Assessment::getECTS).reduce((d1, d2) -> d1 + d2).get() / assessments.size());
        System.out.println("Stream " + time);

        // Stream mapToInt
        time = measureTime(() -> assessments.stream().mapToInt(Assessment::getECTS).average());
        System.out.println("Stream int " + time);

        //ParallelStream
        time = measureTime(() -> assessments.parallelStream().mapToInt(Assessment::getECTS).average());
        System.out.println("Parallel stream int " + time);

        //parallel stream with reduction
        time = measureTime(() -> assessments.parallelStream().map(Assessment::getECTS).reduce((a,b)->a+b).get() / assessments.size());
        System.out.println("Parallel stream reduction " + time);

        //foreach
        time = measureTime(
                ()-> {
                    int sumItr = 0;
                    Iterator<Assessment> itr = assessments.iterator();
                    while(itr.hasNext())
                        sumItr+=itr.next().getECTS();
                    return sumItr/assessments.size();
                }
        );
        System.out.println("While with iterator " + time);

        // classic for
        time = measureTime(() -> {
            int sum = 0;
            for (Assessment assessment : assessments) {
                sum += assessment.getECTS();
            }
            return sum / assessments.size();
        });
        System.out.println("For " + time);
    }


    private StudentChecker passedYear() {
        return student -> student.hadPassedEverything();
    }

    private List<Assessment> getAssessments(int numberOfAssessments) {
        return new Random().ints(0, 100).limit(numberOfAssessments).mapToObj(
                (i) -> {

                    return new Assessment(
                            i<50?Grade.FAIL:Grade.EXCELLENT,
                            "subject", i);
                }
                ).collect(toList());
    }

    private List<Student> getStudents(int numberOfStudents){
        List<Student> studentsList = new ArrayList<>();
        for (int i = 0; i < numberOfStudents; i++) {
            studentsList.add(new Student(Integer.toString(i), getAssessments(2)));
        }
        return studentsList;
    }

    private <T> long measureTime(Supplier<T> supplier) {
        long start = System.nanoTime();
        for (int i = 0; i < 250000; i++) {
            supplier.get();
        }
        return (System.nanoTime() - start) / 250000;
    }
}
