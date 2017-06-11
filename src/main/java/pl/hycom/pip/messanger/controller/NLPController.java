package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hycom.pip.messanger.nlp.NlpService;
import pl.hycom.pip.messanger.nlp.Result;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


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

    @RequestMapping(value = "/admin/nlp", method = RequestMethod.POST)
    public String returnResult(Model model)  {
        log.info("Variable received from service " + nlpService.analyze());
        List<Result> temp = new ArrayList<>();
        temp.addAll(nlpService.analyze());
//        if (!outputList.isEmpty()) {
//            temp .addAll(nlpService.matchKeywords(outputList));
//        }
        model.addAttribute("lista" , temp);
        return NLP_VIEW;

    }


}







