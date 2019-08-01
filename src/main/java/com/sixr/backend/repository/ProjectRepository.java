package com.sixr.backend.repository;

import com.sixr.backend.models.Project;
import com.sixr.backend.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project,Long> {
    List<Project> findAll();

    List<Project> findAllByOwner(User user);
}
