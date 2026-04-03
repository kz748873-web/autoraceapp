package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.autoraceapp.entity.GlossaryTerm;
import com.example.autoraceapp.service.GlossaryService;

@Controller
@RequestMapping("/glossaries")
public class GlossaryController {

    private final GlossaryService glossaryService;

    public GlossaryController(GlossaryService glossaryService) {
        this.glossaryService = glossaryService;
    }

    @GetMapping
    public String showGlossaryPage(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("query", q == null ? "" : q);
        model.addAttribute("terms", glossaryService.findTerms(q));
        model.addAttribute("newTerm", glossaryService.createEmptyTerm());
        return "glossaries/list";
    }

    @PostMapping
    public String addGlossaryTerm(@ModelAttribute("newTerm") GlossaryTerm glossaryTerm) {
        glossaryService.addTerm(glossaryTerm);
        return "redirect:/glossaries";
    }

    @PostMapping("/{id}")
    public String updateGlossaryTerm(
            @PathVariable Long id,
            @RequestParam String term,
            @RequestParam(required = false) String meaning,
            @RequestParam(required = false) String memo,
            @RequestParam(required = false, defaultValue = "false") boolean checked,
            @RequestParam(required = false) String q) {
        glossaryService.updateTerm(id, term, meaning, memo, checked);
        if (q != null && !q.isBlank()) {
            return "redirect:" + UriComponentsBuilder.fromPath("/glossaries")
                    .queryParam("q", q)
                    .build()
                    .toUriString();
        }
        return "redirect:/glossaries";
    }
}
