package com.example.autoraceapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 外部リンクを管理する Entity です。
 *
 * 今後リンクが増えても扱いやすいように、
 * カテゴリ、リンク種別、会場名、表示順などを持てる形にしています。
 */
@Entity
public class SiteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 画面に表示するサイト名です。
     */
    private String siteName;

    /**
     * どのカテゴリのリンクかを表します。
     * 例: OFFICIAL / FANCLUB / VENUE / YOUTUBE / SNS
     */
    @Enumerated(EnumType.STRING)
    private SiteLinkCategory category;

    /**
     * 遷移先 URL です。
     */
    private String url;

    /**
     * レース場名を持たせたいときに使います。
     * 例: 川口 / 伊勢崎 / 浜松 / 飯塚 / 山陽
     */
    private String venueName;

    /**
     * 同じカテゴリ内での表示順です。
     */
    private Integer displayOrder;

    /**
     * 補足説明です。
     */
    private String note;

    /**
     * そのカテゴリの中で主導線として見せたいリンクかどうかです。
     */
    private Boolean isPrimary;

    /**
     * リンクの種類です。
     * 例: WEB / YOUTUBE / SNS
     */
    @Enumerated(EnumType.STRING)
    private SiteLinkType linkType;

    public SiteLink() {
    }

    public SiteLink(
            String siteName,
            SiteLinkCategory category,
            String url,
            String venueName,
            Integer displayOrder,
            String note,
            Boolean isPrimary,
            SiteLinkType linkType) {
        this.siteName = siteName;
        this.category = category;
        this.url = url;
        this.venueName = venueName;
        this.displayOrder = displayOrder;
        this.note = note;
        this.isPrimary = isPrimary;
        this.linkType = linkType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public SiteLinkCategory getCategory() {
        return category;
    }

    public void setCategory(SiteLinkCategory category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public SiteLinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(SiteLinkType linkType) {
        this.linkType = linkType;
    }

    /**
     * 既存テンプレートとの互換用です。
     */
    public String getDisplayName() {
        return siteName;
    }

    public void setDisplayName(String displayName) {
        this.siteName = displayName;
    }

    /**
     * 既存 Service との互換用です。
     */
    public Integer getSortOrder() {
        return displayOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.displayOrder = sortOrder;
    }

    /**
     * 画面表示用のカテゴリ名です。
     */
    public String getCategoryLabel() {
        return category != null ? category.getLabel() : "未分類";
    }

    /**
     * 画面表示用のリンク種別名です。
     */
    public String getLinkTypeLabel() {
        return linkType != null ? linkType.getLabel() : "";
    }

    /**
     * Thymeleaf で扱いやすいように boolean 風の getter も用意しています。
     */
    public boolean isPrimary() {
        return Boolean.TRUE.equals(isPrimary);
    }
}
