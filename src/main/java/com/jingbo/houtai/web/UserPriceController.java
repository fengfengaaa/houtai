package com.jingbo.houtai.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingbo.houtai.entity.PageResult;
import com.jingbo.houtai.entity.User;
import com.jingbo.houtai.entity.UserPrice;
import com.jingbo.houtai.result.JsonResult;
import com.jingbo.houtai.service.UserPriceService;
import com.jingbo.houtai.util.ShiroUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/price")
@CrossOrigin(origins = {"http://localhost:8080"})
public class UserPriceController extends BaseController {
    @Autowired
    private UserPriceService userPriceServiceImpl;

    @PostMapping("/update")
    public JsonResult addUserPrice(HttpServletRequest request) {
        String parameter = request.getParameter("0");
        Gson gson = new Gson();
        List<UserPrice> userPrice =gson.fromJson(parameter, new TypeToken<List<UserPrice>>() {}.getType());
        try {
            userPriceServiceImpl.batchAddOrUpdateUserPrice(userPrice);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("添加用户失败，请联系it人员");
        }
        return JsonResult.success();
    }


    @GetMapping("/all")
    public PageResult<UserPrice> getAllUserInfo(Integer userId) {
        PageResult<UserPrice> result = new PageResult();
        try {
            List<UserPrice> userPriceByUserid = userPriceServiceImpl.getUserPriceByUserid(userId);
            if (userPriceByUserid.size() == 0) {
                userPriceByUserid = createUserPrice(userId);
            }
            result.setResults(userPriceByUserid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/app/all")
    public JsonResult getUserPrice() {
        Subject subject = ShiroUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return JsonResult.fail("未登录！！");
        }
        try {
            if (principal instanceof User) {
                Integer userId = ((User) principal).getUserId();
                List<UserPrice> userPriceByUserid = userPriceServiceImpl.getUserPriceByUserid(userId);
                if (userPriceByUserid.size() == 0) {
                    userPriceByUserid = createUserPrice(userId);
                }
                return JsonResult.success(userPriceByUserid);
            }else{
                return JsonResult.fail("账号类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("发生异常，请联系it人员");
        }
    }

    public List<UserPrice> createUserPrice(Integer userId) {
        List<UserPrice> userPriceList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userPriceList.add(new UserPrice(i, userId));
        }
        return userPriceList;
    }
}
