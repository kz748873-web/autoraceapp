package com.example.autoraceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autoraceapp.entity.RaceRecord;

/**
 * レース記録を扱うためのリポジトリです。
 * 基本的な保存や検索はJpaRepositoryが用意してくれます。
 */
public interface RaceRecordRepository extends JpaRepository<RaceRecord, Long> {
}