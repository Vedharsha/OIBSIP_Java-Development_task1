package com.reservation.onlinebooking.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "trains")
public class Train {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trainId;

    @Column(unique=true, nullable=false)
    private String trainNumber;

    private String trainName;
    private String classType;
    private String source;
    private String destination;
    private String timing;
    private Integer availableSeats = 0;
    private LocalTime estimatedReachTime;


    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public LocalTime getEstimatedReachTime() {
        return estimatedReachTime;
    }

    public void setEstimatedReachTime(LocalTime estimatedReachTime) {
        this.estimatedReachTime = estimatedReachTime;
    }
}
