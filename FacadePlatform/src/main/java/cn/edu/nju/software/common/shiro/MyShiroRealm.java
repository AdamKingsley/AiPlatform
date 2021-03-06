package cn.edu.nju.software.common.shiro;

import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import cn.edu.nju.software.service.AccountService;
import cn.edu.nju.software.util.EncodeUtil;
import cn.edu.nju.software.util.PasswordHelper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by mengf on 2018/4/9 0009.
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private AccountService accountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(shiroUser.getRoleName());
        //如果亦多种角色需要使用addRoles
        //authorizationInfo.addRoles();
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User userInfo = accountService.findByUsername(usernamePasswordToken.getUsername());
        if (userInfo != null) {
            ShiroUser shiroUser = new ShiroUser();
            BeanUtils.copyProperties(userInfo, shiroUser);
            shiroUser.setRoleName(RoleEnum.valueOf(userInfo.getRoleId()).toString().toLowerCase());
            // shiroUser 的basicinfo和UserInfo的相关信息有待完善
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    shiroUser, //用户名
                    userInfo.getPassword(), //密码
                    //ByteSource.Util.bytes(EncodeUtil.decodeHex(EncodeUtil.encodeHex(userInfo.getSalt().getBytes()))),//salt生成策略待定
                    ByteSource.Util.bytes(EncodeUtil.decodeHex(userInfo.getSalt())),//salt生成策略待定
                    getName()  //realm name
            );
            return authenticationInfo;
        }
        return null;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordHelper.HASH_ALGORITHM);
        matcher.setStoredCredentialsHexEncoded(PasswordHelper.useHexDecode);
        matcher.setHashIterations(PasswordHelper.ITERATION_TIMES);
        setCredentialsMatcher(matcher);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
