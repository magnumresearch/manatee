package com.fangzhou.manatee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Patient extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "medical_referral_id")
    private String medicalReferralId;

    @Column(name = "age")
    private Long age;

    @Column(name = "condition_desciption")
    private String conditionDesciption;

    @Column(name = "priority")
    private String priority;

    @Column(name = "deadline")
    private ZonedDateTime deadline;

    @ManyToOne
    private ReferralSource referralSource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalReferralId() {
        return medicalReferralId;
    }

    public void setMedicalReferralId(String medicalReferralId) {
        this.medicalReferralId = medicalReferralId;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getConditionDesciption() {
        return conditionDesciption;
    }

    public void setConditionDesciption(String conditionDesciption) {
        this.conditionDesciption = conditionDesciption;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public ReferralSource getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(ReferralSource referralSource) {
        this.referralSource = referralSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Patient patient = (Patient) o;
        if(patient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", medicalReferralId='" + medicalReferralId + "'" +
            ", age='" + age + "'" +
            ", conditionDesciption='" + conditionDesciption + "'" +
            ", priority='" + priority + "'" +
            ", deadline='" + deadline + "'" +
            '}';
    }
}
