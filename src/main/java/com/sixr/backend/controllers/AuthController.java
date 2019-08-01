package com.sixr.backend.controllers;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("Authentication")
@RestController
@RequestMapping(value = "/")
public interface AuthController {
    /**
     * Implimented by Spring Security
     */
    @ApiOperation(value = "Login",notes = "Login with given credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "",response = Authorization.class)
    })
    @PostMapping("/login")
    default void login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ){
        throw new IllegalStateException("Should never be called, implemented by Spring");
    }
}
