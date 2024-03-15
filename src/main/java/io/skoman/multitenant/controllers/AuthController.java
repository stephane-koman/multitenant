package io.skoman.multitenant.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/v1/auth")
public class AuthController {

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws Exception {
        request.logout();
    }
}
