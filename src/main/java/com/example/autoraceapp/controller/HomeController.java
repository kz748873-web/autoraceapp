package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面を表示するためのコントローラです。
 * 最初に "/" へアクセスしたとき、このクラスがテンプレートを返します。
 */
@Controller
public class HomeController {

	/**
	 * トップページへの GET リクエストを受け取り、
	 * src/main/resources/templates/home/index.html を表示します。
	 *
	 * @return 表示する Thymeleaf テンプレート名
	 */
	@GetMapping("/")
	public String showHomePage() {
		return "home/index";
	}
}
