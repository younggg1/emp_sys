package com.empsys.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName employment_records
 */
@TableName(value ="employment_records")
@Data
public class EmploymentRecords {
    /**
     * 
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long record_id;

    /**
     * 
     */
    @TableField(value = "student_id")
    private Long student_id;

    /**
     * 
     */
    @TableField(value = "company_nature")
    private String company_nature;

    /**
     * 
     */
    @TableField(value = "company")
    private String company;

    /**
     * 
     */
    @TableField(value = "position")
    private String position;

    /**
     * 
     */
    @TableField(value = "salary")
    private BigDecimal salary;

    /**
     * 
     */
    @TableField(value = "entry_date")
    private Date entry_date;

    /**
     * 
     */
    @TableField(value = "region")
    private String region;

    /**
     * 
     */
    @TableField(value = "status")
    private Object status;

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
        EmploymentRecords other = (EmploymentRecords) that;
        return (this.getRecord_id() == null ? other.getRecord_id() == null : this.getRecord_id().equals(other.getRecord_id()))
            && (this.getStudent_id() == null ? other.getStudent_id() == null : this.getStudent_id().equals(other.getStudent_id()))
            && (this.getCompany_nature() == null ? other.getCompany_nature() == null : this.getCompany_nature().equals(other.getCompany_nature()))
            && (this.getCompany() == null ? other.getCompany() == null : this.getCompany().equals(other.getCompany()))
            && (this.getPosition() == null ? other.getPosition() == null : this.getPosition().equals(other.getPosition()))
            && (this.getSalary() == null ? other.getSalary() == null : this.getSalary().equals(other.getSalary()))
            && (this.getEntry_date() == null ? other.getEntry_date() == null : this.getEntry_date().equals(other.getEntry_date()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRecord_id() == null) ? 0 : getRecord_id().hashCode());
        result = prime * result + ((getStudent_id() == null) ? 0 : getStudent_id().hashCode());
        result = prime * result + ((getCompany_nature() == null) ? 0 : getCompany_nature().hashCode());
        result = prime * result + ((getCompany() == null) ? 0 : getCompany().hashCode());
        result = prime * result + ((getPosition() == null) ? 0 : getPosition().hashCode());
        result = prime * result + ((getSalary() == null) ? 0 : getSalary().hashCode());
        result = prime * result + ((getEntry_date() == null) ? 0 : getEntry_date().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        result = prime * result + ((getUpdated_at() == null) ? 0 : getUpdated_at().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", record_id=").append(record_id);
        sb.append(", student_id=").append(student_id);
        sb.append(", company_nature=").append(company_nature);
        sb.append(", company=").append(company);
        sb.append(", position=").append(position);
        sb.append(", salary=").append(salary);
        sb.append(", entry_date=").append(entry_date);
        sb.append(", region=").append(region);
        sb.append(", status=").append(status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}