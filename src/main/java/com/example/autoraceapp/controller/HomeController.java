package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.autoraceapp.service.SiteLinkService;

/**
 * ホーム画面と共通リソース、外部リンク画面への導線を扱うコントローラです。
 */
@Controller
public class HomeController {

    private final SiteLinkService siteLinkService;

    public HomeController(SiteLinkService siteLinkService) {
        this.siteLinkService = siteLinkService;
    }

    /**
     * トップページを表示します。
     */
    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("linkGroups", siteLinkService.getLinkGroups());
        return "home/index";
    }

    /**
     * 外部リンク一覧ページを表示します。
     */
    @GetMapping("/links")
    public String showLinksPage(Model model) {
        model.addAttribute("linkGroups", siteLinkService.getLinkGroups());
        return "home/links";
    }

    /**
     * ブラウザが自動で要求する favicon.ico を SVG に転送します。
     */
    @GetMapping("/favicon.ico")
    public String redirectFavicon() {
        return "forward:/favicon.svg";
    }
}
