package com.sixr.backend.controllers;

import com.sixr.backend.models.ErrorDetail;
import com.sixr.backend.models.Project;
import com.sixr.backend.models.User;
import com.sixr.backend.services.ProjectService;
import com.sixr.backend.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "Returns all Projects (unpaged)", responseContainer = "List")
    @GetMapping(value = "/listAll",
                produces = {"application/json"})
    public ResponseEntity<?> getProjects(){
        List<Project> list=projectService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "Lists all projects", responseContainer = "List",notes = "set ? = params and separate with & to use")
    @ApiImplicitParams(value={
            @ApiImplicitParam(
                    name = "page",
                    dataType = "integer",
                    paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(
                    name = "size",
                    dataType = "integer",
                    paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(
                    name = "sort",
                    allowMultiple = true,
                    dataType = "string",
                    paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")
    })
    @GetMapping(value = "/list",
                produces = {"application/json"})
    public ResponseEntity<?> getProjects(@PageableDefault(page=0,size=10) Pageable pageable){
        List<Project> list = projectService.findAll(pageable);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @ApiOperation(value = "Gets a project by it's id", response = Project.class)
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "Project not found",response = ErrorDetail.class),
            @ApiResponse(code=401,message = "Not Authorized", response = ErrorDetail.class)
    })
    @GetMapping(value = "/{id}",
                produces = {"application/json"})
    public ResponseEntity<?> getById(@PathVariable long id){
        Project p = projectService.findProjectById(id);
        return new ResponseEntity<>(p,HttpStatus.OK);
    }

    @ApiOperation(value="Update a project given id",response = Project.class)
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "Project not found",response = ErrorDetail.class),
            @ApiResponse(code=401,message = "Not Authorized", response = ErrorDetail.class),
            @ApiResponse(code=500,message = "Internal Server Error",response = ErrorDetail.class)
    })
    @PutMapping(value="/{projectid}",
                consumes = {"application/json"},
                produces = {"application/json"})
    public ResponseEntity<?> updateProject(@RequestBody Project p, @PathVariable long projectid){
        Project ret=projectService.update(p,projectid);
        return new ResponseEntity<>(ret,HttpStatus.OK);
    }

    @ApiOperation(value = "Update the status of a project given it's project id and new status")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "Project not found",response = ErrorDetail.class),
            @ApiResponse(code=500,message = "Internal Server Error",response = ErrorDetail.class)
    })
    @PutMapping(value="/{projectid}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable long projectid, @PathVariable String status){
        Project p=projectService.findProjectById(projectid);
        p.setStatus(status);
        p=projectService.update(p,p.getProjectid());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "Add Project to current user",notes="Requires fields (name,description,amount,email, phone, and status), location header will have new project location",response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code=500,message = "Server Error",response = ErrorDetail.class),
            @ApiResponse(code=201,message="Created",response = void.class)
    })
    @PostMapping(value = "/add",
                consumes = {"application/json"},
                produces = {"application/json"})
    public ResponseEntity<?> addProjectToUser(@RequestBody Project p){
        User user= userService.self();
        p.setOwner(user);
        p=projectService.save(p);

        HttpHeaders headers=new HttpHeaders();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{projectid}").buildAndExpand(p.getProjectid()).toUri();
        headers.setLocation(uri);

        return  new ResponseEntity<>(null,headers,HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Projects owned by self", responseContainer = "List")
    @GetMapping(value = "/owned",
                produces = {"application/json"})
    public ResponseEntity<?> getOwnProjects(){
        User user=userService.self();
        List<Project> res = projectService.findMine(user);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Project by it's id", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,message = "Project Not Found", response = ErrorDetail.class),
            @ApiResponse(code=500, message="Internal Server Error", response = void.class)
    })
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id){
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
