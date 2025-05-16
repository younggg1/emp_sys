package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName students
 */
@TableName(value ="students")
@Data
public class Students {
    /**
     * 
     */
    @TableId(value = "student_id", type = IdType.AUTO)
    private Long student_id;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "grade")
    private String grade;

    /**
     * 
     */
    @TableField(value = "class_name")
    private String class_name;

    /**
     * 
     */
    @TableField(value = "college")
    private String college;

    /**
     * 
     */
    @TableField(value = "major")
    private String major;

    /**
     * 
     */
    @TableField(value = "counselor_id")
    private Long counselor_id;

    /**
     * 
     */
    @TableField(value = "employment_status")
    private String employment_status;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date created_at;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updated_at;
    
    /**
     * 辅导员姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String counselorName;
    
    /**
     * 学号（来自users表的username字段，非数据库字段）
     */
    @TableField(exist = false)
    private String studentId;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Students other = (Students) that;
        return (this.getStudent_id() == null ? other.getStudent_id() == null : this.getStudent_id().equals(other.getStudent_id()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getGrade() == null ? other.getGrade() == null : this.getGrade().equals(other.getGrade()))
            && (this.getClass_name() == null ? other.getClass_name() == null : this.getClass_name().equals(other.getClass_name()))
            && (this.getCollege() == null ? other.getCollege() == null : this.getCollege().equals(other.getCollege()))
            && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()))
            && (this.getCounselor_id() == null ? other.getCounselor_id() == null : this.getCounselor_id().equals(other.getCounselor_id()))
            && (this.getEmployment_status() == null ? other.getEmployment_status() == null : this.getEmployment_status().equals(other.getEmployment_status()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()))
            && (this.getCounselorName() == null ? other.getCounselorName() == null : this.getCounselorName().equals(other.getCounselorName()))
            && (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudent_id() == null) ? 0 : getStudent_id().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getGrade() == null) ? 0 : getGrade().hashCode());
        result = prime * result + ((getClass_name() == null) ? 0 : getClass_name().hashCode());
        result = prime * result + ((getCollege() == null) ? 0 : getCollege().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        result = prime * result + ((getCounselor_id() == null) ? 0 : getCounselor_id().hashCode());
        result = prime * result + ((getEmployment_status() == null) ? 0 : getEmployment_status().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        result = prime * result + ((getUpdated_at() == null) ? 0 : getUpdated_at().hashCode());
        result = prime * result + ((getCounselorName() == null) ? 0 : getCounselorName().hashCode());
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", student_id=").append(student_id);
        sb.append(", name=").append(name);
        sb.append(", grade=").append(grade);
        sb.append(", class_name=").append(class_name);
        sb.append(", college=").append(college);
        sb.append(", major=").append(major);
        sb.append(", counselor_id=").append(counselor_id);
        sb.append(", employment_status=").append(employment_status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append(", counselorName=").append(counselorName);
        sb.append(", studentId=").append(studentId);
        sb.append("]");
        return sb.toString();
    }
}