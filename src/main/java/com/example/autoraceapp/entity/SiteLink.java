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
 * カテゴリ、リンク種別、会場名、表示順に加えて、
 * そのリンクが何の役割を持つかを serviceName で表せるようにしています。
 */
@Entity
public class SiteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 画面に表示する名前です。
     */
    private String siteName;

    /**
     * リンクのカテゴリです。
     */
    @Enumerated(EnumType.STRING)
    private SiteLinkCategory category;

    /**
     * 遷移先 URL です。
     */
    private String url;

    /**
     * 会場別リンクに使う会場名です。
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
     * 主要導線として見せたいリンクかどうかです。
     */
    private Boolean isPrimary;

    /**
     * WEB / YOUTUBE / SNS などのリンク種別です。
     */
    @Enumerated(EnumType.STRING)
    private SiteLinkType linkType;

    /**
     * そのリンクの役割です。
     * 例: 公式サイト / ファンクラブ / YouTube / X / Instagram
     */
    private String serviceName;

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
            SiteLinkType linkType,
            String serviceName) {
        this.siteName = siteName;
        this.category = category;
        this.url = url;
        this.venueName = venueName;
        this.displayOrder = displayOrder;
        this.note = note;
        this.isPrimary = isPrimary;
        this.linkType = linkType;
        this.serviceName = serviceName;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDisplayName() {
        return siteName;
    }

    public void setDisplayName(String displayName) {
        this.siteName = displayName;
    }

    public Integer getSortOrder() {
        return displayOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.displayOrder = sortOrder;
    }

    public String getCategoryLabel() {
        return category != null ? category.getLabel() : "未分類";
    }

    public String getLinkTypeLabel() {
        return linkType != null ? linkType.getLabel() : "";
    }

    public boolean isPrimary() {
        return Boolean.TRUE.equals(isPrimary);
    }
}
