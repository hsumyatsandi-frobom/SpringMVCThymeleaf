package com.amh.pm.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.amh.pm.entity.Project;
import com.amh.pm.entity.User;

public class ProjectDaoImpl implements ProjectDao{
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Project> findProjectNamesByOrganizationId(int id) {
        // TODO Auto-generated method stub
        return entityManager.createQuery("SELECT proj FROM Project proj", Project.class).getResultList();

    }

    @Override
    public Project findById(int id) {
        // TODO Auto-generated method stub
        return entityManager.find(Project.class, id);
    }

    @Override
    public Project findProjectByName(String name) {
        // TODO Auto-generated method stub
        Project resultProject = null;

        try {

            Query q = entityManager.createQuery("select proj from Project proj WHERE proj.name=?");

            q.setParameter(1, name);

            resultProject = (Project) q.getSingleResult();

        } catch (NoResultException e) {

            System.out.println("Error is :" + e);

        }

        return resultProject;

    }
    
    @Override
    public void save(Project project) {
        // TODO Auto-generated method stub
        entityManager.merge(project);
        
    }

    @Override
    public List<Project> findAll() {
        // TODO Auto-generated method stub
        return entityManager.createQuery("SELECT project FROM Project project", Project.class).getResultList();
    }

}
