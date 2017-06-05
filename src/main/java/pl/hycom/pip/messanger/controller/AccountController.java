package pl.hycom.pip.messanger.controller;

import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.hycom.pip.messanger.controller.model.UserDTO;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.UserService;

import javax.validation.Valid;

/**
 * Created by Rafal Lebioda on 25.05.2017.
 */
@Log4j2
@Controller
public class AccountController {

    private static final String ACCOUNT_VIEW = "account";

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade orikaMapper;

    @GetMapping("/admin/account")
    public String showLoggedUserAccount(Model model, @AuthenticationPrincipal User user)
    {
        if(user!=null) {
            model.addAttribute("user", orikaMapper.map(user,UserDTO.class));
            return ACCOUNT_VIEW;
        }
        else {
            log.info(" There is no user logged in");
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin/account/{userId}")
    public String showAccount(Model model, @PathVariable("userId") final Integer userId) {
        UserDTO user =userService.findUserById(userId);
        if(user!=null) {
            model.addAttribute("user", user);
            return ACCOUNT_VIEW;
        }
        else {
            log.error("There is no user with id = "+userId);
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/admin/account/update")
    public String updateAccount(@Valid UserDTO user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            log.info("Validation user account error !!!");
            return ACCOUNT_VIEW;
        }
        else {
            userService.updateUser(user);
            return "redirect:/admin/account/"+user.getId();
        }
    }

}
