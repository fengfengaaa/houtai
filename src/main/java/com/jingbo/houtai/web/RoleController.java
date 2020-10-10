package com.jingbo.houtai.web;

import com.jingbo.houtai.entity.SysMenu;
import com.jingbo.houtai.entity.SysRole;
import com.jingbo.houtai.entity.SysUser;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.service.RoleService;
import com.jingbo.houtai.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = {"http://localhost:8080"})
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleServiceImpl;

    @GetMapping("/all")
    public JsonResult getRoleList() {
        Map result = new HashMap<>();
        Subject subject = ShiroUtils.getSubject();
        Object principal = subject.getPrincipal();
        try {
            Integer roleId;
            if (principal instanceof SysUser) {
             roleId = ((SysUser) principal).getAccountType();
            }else{
                roleId = 8 ;
            }
                SysRole roles = roleServiceImpl.getRoleByRoleId(roleId);
                if (roles != null) {
                    List<SysMenu> menus = roles.getMenus();
                    List menuperms = new ArrayList();
                    List routperms = new ArrayList();
                    for (SysMenu sysMenu : menus) {
                        if(sysMenu.getType() == 1){
                            routperms.add(sysMenu.getPerms());
                        }else if(sysMenu.getType() == 2){
                            menuperms.add(sysMenu.getPerms());
                        }
                    }
                    result.put("menuperms", menuperms);
                    result.put("routperms", routperms);
                }

                if(principal instanceof User){
                    result.put("isadmin", false);
                }else{
                    Boolean admin = ((SysUser) principal).isAdmin();
                    result.put("isadmin", admin);
                }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e.getMessage());
        }
        return JsonResult.success(result);
    }
}
