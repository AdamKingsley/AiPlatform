package cn.edu.nju.software.common.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mengf on 2018/4/6 0006.
 */

@Configuration
public class DruidConfiguration {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DruidStatViewServlet druidStatViewServlet;
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private DruidWebStatFilter druidWebStatFilter;

    @Bean
    public ServletRegistrationBean DruidStatViewServlet() {
        System.out.println("------------------- init DruidStatViewServlet ----------------");
        System.out.println(druidStatViewServlet.toString());
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), druidStatViewServlet.getUrlPattern());
        servletRegistrationBean.setEnabled(druidStatViewServlet.getEnabled());

        servletRegistrationBean.addInitParameter("allow", druidStatViewServlet.getAllow());
        servletRegistrationBean.addInitParameter("deny", druidStatViewServlet.getDeny());

        servletRegistrationBean.addInitParameter("loginUsername", druidStatViewServlet.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", druidStatViewServlet.getLoginPassword());

        servletRegistrationBean.addInitParameter("resetEnable", druidStatViewServlet.getResetEnable().toString());

        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {

        System.out.println("----------------------- init DruidStatFilter ------------------------ ");
        System.out.println(druidWebStatFilter.toString());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        filterRegistrationBean.setEnabled(druidStatViewServlet.getEnabled());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns(druidWebStatFilter.getUrlPattern());
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", druidWebStatFilter.getExclusions());
        return filterRegistrationBean;
    }
}
