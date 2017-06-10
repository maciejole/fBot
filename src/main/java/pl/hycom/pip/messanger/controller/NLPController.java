package pl.hycom.pip.messanger.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.hycom.pip.messanger.nlp.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal Lebioda on 25.05.2017.
 */
@Log4j2
@Controller
public class NLPController {

    public static List<Result> outputList = new ArrayList<>();


    private static final String NLP_VIEW = "nlp";

    @PostMapping(value = "/admin/nlp")
    public String returnView(Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            log.info("Error during preparing view");
            return NLP_VIEW;
        } else {
            model.addAttribute("lista", outputList);
            return NLP_VIEW;
        }
    }
}







