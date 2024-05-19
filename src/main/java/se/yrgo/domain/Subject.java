package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TBL_SUBJECT")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true, nullable = false, length = 30)
    private String name;
    @Column(name = "NUM_SEMESTERS")
    private int numberOfSemesters;
    @ManyToMany
    private Set<Tutor> tutors;

    public Subject() {
    }

    public Subject(String subjectName, int numberOfSemesters) {
        this.name = subjectName;
        this.numberOfSemesters = numberOfSemesters;
        this.tutors = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSemesters() {
        return numberOfSemesters;
    }

    public Set<Tutor> getTutors() {
        return this.tutors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getName(), subject.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }
}

