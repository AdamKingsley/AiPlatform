package cn.edu.nju.software.service;

import cn.edu.nju.software.command.RegisterCommand;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import cn.edu.nju.software.mapper.UserMapper;
import cn.edu.nju.software.util.DigestsUtil;
import cn.edu.nju.software.util.EncodeUtil;
import cn.edu.nju.software.util.PasswordHelper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mengf on 2018/4/9 0009.
 */
@Service
public class AccountService {

    @Autowired
    UserMapper userMapper;


    public Result register(RegisterCommand command) {
        //用户名不能冲突
        if (userMapper.countByUsername(command.getUsername()) > 0) {
            return Result.error().message("用户名已被占用");
        }
        //邮箱不能冲突
        if (userMapper.countByMail(command.getMail()) > 0) {
            return Result.error().message("邮箱已经被注册");
        }

        User user = new User();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(command, user);
        //密码加密 HASH_ALGORITHM ITERATION_TIMES USE_HEX_ENCODE
        user.setSalt(EncodeUtil.encodeHex(DigestsUtil.generateSalt(PasswordHelper.SALT_LENGTH)));
        String password = new SimpleHash(PasswordHelper.HASH_ALGORITHM, command.getPassword(),
                EncodeUtil.decodeHex(user.getSalt()), PasswordHelper.ITERATION_TIMES).toHex();
        user.setPassword(password);
        //插入并返回主键
        userMapper.insert(user);
        BeanUtils.copyProperties(user, userDto);
        return Result.success().withData(userDto).message("注册成功");
    }

    public User findByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        return user;
    }

    public User findByMailAddress(String mail) {
        User user = new User();
        user.setMail(mail);
        user = userMapper.selectOne(user);
        return user;
    }

}


