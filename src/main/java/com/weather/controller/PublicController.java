package com.weather.controller;

import com.weather.service.PublicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class PublicController {

    public static final String PAGE_PROCESS = "process";

    PublicService publicService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "John");
        return "index";
    }

    @PostMapping(value = "/process")
    public String process(Model model,
                          @RequestParam(value = "cityName") String cityName) throws Exception {

        log.info("PublicController /process endpoint called. PublicService.process method calling...");
        Map<String, String> attrMap = publicService.process(cityName);
        log.info("PublicService.process method finished.");

        model.addAttribute("imgUrl", attrMap.get("imgUrl"));
        model.addAttribute("temp", attrMap.get("temp"));
        model.addAttribute("desc", attrMap.get("desc"));
        model.addAttribute("cityName", cityName);

        return PAGE_PROCESS;
    }
}
