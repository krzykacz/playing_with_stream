package model;

public class Assessment {

    private final Grade grade;
    private final String subject;
    private final int ECTS;

    public Assessment(Grade grade, String subject, int ECTS){
        this.grade = grade;
        this.subject = subject;
        this.ECTS = ECTS;
    }

    public int getECTS() {
        return ECTS;
    }

    public Grade getGrade() {return grade;}

    public boolean isExcellent(){return getGrade()==Grade.EXCELLENT;}

    public boolean getPassed() {
        return grade != Grade.FAIL && grade != Grade.SUFFICIENT;
    }
}
