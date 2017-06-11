package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import pl.hycom.pip.messanger.controller.model.KeywordDTO;
import pl.hycom.pip.messanger.nlp.NlpService;
import pl.hycom.pip.messanger.nlp.NlpServiceImplementation;
import pl.hycom.pip.messanger.nlp.Result;
import pl.hycom.pip.messanger.service.KeywordService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import java.io.IOException;
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

    @GetMapping("/admin/nlp")
    public String showView(Model model) {
         return NLP_VIEW;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/nlp", method = RequestMethod.POST)
    public String returnResult(Model model)  {
        log.info("Variable received from Extract class " + nlpService.analyze());
        List<Result> temp = new ArrayList<>();
        temp.addAll(nlpService.analyze());
//        if (!outputList.isEmpty()) {
//            temp .addAll(nlpService.matchKeywords(outputList));
//        }
        model.addAttribute("lista" , temp);
        return NLP_VIEW;

    }


}







