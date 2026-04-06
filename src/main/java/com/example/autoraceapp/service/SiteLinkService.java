package com.example.autoraceapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.autoraceapp.entity.SiteLink;
import com.example.autoraceapp.entity.SiteLinkCategory;
import com.example.autoraceapp.entity.SiteLinkType;
import com.example.autoraceapp.repository.SiteLinkRepository;

@Service
public class SiteLinkService {

    private static final String SERVICE_OFFICIAL_SITE = "公式サイト";
    private static final String SERVICE_FANCLUB = "公式FANCLUB";
    private static final String SERVICE_YOUTUBE = "YouTube";
    private static final String SERVICE_INSTAGRAM = "Instagram";
    private static final String SERVICE_FACEBOOK = "Facebook";
    private static final String DEFAULT_VENUE = "川口";

    private final SiteLinkRepository siteLinkRepository;

    public SiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }

    public List<SiteLink> getOfficialRelatedLinks() {
        syncDefaultLinks();
        return siteLinkRepository.findAll().stream()
                .filter(link -> link.getVenueName() == null || link.getVenueName().isBlank())
                .filter(link -> link.getCategory() == SiteLinkCategory.OFFICIAL || link.getCategory() == SiteLinkCategory.FANCLUB)
                .sorted(createLinkComparator())
                .collect(Collectors.toList());
    }

    public List<String> getVenueNames() {
        syncDefaultLinks();
        return siteLinkRepository.findAll().stream()
                .filter(link -> link.getVenueName() != null && !link.getVenueName().isBlank())
                .sorted(createLinkComparator())
                .map(SiteLink::getVenueName)
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
        syncDefaultLinks();

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
                    } else if (SERVICE_INSTAGRAM.equals(serviceName)) {
                        detailLinks.put("instagram", link);
                    } else if (SERVICE_FACEBOOK.equals(serviceName)) {
                        detailLinks.put("facebook", link);
                    }
                });

        return detailLinks;
    }

    private Map<String, SiteLink> createEmptyVenueDetailMap() {
        Map<String, SiteLink> detailLinks = new LinkedHashMap<>();
        detailLinks.put("officialSite", null);
        detailLinks.put("youtube", null);
        detailLinks.put("instagram", null);
        detailLinks.put("facebook", null);
        return detailLinks;
    }

    private void syncDefaultLinks() {
        List<SiteLink> defaults = buildDefaultLinks();
        List<SiteLink> existingLinks = siteLinkRepository.findAll();
        Map<String, SiteLink> existingByKey = existingLinks.stream()
                .collect(Collectors.toMap(this::buildKey, link -> link, (left, right) -> left, LinkedHashMap::new));

        List<SiteLink> linksToSave = new ArrayList<>();
        Set<String> defaultKeys = new LinkedHashSet<>();

        for (SiteLink defaultLink : defaults) {
            String key = buildKey(defaultLink);
            defaultKeys.add(key);
            SiteLink existing = existingByKey.get(key);
            if (existing == null) {
                linksToSave.add(defaultLink);
            } else if (needsUpdate(existing, defaultLink)) {
                existing.setSiteName(defaultLink.getSiteName());
                existing.setCategory(defaultLink.getCategory());
                existing.setUrl(defaultLink.getUrl());
                existing.setVenueName(defaultLink.getVenueName());
                existing.setDisplayOrder(defaultLink.getDisplayOrder());
                existing.setNote(defaultLink.getNote());
                existing.setIsPrimary(defaultLink.getIsPrimary());
                existing.setLinkType(defaultLink.getLinkType());
                existing.setServiceName(defaultLink.getServiceName());
                linksToSave.add(existing);
            }
        }

        if (!linksToSave.isEmpty()) {
            siteLinkRepository.saveAll(linksToSave);
        }

        List<SiteLink> obsoleteLinks = existingLinks.stream()
                .filter(link -> isManagedLink(link) && !defaultKeys.contains(buildKey(link)))
                .collect(Collectors.toList());
        if (!obsoleteLinks.isEmpty()) {
            siteLinkRepository.deleteAll(obsoleteLinks);
        }
    }

    private boolean isManagedLink(SiteLink link) {
        String serviceName = link.getServiceName();
        return SERVICE_OFFICIAL_SITE.equals(serviceName)
                || SERVICE_FANCLUB.equals(serviceName)
                || SERVICE_YOUTUBE.equals(serviceName)
                || SERVICE_INSTAGRAM.equals(serviceName)
                || SERVICE_FACEBOOK.equals(serviceName)
                || "X".equals(serviceName);
    }

    private boolean needsUpdate(SiteLink existing, SiteLink defaults) {
        return !safeEquals(existing.getSiteName(), defaults.getSiteName())
                || existing.getCategory() != defaults.getCategory()
                || !safeEquals(existing.getUrl(), defaults.getUrl())
                || !safeEquals(existing.getVenueName(), defaults.getVenueName())
                || !safeEquals(existing.getDisplayOrder(), defaults.getDisplayOrder())
                || !safeEquals(existing.getNote(), defaults.getNote())
                || !safeEquals(existing.getIsPrimary(), defaults.getIsPrimary())
                || existing.getLinkType() != defaults.getLinkType()
                || !safeEquals(existing.getServiceName(), defaults.getServiceName());
    }

    private String buildKey(SiteLink link) {
        return (link.getVenueName() == null ? "GLOBAL" : link.getVenueName()) + "::" + link.getServiceName();
    }

    private boolean safeEquals(Object left, Object right) {
        return left == null ? right == null : left.equals(right);
    }

    private List<SiteLink> buildDefaultLinks() {
        List<SiteLink> links = new ArrayList<>();

        links.add(createLink(
                "AutoRace.jp",
                SiteLinkCategory.OFFICIAL,
                "https://autorace.jp/",
                null,
                1,
                "オートレース公式サイトです。開催情報や結果確認に使えます。",
                true,
                SiteLinkType.WEB,
                SERVICE_OFFICIAL_SITE));

        links.add(createLink(
                "公式FANCLUB",
                SiteLinkCategory.FANCLUB,
                "https://fc.autorace.jp/?_gl=1*121xj0r*_gcl_au*MTY3OTY0MDM2LjE3NzUxMDI4NjA.*_ga*NTk4Nzk4MTk0LjE3NzUxMDI4NjA.*_ga_37DP3YV65V*czE3NzUxODI3NzUkbzIkZzAkdDE3NzUxODI3NzUkajYwJGwwJGgxODkzNzM2Mzkx",
                null,
                2,
                "公式FANCLUBの入口です。会員向け情報の確認に使えます。",
                true,
                SiteLinkType.WEB,
                SERVICE_FANCLUB));

        addVenueLinks(links, "川口", "https://www.kawaguchiauto.jp/", "https://www.youtube.com/channel/UCM3t31-x3BYX9s2xGkIqk8w", "https://www.instagram.com/kawaguchiauto/", "https://www.facebook.com/kawaguchiauto/?locale=ja_JP", 1);
        addVenueLinks(links, "伊勢崎", "https://isesaki-auto.jp/", "https://www.youtube.com/channel/UCVytM2dZnTp6oOfBpv7_jLg", "https://www.instagram.com/isesaki_autorace/", "https://www.facebook.com/isesakiauto/", 2);
        addVenueLinks(links, "浜松", "https://www.hamamatsuauto.jp/", "https://www.youtube.com/user/hamamatsuauto", "https://www.instagram.com/hamamatsuauto_official/", "https://www.facebook.com/hamamatsu.autorace/?locale=ja_JP", 3);
        addVenueLinks(links, "飯塚", "https://www.iizuka-auto.jp/", "https://www.youtube.com/channel/UCUwiFvrK2oCwwSA-1SVtOLA", "https://www.instagram.com/iizuka_autorace/", "https://www.facebook.com/iizukaauto/?locale=ja_JP", 4);
        addVenueLinks(links, "山陽", "https://sanyoauto.jp/", "https://www.youtube.com/channel/UCGercgXIY9l1H5r45XqQPEA", "https://www.instagram.com/sanyo_auto/", "https://www.facebook.com/SANYOAUTORACE/", 5);

        return links;
    }

    private void addVenueLinks(List<SiteLink> links, String venueName, String officialUrl, String youtubeUrl, String instagramUrl, String facebookUrl, int order) {
        links.add(createLink(
                venueName + " 公式サイト",
                SiteLinkCategory.VENUE,
                officialUrl,
                venueName,
                order,
                venueName + "の公式サイトです。",
                true,
                SiteLinkType.WEB,
                SERVICE_OFFICIAL_SITE));

        links.add(createLink(
                venueName + " YouTube",
                SiteLinkCategory.YOUTUBE,
                youtubeUrl,
                venueName,
                order,
                venueName + "の動画リンクです。",
                true,
                SiteLinkType.YOUTUBE,
                SERVICE_YOUTUBE));

        links.add(createLink(
                venueName + " Instagram",
                SiteLinkCategory.SNS,
                instagramUrl,
                venueName,
                order,
                venueName + "のInstagramです。",
                true,
                SiteLinkType.SNS,
                SERVICE_INSTAGRAM));

        links.add(createLink(
                venueName + " Facebook",
                SiteLinkCategory.SNS,
                facebookUrl,
                venueName,
                order,
                venueName + "のFacebookです。",
                true,
                SiteLinkType.SNS,
                SERVICE_FACEBOOK));
    }

    private SiteLink createLink(String siteName, SiteLinkCategory category, String url, String venueName, int displayOrder, String note, boolean isPrimary, SiteLinkType linkType, String serviceName) {
        return new SiteLink(siteName, category, url, venueName, displayOrder, note, isPrimary, linkType, serviceName);
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
