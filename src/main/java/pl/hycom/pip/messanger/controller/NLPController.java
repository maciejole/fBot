package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.nlp.NlpService;
import pl.hycom.pip.messanger.nlp.NlpServiceImplementation;
import pl.hycom.pip.messanger.nlp.Result;
import pl.hycom.pip.messanger.service.KeywordService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal Lebioda on 25.05.2017.
 */
@Log4j2
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class NLPController {

    private final NlpService nlpService;


    private static final String NLP_VIEW = "nlp";

//    @GetMapping("/admin/nlp")
//    public String showView(Model model) {
//        return NLP_VIEW;
//    }


    @RequestMapping(value = "/admin/nlp", method = RequestMethod.GET)
    public  String returnResult( List<Result> outputList) {
        log.info("Variable received from Extract class" + outputList.size());
        List<Result> temp = outputList;
        if (!outputList.isEmpty()) {
            temp = nlpService.matchKeywords(outputList);
        }
        ModelAndView mav = new ModelAndView();

        mav.addObject("lista" , temp);
        mav.setViewName(NLP_VIEW);
        return String.valueOf(mav);

    }


}







