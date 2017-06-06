package pl.hycom.pip.messanger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pl.hycom.pip.messanger.controller.model.RoleDTO;
import pl.hycom.pip.messanger.controller.model.UserDTO;
import pl.hycom.pip.messanger.service.RoleService;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.service.UserService;
import pl.hycom.pip.messanger.util.RequestHelper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class UserController {

    private static final String USERS_VIEW = "users";

    private final UserService userService;

    private final RoleService roleService;

    private static final String ROLE_ADMIN = "ADMIN";

    @RolesAllowed(ROLE_ADMIN)
    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        prepareModel(model, new UserDTO());
        return USERS_VIEW;
    }

    @RolesAllowed(ROLE_ADMIN)
    @PostMapping("/admin/users")
    public String addOrUpdateUser(@Valid UserDTO user, BindingResult bindingResult,
                                  Model model, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            prepareModel(model, user);
            log.info("Validation user error!" + bindingResult.getAllErrors());

            return USERS_VIEW;
        }

        try {
            userService.addOrUpdateUser(user, RequestHelper.getURLBase(request));
            return "redirect:/admin/users";
        } catch (EmailNotUniqueException e) {
            prepareModel(model, user);
            model.addAttribute("error", new ObjectError("validation.error.user.exists", "Użytkownik z takim adresem email już istnieje."));
            return USERS_VIEW;
        } catch (MalformedURLException e) {
            prepareModel(model, user);
            model.addAttribute("error", new ObjectError("file.error.malformed.url", "Nie udało się wysłać maila do ustawienia hasła."));
            return USERS_VIEW;
        }
    }

    @RolesAllowed(ROLE_ADMIN)
    @DeleteMapping("/admin/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") final Integer id, Model model) {
        boolean result = userService.isChosenAccountCurrentUser(id);
        if(!result) {
            userService.deleteUser(id);
            log.info("User[" + id + "] deleted!");
            return "redirect:/admin/users";
        } else {
            prepareModel(model, new UserDTO());
            ObjectError error = new ObjectError("user.cannot.delete.own.account", "Użytkownik nie może usunąć własnego konta");
            model.addAttribute("error", error);
            return USERS_VIEW;
        }
    }

    private void prepareModel(Model model, UserDTO user) {
        List<UserDTO> allUsers = userService.findAllUsers();
        List<RoleDTO> allRoles = roleService.findAllRoles();
        List<Integer> rolesIDs = allRoles.stream().mapToInt(RoleDTO::getId).boxed().collect(Collectors.toList());
        model.addAttribute("rolesId", rolesIDs);
        model.addAttribute("users", allUsers);
        model.addAttribute("userForm", user);
        model.addAttribute("authorities", allRoles);
    }

    @RolesAllowed(ROLE_ADMIN)
    @GetMapping("/admin/users/{userId}/roles")
    @ResponseBody
    public Set<Integer> getUserRoles(@PathVariable("userId") final Integer id) {
        log.info("Searching for user's [" + id + "] roles");

        return userService.findUserRoles(id);
    }
}
