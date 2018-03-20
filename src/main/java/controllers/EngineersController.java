package controllers;

import db.DBHelper;
import models.Department;
import models.Engineer;
import models.Manager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class EngineersController {
    public EngineersController() {
        this.setUpEndPoints();
    }

    private void setUpEndPoints(){
        get("/engineers", (req,res) ->{
            HashMap<String, Object> model = new HashMap<>();
            List<Engineer> engineers = DBHelper.getAll(Engineer.class);
            model.put("engineers", engineers);
            model.put("template", "templates/engineers/index.vtl");
            return new ModelAndView(model,"templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/engineers/new", (req,res) ->{
            HashMap<String,Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/engineers/create.vtl");
            model.put("departments", departments);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers", ( req, res) ->{
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId,Department.class);
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            Engineer engineer = new Engineer(firstName,lastName,salary,department);
            DBHelper.save(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());

        get("/engineers/:id/update", (req,res) ->{
            HashMap<String,Object> model = new HashMap<>();
            Engineer engineer = DBHelper.find(Integer.parseInt(req.params(":id")), Engineer.class);
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("template", "templates/engineers/update.vtl");
            model.put("departments",departments);
            model.put("engineer", engineer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/engineers/:id", (req,res) ->{
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            Engineer engineer = DBHelper.find(Integer.parseInt(req.params(":id")),Engineer.class);
            engineer.setSalary(salary);
            engineer.setDepartment(department);
            engineer.setFirstName(firstName);
            engineer.setLastName(lastName);
            DBHelper.save(engineer);
            res.redirect("/engineers");
            return null;
        }, new VelocityTemplateEngine());
    }
}
