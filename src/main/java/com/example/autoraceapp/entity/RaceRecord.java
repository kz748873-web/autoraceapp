package com.example.autoraceapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * レース記録を表すクラスです。
 * 1件分の入力内容をこのクラスにまとめます。
 */
@Entity
public class RaceRecord {

    /**
     * データを区別するための番号です。
     * 保存時に自動で入ります。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 開催日です。 */
    private String raceDate;

    /** レース場です。 */
    private String venue;

    /** レース番号です。 */
    private Integer raceNumber;

    /** 天気です。 */
    private String weather;

    /** 気温です。 */
    private String temperature;

    /** 湿度です。 */
    private String humidity;

    /** 走路温度です。 */
    private String trackTemperature;

    /** 走路状況です。 */
    private String trackCondition;

    /** 注目選手です。 */
    private String featuredRider;

    /** 賭け式です。 */
    private String betType;

    /** 最終予想です。 */
    private String finalPrediction;

    /** 備考です。 */
    private String note;

    /** JPAで使うための空のコンストラクタです。 */
    public RaceRecord() {
    }

    /**
     * ダミーデータを作りやすくするためのコンストラクタです。
     */
    public RaceRecord(Long id, String raceDate, String venue, Integer raceNumber,
            String weather, String temperature, String humidity, String trackTemperature,
            String trackCondition, String featuredRider, String betType,
            String finalPrediction, String note) {
        this.id = id;
        this.raceDate = raceDate;
        this.venue = venue;
        this.raceNumber = raceNumber;
        this.weather = weather;
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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
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