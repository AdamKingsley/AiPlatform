package cn.edu.nju.software.common.initial;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mengf on 2018/4/26 0026.
 */
public class BindingInitializer extends ConfigurableWebBindingInitializer {
    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        super.initBinder(binder, request);
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
