package com.example.autoraceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.autoraceapp.service.SiteLinkService;

@Controller
public class HomeController {

    private final SiteLinkService siteLinkService;

    public HomeController(SiteLinkService siteLinkService) {
        this.siteLinkService = siteLinkService;
    }

    @GetMapping("/")
    public String showHomePage(
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) String glossaryQuery,
            Model model) {
        addLinkPageAttributes(model, venue);
        model.addAttribute("glossaryQuery", glossaryQuery == null ? "" : glossaryQuery);
        return "home/index";
    }

    @GetMapping("/links")
    public String showLinksPage(
            @RequestParam(required = false) String venue,
            Model model) {
        addLinkPageAttributes(model, venue);
        return "links/index";
    }

    @GetMapping("/help")
    public String showHelpPage() {
        return "help/index";
    }

    @GetMapping("/favicon.ico")
    public String redirectFavicon() {
        return "forward:/favicon.svg";
    }

    private void addLinkPageAttributes(Model model, String venue) {
        String selectedVenue = siteLinkService.resolveSelectedVenue(venue);
        model.addAttribute("officialRelatedLinks", siteLinkService.getOfficialRelatedLinks());
        model.addAttribute("venueNames", siteLinkService.getVenueNames());
        model.addAttribute("selectedVenue", selectedVenue);
        model.addAttribute("selectedVenueLinks", siteLinkService.getVenueDetailLinks(selectedVenue));
    }
}
