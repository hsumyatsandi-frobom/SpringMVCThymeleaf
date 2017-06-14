package com.amh.pm.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import com.amh.pm.dao.ProjectDao;
import com.amh.pm.entity.Project;

public class ProjectServiceImpl implements ProjectService{
    
    private ProjectDao projectDao;

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    @Transactional
    public List<Project> findProjectNamesByOrganizationId(int id) {
        // TODO Auto-generated method stub
        return projectDao.findProjectNamesByOrganizationId(id);
    }

    @Override
    @Transactional
    public Project findById(int id) {
        // TODO Auto-generated method stub
        return projectDao.findById(id);
    }

    @Override
    @Transactional
    public Project findProjectByName(String name) {
        // TODO Auto-generated method stub
        return projectDao.findProjectByName(name);
    }

    @Override
    @Transactional
    public void save(Project project) {
        // TODO Auto-generated method stub
        projectDao.save(project);
        
    }

    @Override
    @Transactional
    public List<Project> findAll() {
        // TODO Auto-generated method stub
        return projectDao.findAll();
    }

}