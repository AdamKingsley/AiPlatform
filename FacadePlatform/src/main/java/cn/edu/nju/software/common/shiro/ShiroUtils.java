package cn.edu.nju.software.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by mengf on 2018/4/9 0009.
 */
public class ShiroUtils {
    /**
     * 取出Shiro中的当前用户.
     */
    public static ShiroUser currentUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            if(subject != null) {
                return (ShiroUser) subject.getPrincipal();
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    public static String currentUserName() {
        ShiroUser user = currentUser();
        if(user == null) {
            return null;
        }
        return user.getUsername();
    }
}
