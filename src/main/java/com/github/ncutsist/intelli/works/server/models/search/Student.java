package com.github.ncutsist.intelli.works.server.models.search;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    private String grade, cls, sno, name, sex, area, nation, quality, nation_help, gpa, political, phone, parent_phone;
    private String dorm, dorm_num, uid, home;
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getCls() {
        return cls;
    }
    
    public void setCls(String cls) {
        this.cls = cls;
    }
    
    public String getSno() {
        return sno;
    }
    
    public void setSno(String sno) {
        this.sno = sno;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getArea() {
        return area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    public String getNation() {
        return nation;
    }
    
    public void setNation(String nation) {
        this.nation = nation;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public void setQuality(String quality) {
        this.quality = quality;
    }
    
    public String getNationHelp() {
        return nation_help;
    }
    
    public void setNationHelp(String nation_help) {
        this.nation_help = nation_help;
    }
    
    public String getGpa() {
        return gpa;
    }
    
    public void setGpa(String gpa) {
        this.gpa = gpa;
    }
    
    public String getPolitical() {
        return political;
    }
    
    public void setPolitical(String political) {
        this.political = political;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getParentPhone() {
        return parent_phone;
    }
    
    public void setParentPhone(String parent_phone) {
        this.parent_phone = parent_phone;
    }
    
    public String getDorm() {
        return dorm;
    }
    
    public void setDorm(String dorm) {
        this.dorm = dorm;
    }
    
    public String getDormNum() {
        return dorm_num;
    }
    
    public void setDormNum(String dorm_num) {
        this.dorm_num = dorm_num;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getHome() {
        return home;
    }
    
    public void setHome(String home) {
        this.home = home;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return sno.equals(student.sno) &&
                uid.equals(student.uid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sno, uid);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "grade='" + grade + '\'' +
                ", cls='" + cls + '\'' +
                ", sno='" + sno + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", area='" + area + '\'' +
                ", nation='" + nation + '\'' +
                ", quality='" + quality + '\'' +
                ", nation_help='" + nation_help + '\'' +
                ", gpa='" + gpa + '\'' +
                ", political='" + political + '\'' +
                ", phone='" + phone + '\'' +
                ", parent_phone='" + parent_phone + '\'' +
                ", dorm='" + dorm + '\'' +
                ", dorm_num='" + dorm_num + '\'' +
                ", uid='" + uid + '\'' +
                ", home='" + home + '\'' +
                '}';
    }
}
