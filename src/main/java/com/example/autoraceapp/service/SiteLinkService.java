package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.SiteLink;
import com.example.autoraceapp.entity.SiteLinkCategory;
import com.example.autoraceapp.entity.SiteLinkType;
import com.example.autoraceapp.repository.SiteLinkRepository;

/**
 * 外部リンクをカテゴリごとに整理して扱うサービスです。
 *
 * 今後 YouTube や SNS が増えても、
 * SiteLink にデータを追加するだけで画面に出しやすい形を目指しています。
 */
@Service
public class SiteLinkService {

    private final SiteLinkRepository siteLinkRepository;

    public SiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }

    /**
     * 画面表示用に、カテゴリごとのリンク一覧を返します。
     */
    public Map<String, List<SiteLink>> getLinkGroups() {
        initializeIfEmpty();

        Map<String, List<SiteLink>> groupedLinks = createEmptyGroups();
        List<SiteLink> links = new ArrayList<>(siteLinkRepository.findAll());
        links.sort(createLinkComparator());

        for (SiteLink link : links) {
            groupedLinks.computeIfAbsent(link.getCategoryLabel(), key -> new ArrayList<>()).add(link);
        }
        return groupedLinks;
    }

    /**
     * 初回起動時に、最低限の公式リンクとレース場リンクを登録します。
     */
    private void initializeIfEmpty() {
        if (siteLinkRepository.count() > 0) {
            return;
        }

        List<SiteLink> initialLinks = new ArrayList<>();

        initialLinks.add(new SiteLink(
                "AutoRace.JP投票",
                SiteLinkCategory.OFFICIAL,
                "https://www.autorace.jp/",
                null,
                1,
                "投票や開催情報を確認するための公式サイトです。",
                true,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "AutoRace FANCLUB",
                SiteLinkCategory.FANCLUB,
                "https://autorace-fanclub.jp/",
                null,
                1,
                "ファンクラブ向けの情報を確認できます。",
                true,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "川口オートレース場",
                SiteLinkCategory.VENUE,
                "https://www.kawaguchiauto.jp/",
                "川口",
                1,
                "川口オートレース場の公式サイトです。",
                true,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "伊勢崎オートレース場",
                SiteLinkCategory.VENUE,
                "https://isesaki-auto.jp/",
                "伊勢崎",
                2,
                "伊勢崎オートレース場の公式サイトです。",
                false,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "浜松オートレース場",
                SiteLinkCategory.VENUE,
                "https://www.hamamatsuauto.jp/",
                "浜松",
                3,
                "浜松オートレース場の公式サイトです。",
                false,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "飯塚オートレース場",
                SiteLinkCategory.VENUE,
                "https://www.iizuka-auto.jp/",
                "飯塚",
                4,
                "飯塚オートレース場の公式サイトです。",
                false,
                SiteLinkType.WEB));

        initialLinks.add(new SiteLink(
                "山陽オートレース場",
                SiteLinkCategory.VENUE,
                "https://sanyoauto.jp/",
                "山陽",
                5,
                "山陽オートレース場の公式サイトです。",
                false,
                SiteLinkType.WEB));

        siteLinkRepository.saveAll(initialLinks);
    }

    /**
     * 画面上のカテゴリ表示順を固定します。
     */
    private Map<String, List<SiteLink>> createEmptyGroups() {
        Map<String, List<SiteLink>> groupedLinks = new LinkedHashMap<>();
        groupedLinks.put(SiteLinkCategory.OFFICIAL.getLabel(), new ArrayList<>());
        groupedLinks.put(SiteLinkCategory.FANCLUB.getLabel(), new ArrayList<>());
        groupedLinks.put(SiteLinkCategory.VENUE.getLabel(), new ArrayList<>());
        groupedLinks.put(SiteLinkCategory.YOUTUBE.getLabel(), new ArrayList<>());
        groupedLinks.put(SiteLinkCategory.SNS.getLabel(), new ArrayList<>());
        return groupedLinks;
    }

    /**
     * カテゴリ順 → 表示順 → サイト名の順で並べます。
     */
    private Comparator<SiteLink> createLinkComparator() {
        return Comparator
                .comparingInt((SiteLink link) -> getCategoryOrder(link.getCategory()))
                .thenComparing(link -> link.getDisplayOrder() != null ? link.getDisplayOrder() : Integer.MAX_VALUE)
                .thenComparing(link -> link.getSiteName() != null ? link.getSiteName() : "");
    }

    private int getCategoryOrder(SiteLinkCategory category) {
        if (category == null) {
            return 99;
        }

        return switch (category) {
            case OFFICIAL -> 1;
            case FANCLUB -> 2;
            case VENUE -> 3;
            case YOUTUBE -> 4;
            case SNS -> 5;
        };
    }
}
