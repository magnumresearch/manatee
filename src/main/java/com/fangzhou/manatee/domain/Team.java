package com.fangzhou.manatee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "organization")
    private String organization;

    @Column(name = "name")
    private String name;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "max_patients")
    private Long maxPatients;

    @Column(name = "monday")
    private Long monday;

    @Column(name = "tuesday")
    private Long tuesday;

    @Column(name = "wednesday")
    private Long wednesday;

    @Column(name = "thursday")
    private Long thursday;

    @Column(name = "friday")
    private Long friday;

    @Column(name = "saturday")
    private Long saturday;

    @Column(name = "sunday")
    private Long sunday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Long getMaxPatients() {
        return maxPatients;
    }

    public void setMaxPatients(Long maxPatients) {
        this.maxPatients = maxPatients;
    }

    public Long getMonday() {
        return monday;
    }

    public void setMonday(Long monday) {
        this.monday = monday;
    }

    public Long getTuesday() {
        return tuesday;
    }

    public void setTuesday(Long tuesday) {
        this.tuesday = tuesday;
    }

    public Long getWednesday() {
        return wednesday;
    }

    public void setWednesday(Long wednesday) {
        this.wednesday = wednesday;
    }

    public Long getThursday() {
        return thursday;
    }

    public void setThursday(Long thursday) {
        this.thursday = thursday;
    }

    public Long getFriday() {
        return friday;
    }

    public void setFriday(Long friday) {
        this.friday = friday;
    }

    public Long getSaturday() {
        return saturday;
    }

    public void setSaturday(Long saturday) {
        this.saturday = saturday;
    }

    public Long getSunday() {
        return sunday;
    }

    public void setSunday(Long sunday) {
        this.sunday = sunday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        if(team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", organization='" + organization + "'" +
            ", name='" + name + "'" +
            ", specialty='" + specialty + "'" +
            ", maxPatients='" + maxPatients + "'" +
            ", monday='" + monday + "'" +
            ", tuesday='" + tuesday + "'" +
            ", wednesday='" + wednesday + "'" +
            ", thursday='" + thursday + "'" +
            ", friday='" + friday + "'" +
            ", saturday='" + saturday + "'" +
            ", sunday='" + sunday + "'" +
            '}';
    }
}
