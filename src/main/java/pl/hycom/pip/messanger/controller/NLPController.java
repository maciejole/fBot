package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.hycom.pip.messanger.service.ResultService;

import javax.inject.Inject;


@Log4j2
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class NLPController {

    private static final String NLP_VIEW = "nlp";
    @Autowired
    ResultService resultService;


    @GetMapping("/admin/nlp")
    public String showView(Model model) {
        model.addAttribute("lista" , resultService.matchProducts());
        return NLP_VIEW;
    }




}







