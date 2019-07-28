package com.sixr.backend.services;

import com.sixr.backend.models.Project;
import com.sixr.backend.models.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    List<Project> findAll(Pageable pagable);
    List<Project> findMine(User user);

    Project save(Project p);

    Project findProjectById(long id);
    void delete(long id);
    Project update(Project p, long id);

}
