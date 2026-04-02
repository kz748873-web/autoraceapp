package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.repository.RaceRecordRepository;

/**
 * レース記録の取得・保存・検索を担当するサービスです。
 */
@Service
public class RaceRecordService {

    private final RaceRecordRepository raceRecordRepository;

    public RaceRecordService(RaceRecordRepository raceRecordRepository) {
        this.raceRecordRepository = raceRecordRepository;
    }

    public List<RaceRecord> findRecords(String venue, String weatherCondition, String featuredRider) {
        List<RaceRecord> records = raceRecordRepository.findAll();
        if (records.isEmpty()) {
            records = createDummyRecords();
        }

        List<RaceRecord> filteredRecords = new ArrayList<>();
        for (RaceRecord record : records) {
            if (!isMatch(record.getVenue(), venue)) {
                continue;
            }
            if (!isMatch(record.getWeatherCondition(), weatherCondition)) {
                continue;
            }
            if (!matchesFeaturedRider(record, featuredRider)) {
                continue;
            }
            filteredRecords.add(record);
        }
        return filteredRecords;
    }

    public RaceRecord findById(Long id) {
        Optional<RaceRecord> record = raceRecordRepository.findById(id);
        if (record.isPresent()) {
            return record.get();
        }

        for (RaceRecord dummyRecord : createDummyRecords()) {
            if (dummyRecord.getId().equals(id)) {
                return dummyRecord;
            }
        }
        return null;
    }

    public RaceRecord createEmptyRecord() {
        RaceRecord record = new RaceRecord();

        record.setRaceDate("");
        record.setVenue("");
        record.setWeatherCondition("");
        record.setTemperature("");
        record.setHumidity("");
        record.setWindSpeed("");
        record.setTrackTemperature("");
        record.setTrackCondition("");

        record.setFeaturedRider1Name("");
        record.setFeaturedRider1Mark("");
        record.setFeaturedRider2Name("");
        record.setFeaturedRider2Mark("");
        record.setFeaturedRider3Name("");
        record.setFeaturedRider3Mark("");

        record.setPreRacePrediction("");
        record.setPreRaceNote("");

        record.setTrialTime1("");
        record.setTrialTime2("");
        record.setTrialTime3("");
        record.setTrialTime4("");
        record.setTrialTime5("");
        record.setTrialTime6("");
        record.setTrialTime7("");
        record.setTrialTime8("");

        record.setFeaturedTrialTimeNote("");
        record.setRaceOverallNote("");
        record.setBetType("");
        record.setFinalPrediction("");
        record.setPredictionNote("");
        record.setRaceResult("");
        record.setResultComparison("");
        record.setReviewNote("");
        record.setFeaturedRider("");
        record.setNote("");

        return record;
    }

    public RaceRecord save(RaceRecord raceRecord) {
        normalizeRecord(raceRecord);
        calculateBetAmounts(raceRecord);
        setLegacyFeaturedRider(raceRecord);
        return raceRecordRepository.save(raceRecord);
    }

    private boolean matchesFeaturedRider(RaceRecord record, String searchValue) {
        if (searchValue == null || searchValue.isBlank()) {
            return true;
        }

        String keyword = searchValue.trim();
        return contains(record.getFeaturedRider1Name(), keyword)
                || contains(record.getFeaturedRider2Name(), keyword)
                || contains(record.getFeaturedRider3Name(), keyword)
                || contains(record.getFeaturedRider(), keyword);
    }

