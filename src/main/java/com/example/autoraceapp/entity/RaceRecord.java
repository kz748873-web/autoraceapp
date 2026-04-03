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

    private String raceScheduleType;
    private String handicapInfo;

    private String featuredRider1Name;
    private Integer featuredRider1Number;
    private String featuredRider1Mark;

    private String featuredRider2Name;
    private Integer featuredRider2Number;
    private String featuredRider2Mark;

    private String featuredRider3Name;
    private Integer featuredRider3Number;
    private String featuredRider3Mark;

    private String preRacePrediction;
    private String preRaceNote;

    private String weatherCondition;
    private String temperature;
    private String humidity;
    private String windSpeed;
    private String trackTemperature;
    private String trackCondition;

    private String trialTime1;
    private String trialTime2;
    private String trialTime3;
    private String trialTime4;
    private String trialTime5;
    private String trialTime6;
    private String trialTime7;
    private String trialTime8;

    private String featuredTrialTimeNote;
    private String raceOverallNote;
    private String betType;
    private String finalPrediction;
    private Integer betCount;
    private String predictionNote;
    private Integer purchaseAmount;
    private Integer unitBetAmount;
    private Integer totalBetAmount;

    private String raceResult;
    private String resultComparison;
    private String reviewNote;

    private String featuredRider;
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

    public String getRaceScheduleType() {
        return raceScheduleType;
    }

    public void setRaceScheduleType(String raceScheduleType) {
        this.raceScheduleType = raceScheduleType;
    }

    public String getHandicapInfo() {
        return handicapInfo;
    }

    public void setHandicapInfo(String handicapInfo) {
        this.handicapInfo = handicapInfo;
    }

    public String getFeaturedRider1Name() {
        return featuredRider1Name;
    }

    public void setFeaturedRider1Name(String featuredRider1Name) {
        this.featuredRider1Name = featuredRider1Name;
    }

    public Integer getFeaturedRider1Number() {
        return featuredRider1Number;
    }

    public void setFeaturedRider1Number(Integer featuredRider1Number) {
        this.featuredRider1Number = featuredRider1Number;
    }

    public String getFeaturedRider1Mark() {
        return featuredRider1Mark;
    }

    public void setFeaturedRider1Mark(String featuredRider1Mark) {
        this.featuredRider1Mark = featuredRider1Mark;
    }

    public String getFeaturedRider2Name() {
        return featuredRider2Name;
    }

    public void setFeaturedRider2Name(String featuredRider2Name) {
        this.featuredRider2Name = featuredRider2Name;
    }

    public Integer getFeaturedRider2Number() {
        return featuredRider2Number;
    }

    public void setFeaturedRider2Number(Integer featuredRider2Number) {
        this.featuredRider2Number = featuredRider2Number;
    }

    public String getFeaturedRider2Mark() {
        return featuredRider2Mark;
    }

    public void setFeaturedRider2Mark(String featuredRider2Mark) {
        this.featuredRider2Mark = featuredRider2Mark;
    }

    public String getFeaturedRider3Name() {
        return featuredRider3Name;
    }

    public void setFeaturedRider3Name(String featuredRider3Name) {
        this.featuredRider3Name = featuredRider3Name;
    }

    public Integer getFeaturedRider3Number() {
        return featuredRider3Number;
    }

    public void setFeaturedRider3Number(Integer featuredRider3Number) {
        this.featuredRider3Number = featuredRider3Number;
    }

    public String getFeaturedRider3Mark() {
        return featuredRider3Mark;
    }

    public void setFeaturedRider3Mark(String featuredRider3Mark) {
        this.featuredRider3Mark = featuredRider3Mark;
    }

    public String getPreRacePrediction() {
        return preRacePrediction;
    }

    public void setPreRacePrediction(String preRacePrediction) {
        this.preRacePrediction = preRacePrediction;
    }

    public String getPreRaceNote() {
        return preRaceNote;
    }

    public void setPreRaceNote(String preRaceNote) {
        this.preRaceNote = preRaceNote;
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

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
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

    public String getTrialTime1() {
        return trialTime1;
    }

    public void setTrialTime1(String trialTime1) {
        this.trialTime1 = trialTime1;
    }

    public String getTrialTime2() {
        return trialTime2;
    }

    public void setTrialTime2(String trialTime2) {
        this.trialTime2 = trialTime2;
    }

    public String getTrialTime3() {
        return trialTime3;
    }

    public void setTrialTime3(String trialTime3) {
        this.trialTime3 = trialTime3;
    }

    public String getTrialTime4() {
        return trialTime4;
    }

    public void setTrialTime4(String trialTime4) {
        this.trialTime4 = trialTime4;
    }

    public String getTrialTime5() {
        return trialTime5;
    }

    public void setTrialTime5(String trialTime5) {
        this.trialTime5 = trialTime5;
    }

    public String getTrialTime6() {
        return trialTime6;
    }

    public void setTrialTime6(String trialTime6) {
        this.trialTime6 = trialTime6;
    }

    public String getTrialTime7() {
        return trialTime7;
    }

    public void setTrialTime7(String trialTime7) {
        this.trialTime7 = trialTime7;
    }

    public String getTrialTime8() {
        return trialTime8;
    }

    public void setTrialTime8(String trialTime8) {
        this.trialTime8 = trialTime8;
    }

    public String getFeaturedTrialTimeNote() {
        return featuredTrialTimeNote;
    }

    public void setFeaturedTrialTimeNote(String featuredTrialTimeNote) {
        this.featuredTrialTimeNote = featuredTrialTimeNote;
    }

    public String getRaceOverallNote() {
        return raceOverallNote;
    }

    public void setRaceOverallNote(String raceOverallNote) {
        this.raceOverallNote = raceOverallNote;
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

    public Integer getBetCount() {
        return betCount;
    }

    public void setBetCount(Integer betCount) {
        this.betCount = betCount;
    }

    public String getPredictionNote() {
        return predictionNote;
    }

    public void setPredictionNote(String predictionNote) {
        this.predictionNote = predictionNote;
    }

    public Integer getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Integer purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Integer getUnitBetAmount() {
        return unitBetAmount;
    }

    public void setUnitBetAmount(Integer unitBetAmount) {
        this.unitBetAmount = unitBetAmount;
    }

    public Integer getTotalBetAmount() {
        return totalBetAmount;
    }

    public void setTotalBetAmount(Integer totalBetAmount) {
        this.totalBetAmount = totalBetAmount;
    }

    public String getRaceResult() {
        return raceResult;
    }

    public void setRaceResult(String raceResult) {
        this.raceResult = raceResult;
    }

    public String getResultComparison() {
        return resultComparison;
    }

    public void setResultComparison(String resultComparison) {
        this.resultComparison = resultComparison;
    }

    public String getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    public String getFeaturedRider() {
        return featuredRider;
    }

    public void setFeaturedRider(String featuredRider) {
        this.featuredRider = featuredRider;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
