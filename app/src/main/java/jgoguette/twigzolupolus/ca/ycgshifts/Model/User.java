package jgoguette.twigzolupolus.ca.ycgshifts.Model;

import java.io.Serializable;

/**
 * Created by jerry on 2016-11-14.
 */

public class User implements Serializable {

    private String firebase_id;
    private String emp_id;

    private String name;

    private String email;

    private String password;

    private String department;
    private String authorizationLevel;

    public User() {
        /*Blank default constructor essential for Firebase*/
    }

    public User(String firebase_id, String emp_id, String name, String email, String department, String authorizationLevel) {
        this.firebase_id = firebase_id;
        this.emp_id = emp_id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.authorizationLevel = authorizationLevel;
    }

    public User(User user) {
        this.firebase_id = user.firebase_id;
        this.emp_id = user.emp_id;
        this.name = user.name;
        this.email = user.email;
        this.department = user.department;
        this.authorizationLevel = user.authorizationLevel;
    }

    //Getters and setters
    public String getFirebaseId() {
        return firebase_id;
    }

    public void setFirebaseId(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getEMPId() {
        return emp_id;
    }

    public void setEMPId(String id) {
        this.emp_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAuthorizationLevel() {
        return authorizationLevel;
    }

    public void setAuthorizationLevel(String authorizationLevel) {
        this.authorizationLevel = authorizationLevel;
    }
}


