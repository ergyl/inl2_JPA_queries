package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.ArrayList;
import java.util.List;

public final class AppMain {
    private static final EntityManagerFactory emf;
    private static final EntityManager em;
    private static final EntityTransaction tx;

    static {
        emf = Persistence.createEntityManagerFactory("databaseConfig");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    public static void main(String[] args) {
        setUpData();
        taskOne();
        taskTwo();
        taskThree();
        taskFour();
        taskFive();
    }

    /**
     * Create example data and persist to database.
     */
    private static void setUpData() {
        try {
            tx.begin();
            Subject subject1 = new Subject("Math", 3);
            Subject subject2 = new Subject("Science", 3);
            Subject subject3 = new Subject("Geography", 2);
            Subject subject4 = new Subject("Arts and craft", 1);

            Tutor tutor1 = new Tutor("LABE", "Lars Bengtsson", 42000);
            Tutor tutor2 = new Tutor("ANAN", "Anna Stjerna", 39500);
            Tutor tutor3 = new Tutor("JOFE", "Jonas Fergzi", 9800);
            Tutor tutor4 = new Tutor("RYAA", "Ryan Aarkam", 8000);
            Tutor tutor5 = new Tutor("EVAN", "Eva Andersson", 23900);

            var tutors = new ArrayList<>(List.of(tutor1, tutor2, tutor3, tutor4, tutor5));
            tutors.forEach(em::persist);

            tutor1.addSubjectToTeach(subject1);
            tutor1.addSubjectToTeach(subject2);
            tutor1.addSubjectToTeach(subject3);
            tutor2.addSubjectToTeach(subject3);
            tutor3.addSubjectToTeach(subject1);
            tutor4.addSubjectToTeach(subject4);
            tutor5.addSubjectToTeach(subject3);

            tutor1.createStudentAndAddToTeachingGroup("Shahzad Azam", "stu-24-6163", "Stardust Road 2", "Burbank", "91506 CA");
            tutor1.createStudentAndAddToTeachingGroup("Liza Hanson", "stu-24-3133", "Maple Street 30 apt 42", "Burbank", "91505 CA");
            tutor1.createStudentAndAddToTeachingGroup("Ash Newton", "stu-24-3143", "Palm Street 443", "Burbank", "91506 CA");
            tutor1.createStudentAndAddToTeachingGroup("Jennie McLaren", "stu-24-0033", "Queen's Street 99", "Burbank", "91510 CA");
            tutor2.createStudentAndAddToTeachingGroup("Pria Adumihala", "stu-24-3359", "Skater's Road", "Burbank", "92505 CA");
            tutor2.createStudentAndAddToTeachingGroup("Adam Sandler", "stu-24-0921", "Cheddar Road 220 apt 29", "Burbank", "91515 CA");
            tutor3.createStudentAndAddToTeachingGroup("Madeleine Swark Johnson", "stu-24-4992", "Twin Hills 30 apt 204", "Burbank", "91905 CA");
            tutor4.createStudentAndAddToTeachingGroup("Prince Quovi", "stu-24-0429", "Hikers End 420", "Burbank", "91505 CA");
            tutor4.createStudentAndAddToTeachingGroup("Adam Swenson", "stu-24-8292", "Sunset Bvd 10", "Burbank", "91506 CA");
            tutor5.createStudentAndAddToTeachingGroup("Troy Fenderson", "stu-24-6443", "Boyton Street 404", "Burbank", "91515 CA");

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query to find the name of all students whose tutor
     * teaches science.
     */
    private static void taskOne() {
        try {
            tx.begin();
            String subjectName = "science";
            TypedQuery<Subject> fetchSubject = em.createNamedQuery("findSubjectByName", Subject.class)
                    .setParameter("name", subjectName);
            Subject science = fetchSubject.getSingleResult();

            var results = em.createQuery("select s.name from Tutor t join t.teachingGroup s" +
                            " where :subject member of t.subjectsToTeach", String.class)
                    .setParameter("subject", science).getResultList();
            System.out.println("------Result from task one------");
            results.forEach(System.out::println);

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query to find the names of all students and their tutor.
     */
    private static void taskTwo() {
        try {
            tx.begin();
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery("select s.name, t.name from Tutor t join t.teachingGroup s").getResultList();

            System.out.println("------Result from task two------");
            for (Object[] pair : results) {
                System.out.println((results.indexOf(pair) + 1) + " Student: " + pair[0] + ", \t Tutor: " + pair[1]);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /**
     * Query to calculate the average semester length of all school subjects.
     */
    private static void taskThree() {
        try {
            tx.begin();

            double averageSemesterLength = (Double) em.createQuery("select avg(sub.numberOfSemesters) from Subject sub").getSingleResult();

            System.out.println("------Result from task three------");
            System.out.printf("Average semester length: %.2f%n%n", averageSemesterLength);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query to find the highest salary among all tutors.
     */
    private static void taskFour() {
        try {
            tx.begin();
            var highestSalary = (Integer) em.createQuery("select max(t.salary) from Tutor t").getSingleResult();

            System.out.println("------Result from task four-----");
            System.out.printf("Highest salary for tutor: %d $%n%n", highestSalary);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query to find all tutors with a salary higher than 10 000.
     */
    private static void taskFive() {
        try {
            tx.begin();

            var tenThousand = 10000;
            List<Object[]> results = em.createNamedQuery("findTutorsWithSalaryAbove", Object[].class)
                    .setParameter("reqSalary", tenThousand)
                    .getResultList();

            System.out.println("------Result from task five-----");
            for (Object[] result : results) {
                System.out.printf("%s's salary: %d $%n", result[0], result[1]);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
            em.close();
        }
    }
}
