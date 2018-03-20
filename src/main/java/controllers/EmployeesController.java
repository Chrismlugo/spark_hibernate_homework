package controllers;
import db.DBHelper;
import db.Seeds;
import models.Employee;
import models.Engineer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;


public class EmployeesController {
    public static void main(String[] args) {
        staticFileLocation("public");
        EngineersController engineersController = new EngineersController();
        ManagersController managersController = new ManagersController();
        Seeds.seedData();

        get("/employees", (req,res) ->{
            HashMap<String,Object> model = new HashMap<>();
            List<Employee> employees = DBHelper.getAll(Employee.class);
            model.put("employees", employees);
            model.put("template", "templates/employees/index.vtl");
            return new ModelAndView(model,"templates/layout.vtl");
        }, new VelocityTemplateEngine());


    }
}
