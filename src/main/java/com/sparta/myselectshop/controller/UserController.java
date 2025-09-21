package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.SignupRequestDto;
import com.sparta.myselectshop.dto.UserInfoDto;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/api"})
public class UserController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final FolderService folderService;

    @GetMapping({"/user/login-page"})
    public String loginPage() {
        return "login";
    }

    @GetMapping({"/user/signup"})
    public String signupPage() {
        return "signup";
    }

    @PostMapping({"/user/signup"})
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() <= 0) {
            this.userService.signup(requestDto);
            return "redirect:/api/user/login-page";
        } else {
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                Logger var10000 = log;
                String var10001 = fieldError.getField();
                var10000.error(var10001 + " 필드 : " + fieldError.getDefaultMessage());
            }

            return "redirect:/api/user/signup";
        }
    }

    @GetMapping({"/user-info"})
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = role == UserRoleEnum.ADMIN;
        return new UserInfoDto(username, isAdmin);
    }

    @GetMapping({"/user-folder"})
    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("folders", this.folderService.getFolders(userDetails.getUser()));
        return "index :: #fragment";
    }

    @Generated
    public UserController(final UserService userService, final FolderService folderService) {
        this.userService = userService;
        this.folderService = folderService;
    }
}