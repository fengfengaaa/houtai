package com.jingbo.houtai.web;

import com.jingbo.houtai.result.JsonResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class BaseController {

    public JsonResult processValid(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                if (error.getField().equals("createDate") || error.getField().equals("updateDate")) {
                    continue;
                } else {
                    return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
                }
            }
        }
        return null;
    }

}
