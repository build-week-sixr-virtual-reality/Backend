package com.sixr.backend.controllers;

import com.sixr.backend.models.ErrorDetail;
import com.sixr.backend.models.User;
import com.sixr.backend.models.UserRoles;
import com.sixr.backend.services.RoleService;
import com.sixr.backend.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "lists all users",notes = "requires admin login", responseContainer = "List")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/all",
                produces = {"application/json"})
    public ResponseEntity<?> listAllUsers(HttpServletRequest request)
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        List<User> myUsers = userService.findAll();
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }

    @ApiOperation(value = "returns user by id", responseContainer = "List", notes = "Requires Admin Login")
    @ApiResponses(value = {
            @ApiResponse(code=404,message="User Not Found", response = ErrorDetail.class)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/{userId}",
                produces = {"application/json"})
    public ResponseEntity<?> getUser(HttpServletRequest request,
                                     @PathVariable
                                             Long userId)
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }


    @ApiOperation(value = "get's username",response = String.class)
    @GetMapping(value = "/getusername",
                produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> getCurrentUserName(HttpServletRequest request, Authentication authentication)
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
    }

    @ApiOperation(value = "Add New User as Admin", response = void.class,notes = "Requires admin login, use /createnewuser for signup")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewUser(HttpServletRequest request, @Valid
    @RequestBody
            User newuser) throws URISyntaxException
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        newuser = userService.save(newuser);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userid}").buildAndExpand(newuser.getUserid()).toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value="Grant someone Authority",response = User.class,notes="Change someone to Authority {auth}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message="User not found", response = ErrorDetail.class),
            @ApiResponse(code=500, message= "Internal Server Error", response = ErrorDetail.class)
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/grant/{auth}/{userid}")
    public ResponseEntity<?> grantAuth(@PathVariable String auth, @PathVariable long userid){
        User user = userService.findUserById(userid);
        user.getUserRoles().add(new UserRoles(user,roleService.findByName(auth)));
        user=userService.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @ApiOperation(value = "Updates a user by userid",response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,message="User Not Found", response = ErrorDetail.class)
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(HttpServletRequest request,
                                        @RequestBody
                                                User updateUser,
                                        @PathVariable
                                                long id)
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        userService.update(updateUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete user by id", notes = "Requires Admin access", response = void.class)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(HttpServletRequest request,
                                            @PathVariable
                                                    long id)
    {
        logger.trace(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed");

        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Get your records", response = User.class)
    @GetMapping(value = "/self",
                produces = {"application/json"})
    public ResponseEntity<?> getSelf(){
        return new ResponseEntity<>(userService.self(),HttpStatus.OK);
    }

    @ApiOperation(value = "Change your user type to {type}",notes = "{type} is treated as a string. Returns updated user", response = User.class)
    @PutMapping(value = "/type/{type}",
                produces = {"application/json"})
    public ResponseEntity<?> changeUserType(@PathVariable String type){
        User self = userService.self();
        self.setType(type);
        self=userService.update(self,self.getUserid());
        return new ResponseEntity<>(self,HttpStatus.OK);
    }

    @ApiOperation(value = "Get Mentors", responseContainer = "List")
    @GetMapping(value = "/mentors",
                        produces = {"application/json"})
    public ResponseEntity<?> getMentors(){
        List<User> res = userService.getMentors();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}