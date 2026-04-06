package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.repository.RaceRecordRepository;

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
        record.setRaceScheduleType("");
        record.setHandicapInfo("");
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
        record.setBetLine1Type("");
        record.setBetLine2Type("");
        record.setBetLine3Type("");
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

    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        if (raceRecordRepository.existsById(id)) {
            raceRecordRepository.deleteById(id);
        }
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
        record.setRaceScheduleType(trimToEmpty(record.getRaceScheduleType()));
        record.setHandicapInfo(trimToEmpty(record.getHandicapInfo()));

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
        record.setBetLine1Type(trimToEmpty(record.getBetLine1Type()));
        record.setBetLine2Type(trimToEmpty(record.getBetLine2Type()));
        record.setBetLine3Type(trimToEmpty(record.getBetLine3Type()));
        record.setBetType(trimToEmpty(record.getBetType()));
        record.setFinalPrediction(trimToEmpty(record.getFinalPrediction()));
        record.setPredictionNote(trimToEmpty(record.getPredictionNote()));
        record.setRaceResult(trimToEmpty(record.getRaceResult()));
        record.setResultComparison(trimToEmpty(record.getResultComparison()));
        record.setReviewNote(trimToEmpty(record.getReviewNote()));
        record.setNote(trimToEmpty(record.getNote()));
    }

    private void calculateBetAmounts(RaceRecord record) {
        int total = safeInt(record.getBetLine1Amount())
                + safeInt(record.getBetLine2Amount())
                + safeInt(record.getBetLine3Amount());

        if (total > 0) {
            record.setTotalBetAmount(total);
            record.setPurchaseAmount(total);
        } else if (record.getPurchaseAmount() != null) {
            record.setTotalBetAmount(record.getPurchaseAmount());
        }

        if (record.getPayoutAmount() != null) {
            record.setProfitLoss(record.getPayoutAmount() - safeInt(record.getTotalBetAmount()));
        } else {
            record.setProfitLoss(null);
        }

        if (!record.getBetLine1Type().isBlank()) {
            record.setBetType(record.getBetLine1Type());
            record.setBetCount(record.getBetLine1Count());
            record.setUnitBetAmount(record.getBetLine1Amount());
        }
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
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

        RaceRecord first = new RaceRecord(1L, "2026-03-31", "川口", 8, "晴れ", "22", "48", "31", "良走路", "◎ 山田太郎 / ○ 鈴木一郎 / △ 佐藤花子", "3連単", "1-3-5 / 1-5-3", "スタート力を重視したレースだった。");
        first.setRaceScheduleType("デイレース");
        first.setHandicapInfo("0m / 10m / 20m");
        first.setFeaturedRider1Name("山田太郎");
        first.setFeaturedRider1Number(1);
        first.setFeaturedRider1Mark("◎");
        first.setFeaturedRider2Name("鈴木一郎");
        first.setFeaturedRider2Number(3);
        first.setFeaturedRider2Mark("○");
        first.setFeaturedRider3Name("佐藤花子");
        first.setFeaturedRider3Number(5);
        first.setFeaturedRider3Mark("△");
        first.setPreRacePrediction("内枠中心で組み立てたい。スタート力を重視。");
        first.setPreRaceNote("前日オッズでは1号車が中心。走路は軽めの想定。");
        first.setWindSpeed("2.0");
        first.setTrialTime1("3.32");
        first.setTrialTime2("3.35");
        first.setTrialTime3("3.34");
        first.setTrialTime4("3.37");
        first.setTrialTime5("3.33");
        first.setTrialTime6("3.39");
        first.setTrialTime7("3.40");
        first.setTrialTime8("3.38");
        first.setFeaturedTrialTimeNote("1号車と5号車の試走が良く、上積みを感じた。");
        first.setRaceOverallNote("スタートで主導権を取れるかがポイント。");
        first.setBetLine1Type("3連単");
        first.setBetLine1Count(6);
        first.setBetLine1Amount(600);
        first.setPayoutAmount(1240);
        first.setPredictionNote("試走後に1号車中心へ寄せた。");
        first.setRaceResult("結果は1-3-5。");
        first.setResultComparison("本命からの組み立ては合っていた。");
        first.setReviewNote("試走上位の車を素直に評価できた。");
        calculateBetAmounts(first);
        setLegacyFeaturedRider(first);

        RaceRecord second = new RaceRecord(2L, "2026-03-30", "伊勢崎", 10, "曇り", "18", "62", "24", "湿走路", "◎ 田中次郎 / ▲ 高橋未来 / ○ 小林優子", "2連単", "2-6 / 2-1", "試走気配を見て軸を絞った。");
        second.setRaceScheduleType("ナイター");
        second.setHandicapInfo("0m / 10m / 10m");
        second.setFeaturedRider1Name("田中次郎");
        second.setFeaturedRider1Number(2);
        second.setFeaturedRider1Mark("◎");
        second.setFeaturedRider2Name("高橋未来");
        second.setFeaturedRider2Number(6);
        second.setFeaturedRider2Mark("▲");
        second.setFeaturedRider3Name("小林優子");
        second.setFeaturedRider3Number(1);
        second.setFeaturedRider3Mark("○");
        second.setPreRacePrediction("2号車の試走とスタートを軸に考える。");
        second.setPreRaceNote("湿走路なら差しより先行力を優先。");
        second.setWindSpeed("4.0");
        second.setTrialTime1("3.40");
        second.setTrialTime2("3.31");
        second.setTrialTime3("3.38");
        second.setTrialTime4("3.42");
        second.setTrialTime5("3.39");
        second.setTrialTime6("3.36");
        second.setTrialTime7("3.41");
        second.setTrialTime8("3.43");
        second.setFeaturedTrialTimeNote("2号車が最上位の試走で気配良好。");
        second.setRaceOverallNote("湿走路なので無理な捲りは届きにくい想定。");
        second.setBetLine1Type("2連単");
        second.setBetLine1Count(3);
        second.setBetLine1Amount(600);
        second.setPayoutAmount(0);
        second.setPredictionNote("点数を絞って回収重視にした。");
        second.setRaceResult("結果は2-6。");
        second.setResultComparison("軸選びは良かったが相手評価に課題あり。");
        second.setReviewNote("湿走路の時は先行力をさらに重視したい。");
        calculateBetAmounts(second);
        setLegacyFeaturedRider(second);

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
        return value != null && value.contains(keyword);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}

