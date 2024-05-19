package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "TBL_TUTOR")
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true, nullable = false, length = 4)
    private String tutorId;
    @Column(length = 25)
    private String name;
    @Column(length = 8)
    private int salary;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TUTOR_FK")
    private Set<Student> teachingGroup;

    @ManyToMany(mappedBy = "tutors", cascade = CascadeType.PERSIST)
    private Set<Subject> subjectsToTeach;

    public Tutor() {
    }

    public Tutor(String tutorId, String name, int salary) {
        this.tutorId = tutorId;
        this.name = name;
        this.salary = salary;
        this.teachingGroup = new HashSet<Student>();
        this.subjectsToTeach = new HashSet<Subject>();

    }

    public String getTutorId() {
        return tutorId;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public Set<Subject> getSubjectsToTeach() {
        return subjectsToTeach;
    }

    public Set<Student> getTeachingGroup() {
        return Collections.unmodifiableSet(this.teachingGroup);
    }

    public Set<Subject> getSubjects() {
        return this.subjectsToTeach;
    }

    public void createStudentAndAddToTeachingGroup(String studentName, String enrollmentID, String street, String city, String zipcode) {
        Student newStudent = new Student(studentName, enrollmentID, street, city, zipcode);
        this.addStudentToTeachingGroup(newStudent);
    }

    public void addStudentToTeachingGroup(Student newStudent) {
        this.teachingGroup.add(newStudent);

    }

    public void addSubjectToTeach(Subject subject) {
        this.subjectsToTeach.add(subject);
        subject.getTutors().add(this);
    }

    @Override
    public String toString() {
        return "Tutor: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return Objects.equals(getTutorId(), tutor.getTutorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tutorId, getTutorId());
    }
}