package se.yrgo.domain;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="TBL_STUDENT")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true, nullable = false, length = 11)
    private String enrollmentID;
    @Column(length = 25)
    private String name;
    private Address address;

    public Student() {
    }

    public Student(String name, String enrollmentID, String street, String city, String zipcode) {
        this.name = name;
        this.enrollmentID = enrollmentID;
        this.address = new Address(street, city, zipcode);
    }

    public String getEnrollmentID() {
        return enrollmentID;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(String street, String city, String zipcode) {
        this.address = new Address(street, city, zipcode);
    }

    public String toString() {
        return name + ", home adress: " + address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getEnrollmentID(), student.getEnrollmentID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getEnrollmentID());
    }
}