    private void normalizeRecord(RaceRecord record) {
        record.setRaceDate(trimToEmpty(record.getRaceDate()));
        record.setVenue(trimToEmpty(record.getVenue()));
        record.setWeatherCondition(trimToEmpty(record.getWeatherCondition()));
        record.setTemperature(trimToEmpty(record.getTemperature()));
        record.setHumidity(trimToEmpty(record.getHumidity()));
        record.setWindSpeed(trimToEmpty(record.getWindSpeed()));
        record.setTrackTemperature(trimToEmpty(record.getTrackTemperature()));
        record.setTrackCondition(trimToEmpty(record.getTrackCondition()));

        record.setFeaturedRider1Name(trimToEmpty(record.getFeaturedRider1Name()));
        record.setFeaturedRider1Mark(trimToEmpty(record.getFeaturedRider1Mark()));
        record.setFeaturedRider2Name(trimToEmpty(record.getFeaturedRider2Name()));
        record.setFeaturedRider2Mark(trimToEmpty(record.getFeaturedRider2Mark()));
        record.setFeaturedRider3Name(trimToEmpty(record.getFeaturedRider3Name()));
        record.setFeaturedRider3Mark(trimToEmpty(record.getFeaturedRider3Mark()));

        record.setPreRacePrediction(trimToEmpty(record.getPreRacePrediction()));
        record.setPreRaceNote(trimToEmpty(record.getPreRaceNote()));

        record.setTrialTime1(trimToEmpty(record.getTrialTime1()));
        record.setTrialTime2(trimToEmpty(record.getTrialTime2()));
        record.setTrialTime3(trimToEmpty(record.getTrialTime3()));
        record.setTrialTime4(trimToEmpty(record.getTrialTime4()));
        record.setTrialTime5(trimToEmpty(record.getTrialTime5()));
        record.setTrialTime6(trimToEmpty(record.getTrialTime6()));
        record.setTrialTime7(trimToEmpty(record.getTrialTime7()));
        record.setTrialTime8(trimToEmpty(record.getTrialTime8()));

        record.setFeaturedTrialTimeNote(trimToEmpty(record.getFeaturedTrialTimeNote()));
        record.setRaceOverallNote(trimToEmpty(record.getRaceOverallNote()));
        record.setBetType(trimToEmpty(record.getBetType()));
        record.setFinalPrediction(trimToEmpty(record.getFinalPrediction()));
        record.setPredictionNote(trimToEmpty(record.getPredictionNote()));
        record.setRaceResult(trimToEmpty(record.getRaceResult()));
        record.setResultComparison(trimToEmpty(record.getResultComparison()));
        record.setReviewNote(trimToEmpty(record.getReviewNote()));
        record.setNote(trimToEmpty(record.getNote()));
    }

    private void calculateBetAmounts(RaceRecord record) {
        Integer betCount = record.getBetCount();
        Integer unitBetAmount = record.getUnitBetAmount();

        if (betCount != null && unitBetAmount != null) {
            int calculatedTotal = betCount * unitBetAmount;
            record.setTotalBetAmount(calculatedTotal);

            if (record.getPurchaseAmount() == null) {
                record.setPurchaseAmount(calculatedTotal);
            }
        } else if (record.getPurchaseAmount() != null) {
            record.setTotalBetAmount(record.getPurchaseAmount());
        }
    }

    private void setLegacyFeaturedRider(RaceRecord record) {
        List<String> riders = new ArrayList<>();
        addFeaturedRiderText(riders, record.getFeaturedRider1Mark(), record.getFeaturedRider1Name());
        addFeaturedRiderText(riders, record.getFeaturedRider2Mark(), record.getFeaturedRider2Name());
        addFeaturedRiderText(riders, record.getFeaturedRider3Mark(), record.getFeaturedRider3Name());
        record.setFeaturedRider(String.join(" / ", riders));
    }

    private void addFeaturedRiderText(List<String> riders, String mark, String name) {
        if (name == null || name.isBlank()) {
            return;
        }

        if (mark == null || mark.isBlank()) {
            riders.add(name);
            return;
        }

        riders.add(mark + " " + name);
    }

