package io.github.victorhsr.ttxn.entitydomain;

import javax.persistence.*;

@Entity
@Table(name = Person.TABLE_NAME)
@SequenceGenerator(name = Person.SEQUENCE_GEN, sequenceName = Person.SEQUENCE_NAME)
public class Person {

    public static final String TABLE_NAME = "t_person";
    public static final String SEQUENCE_GEN = "person_gen";
    public static final String SEQUENCE_NAME = "seq_t_person_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Person.SEQUENCE_GEN)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    public Person() {
    }

    public Person(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
