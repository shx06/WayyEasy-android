package com.wayyeasy.wayyeasy.Models.Hospitals;

public class doctors_response_model {

    String name, qualification, age;

    public doctors_response_model() {
    }

    public doctors_response_model(String name, String qualification, String age) {
        this.name = name;
        this.qualification = qualification;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }
}
