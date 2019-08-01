package com.sixr.backend.services;

import com.sixr.backend.exceptions.ResourceNotFoundException;
import com.sixr.backend.models.Project;
import com.sixr.backend.models.User;
import com.sixr.backend.repository.ProjectRepository;
import com.sixr.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userrepos;

    @Override
    public List<Project> findAll() {
        List<Project> list = new ArrayList<>();
        projectRepo.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public List<Project> findAll(Pageable pagable) {
        List<Project> list = new ArrayList<>();
        projectRepo.findAll(pagable).iterator().forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public Project save(Project p) {
        Project ret = new Project();

        ret.setAmount(p.getAmount());
        ret.setDescription(p.getDescription());
        ret.setEmail(p.getEmail());
        ret.setPhone(p.getPhone());
        ret.setName(p.getName());
        ret.setStatus(p.getStatus());
        ret.setOwner(p.getOwner());

        return projectRepo.save(ret);
    }

    @Override
    public Project findProjectById(long id) {
        return projectRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(Long.toString(id)));
    }

    @Transactional
    @Override
    public void delete(long id) {
        if(projectRepo.findById(id).isPresent()){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = userrepos.findByUsername(authentication.getName());
            Project project = projectRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(Long.toString(id)));

            if(currentUser.getUserid()==project.getOwner().getUserid() || SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                                                                                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                projectRepo.deleteById(id);
            }
            else{
                throw new ResourceNotFoundException("Not Allowed");
            }
        }else{
            throw new ResourceNotFoundException(Long.toString(id));
        }
    }


    @Transactional
    @Override
    public Project update(Project p, long id) {
        Project temp = projectRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(Long.toString(id)));
        if(p.getAmount()>-1)
            temp.setAmount(p.getAmount());
        if(p.getDescription()!=null)
            temp.setDescription(p.getDescription());
        if(p.getEmail()!=null)
            temp.setEmail(p.getEmail());
        if(p.getName()!=null)
            temp.setName(p.getName());
        if(p.getStatus()!=null)
            temp.setStatus(p.getStatus());
        if(p.getOwner()!=null)
            temp.setOwner(p.getOwner());

        return projectRepo.save(temp);
    }

    @Override
    public List<Project> findMine(User user) {
        List<Project> list = new ArrayList<>();
        projectRepo.findAllByOwner(user).iterator().forEachRemaining(list::add);
        return list;
    }
}
