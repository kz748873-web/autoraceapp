package com.example.autoraceapp.entity;

/**
 * 外部リンクの種類です。
 * WEB / YouTube / SNS のように、リンクの用途を整理するために使います。
 */
public enum SiteLinkType {

    WEB("WEB"),
    YOUTUBE("YouTube"),
    SNS("SNS");

    private final String label;

    SiteLinkType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
