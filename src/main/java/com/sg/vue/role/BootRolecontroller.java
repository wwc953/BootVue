package com.sg.vue.role;

import com.sg.vue.converter.ResponseResult;
import com.sg.vue.role.model.QueryCurrentUserRoleAO;
import com.sg.vue.role.model.SaveUserRoleAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/role")
public class BootRolecontroller {

    @Resource
    BootRoleServer roleServer;

    @PostMapping("/queryAllUrlList")
    public ResponseResult queryAllUrlList() {
        return ResponseResult.success(roleServer.queryAllUrlList());
    }

    @PostMapping("/currentUserRole")
    public ResponseResult currentUserRole(@RequestBody QueryCurrentUserRoleAO ao) {
        return roleServer.currentUserRole(ao);
    }

    @PostMapping("/saveRoles")
    public ResponseResult saveRoles(@RequestBody SaveUserRoleAO ao) {
        return roleServer.saveUserRole(ao);
    }
}
