package com.example.autoraceapp.entity;

/**
 * 外部リンクのカテゴリです。
 * 画面では日本語ラベルを使って見やすく表示します。
 */
public enum SiteLinkCategory {

    OFFICIAL("公式"),
    FANCLUB("ファンクラブ"),
    VENUE("レース場"),
    YOUTUBE("YouTube"),
    SNS("SNS");

    private final String label;

    SiteLinkCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
