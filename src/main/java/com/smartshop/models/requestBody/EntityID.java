package com.smartshop.models.requestBody;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class EntityID {
    @NotNull
    private Long id;

}
