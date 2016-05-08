package com.fangzhou.manatee.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Queue.
 */
@Entity
@Table(name = "queue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Queue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp_initial")
    private ZonedDateTime timestampInitial;

    @Column(name = "timestamp_final")
    private ZonedDateTime timestampFinal;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getTimestampInitial() {
        return timestampInitial;
    }

    public void setTimestampInitial(ZonedDateTime timestampInitial) {
        this.timestampInitial = timestampInitial;
    }

    public ZonedDateTime getTimestampFinal() {
        return timestampFinal;
    }

    public void setTimestampFinal(ZonedDateTime timestampFinal) {
        this.timestampFinal = timestampFinal;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Queue queue = (Queue) o;
        if(queue.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, queue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Queue{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", timestampInitial='" + timestampInitial + "'" +
            ", timestampFinal='" + timestampFinal + "'" +
            '}';
    }
}
