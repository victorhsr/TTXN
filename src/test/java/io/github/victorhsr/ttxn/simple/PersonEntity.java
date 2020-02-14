package io.github.victorhsr.ttxn.simple;

import javax.persistence.*;

@Entity
@Table(name = PersonEntity.TABLE_NAME)
@SequenceGenerator(name = PersonEntity.SEQUENCE_GEN, sequenceName = PersonEntity.SEQUENCE_NAME)
public class PersonEntity {

    public static final String TABLE_NAME = "t_person";
    public static final String SEQUENCE_GEN = "person_gen";
    public static final String SEQUENCE_NAME = "seq_t_person_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PersonEntity.SEQUENCE_GEN)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    public PersonEntity() {
    }

    public PersonEntity(String fullName) {
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
        return "PersonEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
