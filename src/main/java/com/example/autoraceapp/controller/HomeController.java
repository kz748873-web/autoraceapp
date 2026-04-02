package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面と共通リソースへの導線を扱うコントローラです。
 */
@Controller
public class HomeController {

    /**
     * トップページを表示します。
     */
    @GetMapping("/")
    public String showHomePage() {
        return "home/index";
    }

    /**
     * ブラウザが自動で要求する favicon.ico を SVG に転送します。
     */
    @GetMapping("/favicon.ico")
    public String redirectFavicon() {
        return "forward:/favicon.svg";
    }
}
