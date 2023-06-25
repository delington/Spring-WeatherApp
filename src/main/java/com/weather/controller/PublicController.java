package com.weather.controller;

import java.util.Map;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.weather.form.CityForm;
import com.weather.service.PublicService;

@Slf4j
@AllArgsConstructor
@Controller
public class PublicController {

    private PublicService publicService;
    
    @RequestMapping("/")
    public String index(Model model) {
        log.info("Main page called. Returning index page...");
        model.addAttribute("cityForm", new CityForm());
        
        return "index";
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public String process(Model model,
                          @RequestParam(value = "name") String cityName,
                          @RequestParam(value = "provider") String apiProvider,
                          @Valid @ModelAttribute CityForm form,
                          BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        
        log.info("PublicController /process endpoint called. PublicService.process method calling...");
        Map<String, String> attrMap = publicService.process(cityName, apiProvider);
        log.info("PublicService.process method finished.");
        
        model.addAttribute("imgUrl", attrMap.get("imgUrl"));
        model.addAttribute("temp", attrMap.get("temp"));
        model.addAttribute("desc", attrMap.get("desc"));
        model.addAttribute("cityName", cityName);
        
        return "process";
    }
}
