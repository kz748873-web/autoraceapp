package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.repository.RaceRecordRepository;

/**
 * レース記録に関する処理をまとめるサービスです。
 * 今回は最小構成なので、DBにデータがない場合はダミーデータを返します。
 */
@Service
public class RaceRecordService {

    private final RaceRecordRepository raceRecordRepository;

    public RaceRecordService(RaceRecordRepository raceRecordRepository) {
        this.raceRecordRepository = raceRecordRepository;
    }

    /**
     * 一覧画面に表示するデータを返します。
     */
    public List<RaceRecord> findAll() {
        List<RaceRecord> records = raceRecordRepository.findAll();
        if (!records.isEmpty()) {
            return records;
        }

        List<RaceRecord> dummyRecords = new ArrayList<>();
        dummyRecords.add(new RaceRecord(1L, "2026-03-31", "川口", 8, "晴", "青山 周平", "1-2-3"));
        dummyRecords.add(new RaceRecord(2L, "2026-03-30", "浜松", 10, "曇", "鈴木 圭一郎", "2-1-5"));
        return dummyRecords;
    }

    /**
     * 詳細画面用に1件だけ取り出します。
     */
    public RaceRecord findById(Long id) {
        Optional<RaceRecord> record = raceRecordRepository.findById(id);
        if (record.isPresent()) {
            return record.get();
        }

        return findAll().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * 登録画面で使う空のデータです。
     */
    public RaceRecord createEmptyRecord() {
        return new RaceRecord(null, "", "", null, "", "", "");
    }

    /**
     * 入力されたレース記録を保存します。
     */
    public RaceRecord save(RaceRecord raceRecord) {
        return raceRecordRepository.save(raceRecord);
    }
}