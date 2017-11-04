package model;

import java.util.List;

public class Student {

    private final String index;
    private List<Assessment> assessments;
    private int sumECTS;

    public Student(String index, List<Assessment> assessments){
        this.index = index;
        this.assessments = assessments;
        this.sumECTS = assessments.stream()
                .map(Assessment::getECTS)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public int getSumECTS() {
        return sumECTS;
    }

    public List<Assessment> getAssessment() {return assessments;}

    public boolean hadPassedEverything() {
        return assessments.stream().map(Assessment::getPassed).allMatch((a) -> a);
    }
}

