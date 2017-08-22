/**
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.hycom.pip.messanger.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;
import pl.hycom.pip.messanger.controller.model.UserDTO;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.exception.SecurityException;
import pl.hycom.pip.messanger.repository.model.Role;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.UserService;

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

    @GetMapping("/user/account")
    public String showLoggedUserAccount(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("user", orikaMapper.map(user, UserDTO.class));
            return ACCOUNT_VIEW;
        }

        log.info(" There is no user logged in");
        return "redirect:/admin";
    }

    @RolesAllowed(Role.Name.ADMIN)
    @GetMapping("/user/account/{userId}")
    public String showAccount(Model model, @PathVariable("userId") final Integer userId) {
        UserDTO user = userService.findUserById(userId);
        if (user != null) {
            model.addAttribute("user", user);
            return ACCOUNT_VIEW;
        }

        log.error("There is no user with id = " + userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/user/account/update")
    public String updateAccount(@Valid UserDTO user, BindingResult bindingResult, Model model, @AuthenticationPrincipal User currentUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            log.info("Validation user account error !!!");
            return ACCOUNT_VIEW;
        }

        try {
            userService.addOrUpdateUser(user);
        } catch (EmailNotUniqueException | SecurityException e) {
            log.warn("Error during user update", e);

            model.addAttribute("user", user);
            model.addAttribute("error", new ObjectError("validation.error.user.exists", "Użytkownik z takim adresem email już istnieje."));
            return ACCOUNT_VIEW;
        }

        if (currentUser != null) {
            return user.getId().equals(currentUser.getId()) ? "redirect:/user/account" : "redirect:/user/account/" + user.getId();
        }

        log.info(" There is no user logged in");
        return "redirect:/admin";
    }

}
