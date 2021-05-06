package com.smartshop.models.requestBody;


import javax.validation.constraints.NotNull;


public class EntityID {
    @NotNull
    private Long id;

    public EntityID() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
