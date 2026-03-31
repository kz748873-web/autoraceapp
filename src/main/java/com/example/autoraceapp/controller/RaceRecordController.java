package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.service.RaceRecordService;

@Controller
@RequestMapping("/records")
public class RaceRecordController {

    private final RaceRecordService raceRecordService;

    public RaceRecordController(RaceRecordService raceRecordService) {
        this.raceRecordService = raceRecordService;
    }

    @GetMapping
    public String showList(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String weatherCondition,
            @RequestParam(required = false) String featuredRider,
            Model model) {

        model.addAttribute("records", raceRecordService.findRecords(venue, weatherCondition, featuredRider));
        model.addAttribute("selectedVenue", venue);
        model.addAttribute("weatherCondition", weatherCondition);
        model.addAttribute("featuredRider", featuredRider);
        return "records/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("record", raceRecordService.createEmptyRecord());
        return "records/form";
    }

    @PostMapping
    public String saveRecord(@ModelAttribute RaceRecord record) {
        raceRecordService.save(record);
        return "redirect:/records";
    }

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