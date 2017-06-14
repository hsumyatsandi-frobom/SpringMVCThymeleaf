package com.amh.pm.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.amh.pm.entity.Organization;
import com.amh.pm.entity.Project;
import com.amh.pm.service.OrganizationService;
import com.amh.pm.service.ProjectService;

@Controller
public class ProjectController {

    private ProjectService projectService;

    private OrganizationService organizationService;

    HttpSession session;

    @Autowired(required = true)
    @Qualifier(value = "projectService")
    public void setProjectService(ProjectService projectService) {

        this.projectService = projectService;

    }

    @Autowired(required = true)
    @Qualifier(value = "organizationService")
    public void setOrganizationService(OrganizationService organizationService) {

        this.organizationService = organizationService;

    }

    @RequestMapping(value = "/organizations/{organizationId}/projects", method = RequestMethod.GET)
    public String showProjects(@PathVariable("organizationId") int id, Model model, HttpServletRequest request) {

        session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        } else {
            int userId = (Integer) session.getAttribute("userId");
            Organization organizationOwner = organizationService.findById(id);

            if (userId == organizationOwner.getOwner().getId()) {

                session = request.getSession(true);
                session.setAttribute("organizationId", id);
                List<Project> projects = projectService.findAll();
                List<Project> projectNames = new ArrayList<Project>();

                for (Project project : projects) {
                    if (project.getManager().getId() == id) {
                        projectNames.add(project);
                    }
                }

                model.addAttribute("organizationId", id);
                model.addAttribute("projectNames", projectNames);
                return "projects";

            } else {
                List<Project> projects = projectService.findAll();
                List<Project> projectNames = new ArrayList<Project>();

                for (Project project : projects) {
                    if (project.getManager().getId() == id) {
                        projectNames.add(project);
                    }
                }
                model.addAttribute("projectNames", projectNames);
                return "showProjects";
            }

        }
    }

    @RequestMapping(value = "/organizations/{organizationId}/projects/new", method = RequestMethod.GET)
    public String showProjectNewForm(@PathVariable("organizationId") int id, Model model, HttpServletRequest request) {
        if (session == null) {

            return "redirect:/login";
        } else {

            model.addAttribute("organizationId", id);
            model.addAttribute("project", new Project());
            return "addProject";
        }

    }

    @RequestMapping(value = "/organizations/{id}/projects/new", method = RequestMethod.POST)
    public String addNewProject(@PathVariable("id") int organizationid, @Validated @ModelAttribute Project project, BindingResult result, Model model, HttpServletRequest request) {

        if (result.hasErrors()) {

            model.addAttribute("organizationId", organizationid);
            return "addProject";

        } 
        else {
            if (project.getScheduleStartDate() == null || project.getScheduleEndDate() == null || project.getActualStartDate() == null || project.getActualEndDate() == null) {
                String dateEmptyError = "Please Fill Every Date";
                model.addAttribute("dateEmptyError", dateEmptyError);
                model.addAttribute("organizationId", organizationid);
                return "addProject";

            } 
            else {
                Organization organization = organizationService.findById(organizationid);

                project.setManager(organization);
                Date scheduleStartDate = project.getScheduleStartDate();
                Date scheduleEndDate = project.getScheduleEndDate();
                Date actualStartDate = project.getActualStartDate();
                Date actualEndDate = project.getActualEndDate();

                if ((scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && (actualStartDate.compareTo(actualEndDate) > 0) 
                        && (scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError1 = "Schedule End Date Must Be Greater Than Schedule Start Date. "
                            + "Actual End Date Must Be Greater Than Actual Start Date. "
                            + "Actual Start Date Must Be Greater Than Schedule Start Date.";
                    
                    model.addAttribute("dateError1", dateError1);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if ((scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && (actualStartDate.compareTo(actualEndDate) > 0) 
                        && !(scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError2 = "Schedule End Date Must Be Greater Than Schedule Start Date. "
                            + "Actual End Date Must Be Greater Than Actual Start Date.";
                    
                    model.addAttribute("dateError2", dateError2);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if ((scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && !(actualStartDate.compareTo(actualEndDate) > 0) 
                        && (scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError3 = "Schedule End Date Must Be Greater Than Schedule Start Date. "
                            + "Actual Start Date Must Be Greater Than Schedule Start Date.";
                    
                    model.addAttribute("dateError3", dateError3);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if (!(scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && (actualStartDate.compareTo(actualEndDate) > 0) 
                        && (scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError4 = "Actual End Date Must Be Greater Than Actual Start Date. "
                            + "Actual Start Date Must Be Greater Than Schedule Start Date.";
                    
                    model.addAttribute("dateError4", dateError4);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if ((scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && !(actualStartDate.compareTo(actualEndDate) > 0) 
                        && !(scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError5= "Schedule End Date Must Be Greater Than Schedule Start Date.";
                    
                    model.addAttribute("dateError5", dateError5);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if (!(scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && (actualStartDate.compareTo(actualEndDate) > 0) 
                        && !(scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError6 = "Actual End Date Must Be Greater Than Actual Start Date.";
                    
                    model.addAttribute("dateError6", dateError6);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else if (!(scheduleStartDate.compareTo(scheduleEndDate) > 0) 
                        && !(actualStartDate.compareTo(actualEndDate) > 0) 
                        && (scheduleStartDate.compareTo(actualStartDate) > 0)) {

                    String dateError7 = "Actual Start Date Must Be Greater Than Schedule Start Date.";
                    
                    model.addAttribute("dateError7", dateError7);
                    model.addAttribute("organizationId", organizationid);
                    return "addProject";
                } 
                else {

                    projectService.save(project);
                    showProjects(organizationid, model, request);
                    return "projects";

                }

            }

        }

    }
}
