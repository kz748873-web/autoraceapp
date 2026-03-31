package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.service.RaceRecordService;

/**
 * レース記録画面に関するリクエストを受け取るコントローラです。
 */
@Controller
@RequestMapping("/records")
public class RaceRecordController {

    private final RaceRecordService raceRecordService;

    public RaceRecordController(RaceRecordService raceRecordService) {
        this.raceRecordService = raceRecordService;
    }

    /**
     * 一覧画面を表示します。
     */
    @GetMapping
    public String showList(Model model) {
        model.addAttribute("records", raceRecordService.findAll());
        return "records/list";
    }

    /**
     * 登録画面を表示します。
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("record", raceRecordService.createEmptyRecord());
        return "records/form";
    }

    /**
     * 入力されたレース記録を保存して一覧画面へ戻します。
     */
    @PostMapping
    public String saveRecord(@ModelAttribute RaceRecord record) {
        raceRecordService.save(record);
        return "redirect:/records";
    }

    /**
     * 詳細画面を表示します。
     */
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        RaceRecord record = raceRecordService.findById(id);
        if (record == null) {
            return "redirect:/records";
        }
        model.addAttribute("record", record);
        return "records/detail";
    }
}