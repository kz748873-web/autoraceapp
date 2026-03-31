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
            if (!isMatch(record.getFeaturedRider(), featuredRider)) {
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
        return new RaceRecord(null, "", "", null, "", "", "", "", "", "", "", "", "");
    }

    public RaceRecord save(RaceRecord raceRecord) {
        return raceRecordRepository.save(raceRecord);
    }

    private List<RaceRecord> createDummyRecords() {
        List<RaceRecord> dummyRecords = new ArrayList<>();

        dummyRecords.add(new RaceRecord(
                1L,
                "2026-03-31",
                "\u5DDD\u53E3",
                8,
                "\u6674\u308C",
                "22\u2103",
                "48%",
                "31\u2103",
                "\u826F\u8D70\u8DEF",
                "\u5927\u90FD\u592A\u90CE",
                "3\u9023\u5358",
                "1-2-3",
                "\u30B9\u30BF\u30FC\u30C8\u91CD\u8996\u3067\u78BA\u8A8D\u3057\u305F\u3044\u30EC\u30FC\u30B9\u3067\u3059\u3002"));

        dummyRecords.add(new RaceRecord(
                2L,
                "2026-03-30",
                "\u6D5C\u677E",
                10,
                "\u66C7\u308A",
                "18\u2103",
                "62%",
                "24\u2103",
                "\u6591\u8D70\u8DEF",
                "\u5927\u90FD\u592A\u90CE",
                "2\u9023\u5358",
                "2-1",
                "\u8A66\u8D70\u6C17\u914D\u3092\u898B\u3066\u8CB7\u3044\u76EE\u3092\u7D5E\u308B\u4E88\u5B9A\u3067\u3059\u3002"));

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
}