    private List<RaceRecord> createDummyRecords() {
        List<RaceRecord> dummyRecords = new ArrayList<>();

        RaceRecord first = new RaceRecord(
                1L,
                "2026-03-31",
                "川口",
                8,
                "晴れ",
                "22℃",
                "48%",
                "31℃",
                "良走路",
                "◎ 大都太郎 / ○ 佐藤花子 / △ 高橋一郎",
                "3連単",
                "1-2-3",
                "スタート重視で確認したいレースです。"
        );
        first.setFeaturedRider1Name("大都太郎");
        first.setFeaturedRider1Number(1);
        first.setFeaturedRider1Mark("◎");
        first.setFeaturedRider2Name("佐藤花子");
        first.setFeaturedRider2Number(3);
        first.setFeaturedRider2Mark("○");
        first.setFeaturedRider3Name("高橋一郎");
        first.setFeaturedRider3Number(5);
        first.setFeaturedRider3Mark("△");
        first.setPreRacePrediction("内枠中心で組み立てたい。");
        first.setPreRaceNote("前日オッズでは1号車中心。");
        first.setWindSpeed("2m");
        first.setTrialTime1("3.32");
        first.setTrialTime2("3.35");
        first.setTrialTime3("3.34");
        first.setTrialTime4("3.37");
        first.setTrialTime5("3.33");
        first.setTrialTime6("3.39");
        first.setTrialTime7("3.40");
        first.setTrialTime8("3.38");
        first.setFeaturedTrialTimeNote("1号車と5号車の気配が良い。");
        first.setRaceOverallNote("スタートで主導権を取れるかがポイント。");
        first.setBetCount(6);
        first.setUnitBetAmount(100);
        first.setPurchaseAmount(600);
        first.setTotalBetAmount(600);
        first.setPredictionNote("試走を見て1号車中心に調整。");
        first.setRaceResult("結果は1-2-3。");
        first.setResultComparison("本命と結果が一致した。");
        first.setReviewNote("走路状態の読みも良かった。");

        RaceRecord second = new RaceRecord(
                2L,
                "2026-03-30",
                "浜松",
                10,
                "曇り",
                "18℃",
                "62%",
                "24℃",
                "斑走路",
                "◎ 青山次郎 / 注 中村一樹 / ○ 山田健太",
                "2連単",
                "2-1",
                "試走気配を見て買い目を絞りたい。"
        );
        second.setFeaturedRider1Name("青山次郎");
        second.setFeaturedRider1Number(2);
        second.setFeaturedRider1Mark("◎");
        second.setFeaturedRider2Name("中村一樹");
        second.setFeaturedRider2Number(6);
        second.setFeaturedRider2Mark("注");
        second.setFeaturedRider3Name("山田健太");
        second.setFeaturedRider3Number(1);
        second.setFeaturedRider3Mark("○");
        second.setPreRacePrediction("2号車の安定感を重視。");
        second.setPreRaceNote("天候変化があれば再検討。");
        second.setWindSpeed("4m");
        second.setTrialTime1("3.40");
        second.setTrialTime2("3.31");
        second.setTrialTime3("3.38");
        second.setTrialTime4("3.42");
        second.setTrialTime5("3.39");
        second.setTrialTime6("3.36");
        second.setTrialTime7("3.41");
        second.setTrialTime8("3.43");
        second.setFeaturedTrialTimeNote("2号車が抜けて良い。");
        second.setRaceOverallNote("斑走路なので外の動きに注意。");
        second.setBetCount(3);
        second.setUnitBetAmount(200);
        second.setPurchaseAmount(600);
        second.setTotalBetAmount(600);
        second.setPredictionNote("点数を絞って勝負。");
        second.setRaceResult("結果は2-6。");
        second.setResultComparison("本命は来たが相手が想定外。");
        second.setReviewNote("注評価の選手をもう少し上げてもよかった。");

        dummyRecords.add(first);
        dummyRecords.add(second);
        return dummyRecords;
    }

    private boolean isMatch(String recordValue, String searchValue) {
        if (searchValue == null || searchValue.isBlank()) {
            return true;
        }
        if (recordValue == null) {
            return false;
        }
        return recordValue.contains(searchValue.trim());
    }

    private boolean contains(String value, String keyword) {
        if (value == null) {
            return false;
        }
        return value.contains(keyword);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
