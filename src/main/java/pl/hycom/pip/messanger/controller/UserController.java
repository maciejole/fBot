package pl.hycom.pip.messanger.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.controller.model.RoleDTO;
import pl.hycom.pip.messanger.controller.model.UserDTO;
import pl.hycom.pip.messanger.exception.EmailNotUniqueException;
import pl.hycom.pip.messanger.repository.model.Role;
import pl.hycom.pip.messanger.repository.model.User;
import pl.hycom.pip.messanger.service.RoleService;
import pl.hycom.pip.messanger.service.UserService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class UserController {

    private static final String USERS_VIEW = "users";

    private final UserService userService;

    private final RoleService roleService;

    private static final String ROLE_ADMIN = Role.Name.ADMIN;

    @RolesAllowed(ROLE_ADMIN)
    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        prepareModel(model, new UserDTO());
        return USERS_VIEW;
    }

    @RolesAllowed(ROLE_ADMIN)
    @PostMapping("/admin/users")
    public String addOrUpdateUser(@Valid UserDTO user, BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            prepareModel(model, user);
            log.info("Validation user error!" + bindingResult.getAllErrors());
            return USERS_VIEW;
        }

        try {
            userService.addOrUpdateUser(user);
            return "redirect:/admin/users";
        } catch (EmailNotUniqueException e) {
            prepareModel(model, user);
            model.addAttribute("error", new ObjectError("validation.error.user.exists", "Użytkownik z takim adresem email już istnieje."));
            return USERS_VIEW;
        }
    }

    @RolesAllowed(ROLE_ADMIN)
    @DeleteMapping("/admin/users/{userId}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") final Integer id, Model model, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id)) {
            userService.deleteUser(id);
            log.info("User[" + id + "] deleted!");
            return ResponseEntity.ok(true);
        } else {
            prepareModel(model, new UserDTO());
            ObjectError error = new ObjectError("user.cannot.delete.own.account", "Użytkownik nie może usunąć własnego konta");
            model.addAttribute("error", error);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    private void prepareModel(Model model, UserDTO user) {
        List<UserDTO> allUsers = userService.findAllUsers();
        List<RoleDTO> allRoles = roleService.findAllRoles();
        model.addAttribute("users", allUsers);
        model.addAttribute("userForm", user);
        model.addAttribute("authorities", allRoles);
    }

}
