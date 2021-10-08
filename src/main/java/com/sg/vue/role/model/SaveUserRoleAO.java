package com.sg.vue.role.model;

import lombok.Data;

import java.util.List;

@Data
public class SaveUserRoleAO {
    private Integer currentUserId;
    private List<String> value;
}
