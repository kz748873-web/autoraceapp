package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.SiteLink;
import com.example.autoraceapp.entity.SiteLinkCategory;
import com.example.autoraceapp.entity.SiteLinkType;
import com.example.autoraceapp.repository.SiteLinkRepository;

/**
 * 外部リンクをカテゴリごと、会場ごとに整理して扱うサービスです。
 */
@Service
public class SiteLinkService {

    private static final String SERVICE_OFFICIAL_SITE = "公式サイト";
    private static final String SERVICE_FANCLUB = "ファンクラブ";
    private static final String SERVICE_YOUTUBE = "YouTube";
    private static final String SERVICE_X = "X";
    private static final String SERVICE_INSTAGRAM = "Instagram";
    private static final String DEFAULT_VENUE = "川口";

    private final SiteLinkRepository siteLinkRepository;

    public SiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }

    public List<SiteLink> getOfficialRelatedLinks() {
        initializeIfEmpty();

        return siteLinkRepository.findAll().stream()
                .filter(link -> link.getCategory() == SiteLinkCategory.OFFICIAL
                        || link.getCategory() == SiteLinkCategory.FANCLUB)
                .sorted(createLinkComparator())
                .collect(Collectors.toList());
    }

    public List<String> getVenueNames() {
        initializeIfEmpty();

        return siteLinkRepository.findAll().stream()
                .filter(link -> link.getCategory() == SiteLinkCategory.VENUE)
                .sorted(createLinkComparator())
                .map(SiteLink::getVenueName)
                .filter(name -> name != null && !name.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    public String resolveSelectedVenue(String requestedVenue) {
        List<String> venueNames = getVenueNames();
        if (requestedVenue == null || requestedVenue.isBlank()) {
            return venueNames.isEmpty() ? DEFAULT_VENUE : venueNames.get(0);
        }

        for (String venueName : venueNames) {
            if (venueName.equals(requestedVenue)) {
                return venueName;
            }
        }

        return venueNames.isEmpty() ? DEFAULT_VENUE : venueNames.get(0);
    }

    public Map<String, SiteLink> getVenueDetailLinks(String venueName) {
        initializeIfEmpty();

        String selectedVenue = resolveSelectedVenue(venueName);
        Map<String, SiteLink> detailLinks = createEmptyVenueDetailMap();

        siteLinkRepository.findAll().stream()
                .filter(link -> selectedVenue.equals(link.getVenueName()))
                .sorted(createLinkComparator())
                .forEach(link -> {
                    String serviceName = link.getServiceName();
                    if (SERVICE_OFFICIAL_SITE.equals(serviceName)) {
                        detailLinks.put("officialSite", link);
                    } else if (SERVICE_YOUTUBE.equals(serviceName)) {
                        detailLinks.put("youtube", link);
                    } else if (SERVICE_X.equals(serviceName)) {
                        detailLinks.put("x", link);
                    } else if (SERVICE_INSTAGRAM.equals(serviceName)) {
                        detailLinks.put("instagram", link);
                    } else {
                        detailLinks.put("supplement", link);
                    }
                });

        return detailLinks;
    }

    private Map<String, SiteLink> createEmptyVenueDetailMap() {
        Map<String, SiteLink> detailLinks = new LinkedHashMap<>();
        detailLinks.put("officialSite", null);
        detailLinks.put("youtube", null);
        detailLinks.put("x", null);
        detailLinks.put("instagram", null);
        detailLinks.put("supplement", null);
        return detailLinks;
    }

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
                SiteLinkType.WEB,
                SERVICE_OFFICIAL_SITE));

        initialLinks.add(new SiteLink(
                "AutoRace FANCLUB",
                SiteLinkCategory.FANCLUB,
                "https://autorace-fanclub.jp/",
                null,
                2,
                "ファンクラブ向けの情報を確認できます。",
                false,
                SiteLinkType.WEB,
                SERVICE_FANCLUB));

        initialLinks.add(new SiteLink(
                "オートレース公式YouTube",
                SiteLinkCategory.YOUTUBE,
                "https://www.youtube.com/channel/UCJAPqcMNYk3HMknRuPMNvKA",
                null,
                1,
                "公式配信や動画を確認するための公式YouTubeです。",
                true,
                SiteLinkType.YOUTUBE,
                SERVICE_YOUTUBE));

        addVenueLinks(initialLinks, "川口", "https://www.kawaguchiauto.jp/", "https://www.youtube.com/channel/UCM3t31-x3BYX9s2xGkIqk8w", 1);
        addVenueLinks(initialLinks, "伊勢崎", "https://isesaki-auto.jp/", "https://www.youtube.com/channel/UCVytM2dZnTp6oOfBpv7_jLg", 2);
        addVenueLinks(initialLinks, "浜松", "https://www.hamamatsuauto.jp/", "https://www.youtube.com/user/hamamatsuauto", 3);
        addVenueLinks(initialLinks, "飯塚", "https://www.iizuka-auto.jp/", "https://www.youtube.com/channel/UCUwiFvrK2oCwwSA-1SVtOLA", 4);
        addVenueLinks(initialLinks, "山陽", "https://sanyoauto.jp/", "https://www.youtube.com/channel/UCGercgXIY9l1H5r45XqQPEA", 5);

        siteLinkRepository.saveAll(initialLinks);
    }

    private void addVenueLinks(List<SiteLink> links, String venueName, String officialUrl, String youtubeUrl, int order) {
        links.add(new SiteLink(
                venueName + "オートレース場",
                SiteLinkCategory.VENUE,
                officialUrl,
                venueName,
                order,
                venueName + "の公式サイトです。",
                true,
                SiteLinkType.WEB,
                SERVICE_OFFICIAL_SITE));

        links.add(new SiteLink(
                venueName + " YouTube",
                SiteLinkCategory.YOUTUBE,
                youtubeUrl,
                venueName,
                order,
                venueName + "の配信や動画を確認するためのYouTubeです。",
                true,
                SiteLinkType.YOUTUBE,
                SERVICE_YOUTUBE));

        links.add(new SiteLink(
                venueName + " X",
                SiteLinkCategory.SNS,
                "#",
                venueName,
                order,
                venueName + "のXリンクは後から設定できます。",
                false,
                SiteLinkType.SNS,
                SERVICE_X));

        links.add(new SiteLink(
                venueName + " Instagram",
                SiteLinkCategory.SNS,
                "#",
                venueName,
                order,
                venueName + "のInstagramリンクは後から設定できます。",
                false,
                SiteLinkType.SNS,
                SERVICE_INSTAGRAM));
    }

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
