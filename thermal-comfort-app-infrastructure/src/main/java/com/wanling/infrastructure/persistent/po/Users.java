package com.wanling.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;

/**
 * 
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users implements Serializable {
    /**
     * 
     */
    @TableId
    private String userId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Users users = (Users) o;
        return Objects.equals(userId, users.userId) && Objects.equals(name, users.name) && Objects.equals(email, users.email) && Objects.equals(password, users.password) && Objects.equals(createdAt, users.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, password, createdAt);
    }
}