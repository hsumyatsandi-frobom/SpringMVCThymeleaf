package com.amh.pm.dao;

import java.util.List;

import com.amh.pm.entity.Project;

public interface ProjectDao {
    
    public List<Project> findProjectNamesByOrganizationId(int id);
    
    public Project findById(int id);
    
    public Project findProjectByName(String name);

    public void save(Project project);

    public List<Project> findAll();

}
