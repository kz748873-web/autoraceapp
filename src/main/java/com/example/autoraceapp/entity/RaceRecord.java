package com.example.autoraceapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * レース記録を表すクラスです。
 * 1件分のレース情報をこのクラスで持ちます。
 */
@Entity
public class RaceRecord {

    /**
     * レコードを区別するための番号です。
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

    /** 天候です。 */
    private String weather;

    /** 注目選手です。 */
    private String featuredRider;

    /** 最終予想です。 */
    private String finalPrediction;

    /** JPAで使うための空コンストラクタです。 */
    public RaceRecord() {
    }

    /** ダミーデータを作りやすくするためのコンストラクタです。 */
    public RaceRecord(Long id, String raceDate, String venue, Integer raceNumber,
            String weather, String featuredRider, String finalPrediction) {
        this.id = id;
        this.raceDate = raceDate;
        this.venue = venue;
        this.raceNumber = raceNumber;
        this.weather = weather;
        this.featuredRider = featuredRider;
        this.finalPrediction = finalPrediction;
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

    public String getFeaturedRider() {
        return featuredRider;
    }

    public void setFeaturedRider(String featuredRider) {
        this.featuredRider = featuredRider;
    }

    public String getFinalPrediction() {
        return finalPrediction;
    }

    public void setFinalPrediction(String finalPrediction) {
        this.finalPrediction = finalPrediction;
    }
}