package com.example.autoraceapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RaceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raceDate;
    private String venue;
    private Integer raceNumber;
    private String weatherCondition;
    private String temperature;
    private String humidity;
    private String trackTemperature;
    private String trackCondition;
    private String featuredRider;
    private String betType;
    private String finalPrediction;
    private String note;

    public RaceRecord() {
    }

    public RaceRecord(Long id, String raceDate, String venue, Integer raceNumber,
            String weatherCondition, String temperature, String humidity,
            String trackTemperature, String trackCondition, String featuredRider,
            String betType, String finalPrediction, String note) {
        this.id = id;
        this.raceDate = raceDate;
        this.venue = venue;
        this.raceNumber = raceNumber;
        this.weatherCondition = weatherCondition;
        this.temperature = temperature;
        this.humidity = humidity;
        this.trackTemperature = trackTemperature;
        this.trackCondition = trackCondition;
        this.featuredRider = featuredRider;
        this.betType = betType;
        this.finalPrediction = finalPrediction;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(Integer raceNumber) {
        this.raceNumber = raceNumber;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTrackTemperature() {
        return trackTemperature;
    }

    public void setTrackTemperature(String trackTemperature) {
        this.trackTemperature = trackTemperature;
    }

    public String getTrackCondition() {
        return trackCondition;
    }

    public void setTrackCondition(String trackCondition) {
        this.trackCondition = trackCondition;
    }

    public String getFeaturedRider() {
        return featuredRider;
    }

    public void setFeaturedRider(String featuredRider) {
        this.featuredRider = featuredRider;
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType;
    }

    public String getFinalPrediction() {
        return finalPrediction;
    }

    public void setFinalPrediction(String finalPrediction) {
        this.finalPrediction = finalPrediction;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}