package com.example.autoraceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autoraceapp.entity.SiteLink;

/**
 * 外部リンクの保存・取得を行う Repository です。
 */
public interface SiteLinkRepository extends JpaRepository<SiteLink, Long> {
}
