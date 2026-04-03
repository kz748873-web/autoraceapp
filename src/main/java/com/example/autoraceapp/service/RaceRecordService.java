package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.repository.RaceRecordRepository;

/**
 * 繝ｬ繝ｼ繧ｹ險倬鹸縺ｮ蜿門ｾ励・菫晏ｭ倥・讀懃ｴ｢繧呈球蠖薙☆繧九し繝ｼ繝薙せ縺ｧ縺吶・ */
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
                "蟾晏哨",
                8,
                "譎ｴ繧・,
                "22邃・,
                "48%",
                "31邃・,
                "濶ｯ襍ｰ霍ｯ",
                "笳・螟ｧ驛ｽ螟ｪ驛・/ 笳・菴占陸闃ｱ蟄・/ 笆ｳ 鬮俶ｩ倶ｸ驛・,
                "3騾｣蜊・,
                "1-2-3",
                "繧ｹ繧ｿ繝ｼ繝磯㍾隕悶〒遒ｺ隱阪＠縺溘＞繝ｬ繝ｼ繧ｹ縺ｧ縺吶・
        );
        first.setFeaturedRider1Name("螟ｧ驛ｽ螟ｪ驛・);
        first.setFeaturedRider1Number(1);
        first.setFeaturedRider1Mark("笳・);
        first.setFeaturedRider2Name("菴占陸闃ｱ蟄・);
        first.setFeaturedRider2Number(3);
        first.setFeaturedRider2Mark("笳・);
        first.setFeaturedRider3Name("鬮俶ｩ倶ｸ驛・);
        first.setFeaturedRider3Number(5);
        first.setFeaturedRider3Mark("笆ｳ");
        first.setPreRacePrediction("蜀・棧荳ｭ蠢・〒邨・∩遶九※縺溘＞縲・);
        first.setPreRaceNote("蜑肴律繧ｪ繝・ぜ縺ｧ縺ｯ1蜿ｷ霆贋ｸｭ蠢・・);
        first.setWindSpeed("2m");
        first.setTrialTime1("3.32");
        first.setTrialTime2("3.35");
        first.setTrialTime3("3.34");
        first.setTrialTime4("3.37");
        first.setTrialTime5("3.33");
        first.setTrialTime6("3.39");
        first.setTrialTime7("3.40");
        first.setTrialTime8("3.38");
        first.setFeaturedTrialTimeNote("1蜿ｷ霆翫→5蜿ｷ霆翫・豌鈴・縺瑚憶縺・・);
        first.setRaceOverallNote("繧ｹ繧ｿ繝ｼ繝医〒荳ｻ蟆取ｨｩ繧貞叙繧後ｋ縺九′繝昴う繝ｳ繝医・);
        first.setBetCount(6);
        first.setUnitBetAmount(100);
        first.setPurchaseAmount(600);
        first.setTotalBetAmount(600);
        first.setPredictionNote("隧ｦ襍ｰ繧定ｦ九※1蜿ｷ霆贋ｸｭ蠢・↓隱ｿ謨ｴ縲・);
        first.setRaceResult("邨先棡縺ｯ1-2-3縲・);
        first.setResultComparison("譛ｬ蜻ｽ縺ｨ邨先棡縺御ｸ閾ｴ縺励◆縲・);
        first.setReviewNote("襍ｰ霍ｯ迥ｶ諷九・隱ｭ縺ｿ繧り憶縺九▲縺溘・);

        RaceRecord second = new RaceRecord(
                2L,
                "2026-03-30",
                "豬懈收",
                10,
                "譖・ｊ",
                "18邃・,
                "62%",
                "24邃・,
                "譁題ｵｰ霍ｯ",
                "笳・髱貞ｱｱ谺｡驛・/ 豕ｨ 荳ｭ譚台ｸ讓ｹ / 笳・螻ｱ逕ｰ蛛･螟ｪ",
                "2騾｣蜊・,
                "2-1",
                "隧ｦ襍ｰ豌鈴・繧定ｦ九※雋ｷ縺・岼繧堤ｵ槭ｊ縺溘＞縲・
        );
        second.setFeaturedRider1Name("髱貞ｱｱ谺｡驛・);
        second.setFeaturedRider1Number(2);
        second.setFeaturedRider1Mark("笳・);
        second.setFeaturedRider2Name("荳ｭ譚台ｸ讓ｹ");
        second.setFeaturedRider2Number(6);
        second.setFeaturedRider2Mark("豕ｨ");
        second.setFeaturedRider3Name("螻ｱ逕ｰ蛛･螟ｪ");
        second.setFeaturedRider3Number(1);
        second.setFeaturedRider3Mark("笳・);
        second.setPreRacePrediction("2蜿ｷ霆翫・螳牙ｮ壽─繧帝㍾隕悶・);
        second.setPreRaceNote("螟ｩ蛟吝､牙喧縺後≠繧後・蜀肴､懆ｨ弱・);
        second.setWindSpeed("4m");
        second.setTrialTime1("3.40");
        second.setTrialTime2("3.31");
        second.setTrialTime3("3.38");
        second.setTrialTime4("3.42");
        second.setTrialTime5("3.39");
        second.setTrialTime6("3.36");
        second.setTrialTime7("3.41");
        second.setTrialTime8("3.43");
        second.setFeaturedTrialTimeNote("2蜿ｷ霆翫′謚懊￠縺ｦ濶ｯ縺・・);
        second.setRaceOverallNote("譁題ｵｰ霍ｯ縺ｪ縺ｮ縺ｧ螟悶・蜍輔″縺ｫ豕ｨ諢上・);
        second.setBetCount(3);
        second.setUnitBetAmount(200);
        second.setPurchaseAmount(600);
        second.setTotalBetAmount(600);
        second.setPredictionNote("轤ｹ謨ｰ繧堤ｵ槭▲縺ｦ蜍晁ｲ縲・);
        second.setRaceResult("邨先棡縺ｯ2-6縲・);
        second.setResultComparison("譛ｬ蜻ｽ縺ｯ譚･縺溘′逶ｸ謇九′諠ｳ螳壼､悶・);
        second.setReviewNote("豕ｨ隧穂ｾ｡縺ｮ驕ｸ謇九ｒ繧ゅ≧蟆代＠荳翫￡縺ｦ繧ゅｈ縺九▲縺溘・);

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


