package controllers;


import db.DBHelper;
import models.Department;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {

    public ManagersController(){
        this.setUpEndPoints();
    }

    private void setUpEndPoints(){
        get("/managers", (req,res) ->{
            HashMap<String,Object> model = new HashMap<>();
            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);
            model.put("template", "templates/managers/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/managers/new", (req,res) ->{
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/managers/create.vtl");
            model.put("departments", departments);
            return  new ModelAndView(model,"templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/managers", (req,res) ->{
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            double budget = Double.parseDouble(req.queryParams("budget"));
            Manager manager = new Manager(firstName,lastName,salary,department,budget);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

        get("/managers/:id/update", (req,res) ->{
            HashMap<String,Object> model = new HashMap<>();
            Manager manager = DBHelper.find(Integer.parseInt(req.params(":id")), Manager.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/managers/update.vtl");
            model.put("departments",departments);
            model.put("manager", manager);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/managers/:id", (req,res) ->{
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            double budget = Double.parseDouble(req.queryParams("budget"));
            Manager manager = DBHelper.find(Integer.parseInt(req.params(":id")),Manager.class);
            manager.setBudget(budget);
            manager.setSalary(salary);
            manager.setDepartment(department);
            manager.setFirstName(firstName);
            manager.setLastName(lastName);
            DBHelper.save(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

        post("/managers/:id/delete", (req,res) ->{
            Manager manager = DBHelper.find(Integer.parseInt(req.params(":id")),Manager.class);
            DBHelper.delete(manager);
            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());


    }
}
