package com.csdn.biz.api.model;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\13 0013 23:00
 */
@Data
public class User implements Serializable {

    @NotNull
    private Long id;

    @NotNull
//    @NotBlank
    private String name;

    public User() {
    }

    public User(@NotNull Long id, @NotNull @NotEmpty String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
