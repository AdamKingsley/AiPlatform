package cn.edu.nju.software.service;

import cn.edu.nju.software.command.ChangePasswordCommand;
import cn.edu.nju.software.command.RegisterCommand;
import cn.edu.nju.software.command.ResetPasswordCommand;
import cn.edu.nju.software.command.UserPageCommand;
import cn.edu.nju.software.common.shiro.RoleEnum;
import cn.edu.nju.software.common.shiro.StateEnum;
import cn.edu.nju.software.common.exception.ExceptionEnum;
import cn.edu.nju.software.common.exception.ServiceException;
import cn.edu.nju.software.common.result.PageResult;
import cn.edu.nju.software.common.result.Result;
import cn.edu.nju.software.common.shiro.ShiroUser;
import cn.edu.nju.software.common.shiro.ShiroUtils;
import cn.edu.nju.software.dto.UserDto;
import cn.edu.nju.software.entity.User;
import cn.edu.nju.software.mapper.UserMapper;
import cn.edu.nju.software.util.DigestsUtil;
import cn.edu.nju.software.util.EncodeUtil;
import cn.edu.nju.software.util.PasswordHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by mengf on 2018/4/9 0009.
 */
@Service
public class AccountService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailService mailService;

    public Result register(RegisterCommand command) {
        command.validate();
        //用户名不能冲突
        if (userMapper.countByUsername(command.getUsername()) > 0) {
            return Result.error().errorMessage("用户名已被占用");
        }
        //邮箱不能冲突
        if (userMapper.countByMail(command.getMail()) > 0) {
            return Result.error().errorMessage("邮箱已经被注册");
        }
        User user = new User();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(command, user);
        //密码加密 HASH_ALGORITHM ITERATION_TIMES USE_HEX_ENCODE
        //user.setSalt(EncodeUtil.encodeHex(DigestsUtil.generateSalt(PasswordHelper.SALT_LENGTH)));
        //String password = new SimpleHash(PasswordHelper.HASH_ALGORITHM, command.getPassword(),
        //        EncodeUtil.decodeHex(user.getSalt()), PasswordHelper.ITERATION_TIMES).toHex();
        String password = encryptPassword(user, command.getPassword());
        user.setPassword(password);
        //插入并返回主键
        //如果是管理员
        if (user.getRoleId() == RoleEnum.ADMIN.getRoleId()) {
            user.setState(StateEnum.ACTIVED.getState());
        }
        //如果是老师2/普通用户考生3
        else {
            user.setState(StateEnum.NOT_ACTIVE.getState());
        }
        user.setCreateTime(new Date());
        user.setModifyTime(user.getCreateTime());
        userMapper.insert(user);
        BeanUtils.copyProperties(user, userDto);
        if (userDto.getState() == StateEnum.NOT_ACTIVE.getState()) {
            mailService.sendActiveMail(userDto.getUsername(), userDto.getMail());
        }
        return Result.success().withData(userDto).message("注册成功，请查收激活邮件！");
    }

    public User findByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        return user;
    }

    public UserDto findByMailAddress(String mail) {
        UserDto user = userMapper.selectByMail(mail);
        return user;
    }

    public void changePassword(ChangePasswordCommand command) {
        command.validate();
        ShiroUser currentUser = ShiroUtils.currentUser();
        User user = userMapper.selectByPrimaryKey(currentUser.getId());
        String password = encryptPassword(user, command.getPassword());
        user.setPassword(password);
        user.setModifyTime(new Date());
        userMapper.updateByPrimaryKey(user);
    }

    public void resetPassword(ResetPasswordCommand command) {
        User user = userMapper.selectByPrimaryKey(command.getUserId());
        String password = encryptPassword(user, command.getPassword());
        user.setPassword(password);
        userMapper.updateByPrimaryKey(user);
    }

    public String encryptPassword(User user, String password) {
        //密码加密 HASH_ALGORITHM ITERATION_TIMES USE_HEX_ENCODE
        user.setSalt(EncodeUtil.encodeHex(DigestsUtil.generateSalt(PasswordHelper.SALT_LENGTH)));
        String encrypt_password = new SimpleHash(PasswordHelper.HASH_ALGORITHM, password,
                EncodeUtil.decodeHex(user.getSalt()), PasswordHelper.ITERATION_TIMES).toHex();
        return encrypt_password;
    }

    public Result active(String code) {
        String username = new String(EncodeUtil.decodeBase64(code));
        UserDto userDto = userMapper.selectByUsername(username);
        if (userDto == null) {
            return Result.error().exception(ExceptionEnum.UNKNOWN_USER);
        }
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        int state = user.getState();
        if (state == StateEnum.NOT_ACTIVE.getState()) {
            user.setState(StateEnum.ACTIVED.getState());
            userMapper.updateByPrimaryKey(user);
            return Result.success().message("激活用户成功，请登录！");
        } else {
            return Result.error().exception(ExceptionEnum.ACTIVE_DUPLICATED);
        }
    }

    public Result delete(List<Long> ids) {
        if (ids.contains(ShiroUtils.currentUser().getId())) {
            throw new ServiceException("不能删除当前账户");
        }
        Example example = new Example(User.class);
        example.createCriteria().andIn("id", ids);
        userMapper.deleteByExample(example);
        return Result.success().message("删除所选用户成功！");
    }

    public Result block(List<Long> ids) {
        if (ids.contains(ShiroUtils.currentUser().getId())) {
            throw new ServiceException("不能冻结当前账户");
        }
        Example example = new Example(User.class);
        example.createCriteria().andIn("id", ids);
        User user = new User();
        user.setState(StateEnum.BLOCKED.getState());
        userMapper.updateByExampleSelective(user, example);
        return Result.success().message("冻结所选用户成功!");
    }

    public Result reactive(List<Long> ids) {
        if (ids.contains(ShiroUtils.currentUser().getId())) {
            throw new ServiceException("不能重新激活当前用户！");
        }
        Example example = new Example(User.class);
        example.createCriteria().andIn("id", ids);
        List<User> users = userMapper.selectByExample(example);
        for (User user : users) {
            mailService.sendActiveMail(user.getUsername(), user.getMail());
        }
        return Result.success().message("对所选用户重新发送激活邮件成功");
    }

    public PageResult list(UserPageCommand command) {
        //TODO 返回用户列表
        PageHelper.startPage(command.getPageNum(), command.getPageSize());
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (command.getState() != null && command.getState() != -1) {
            criteria = criteria.andEqualTo("state", command.getState());
        }
        if (command.getStartCreateTime() != null) {
            criteria = criteria.andGreaterThanOrEqualTo("create_time", command.getStartCreateTime());
        }
        if (command.getEndCreateTime() != null) {
            criteria = criteria.andLessThanOrEqualTo("create_time", command.getEndCreateTime());
        }
        if (command.getStartModifyTime() != null) {
            criteria = criteria.andGreaterThanOrEqualTo("modify_time", command.getStartModifyTime());
        }
        if (command.getEndModifyTime() != null) {
            criteria = criteria.andLessThanOrEqualTo("modify_time", command.getEndModifyTime());
        }
        Page<User> page = (Page<User>) (userMapper.selectByExample(example));
        //TODO User to UserDto

        return null;
    }
}


