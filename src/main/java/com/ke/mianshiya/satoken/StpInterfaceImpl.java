package com.ke.mianshiya.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.ke.mianshiya.constant.UserConstant;
import com.ke.mianshiya.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ke.mianshiya.constant.UserConstant.USER_LOGIN_STATE;

@Component
public class StpInterfaceImpl implements StpInterface {
    /**
     * 返回一个账号所拥有的权限码集合 目前本项目没有按照角色行为进行鉴权
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //从当前登录用户信息中获取角色       request.getSession().setAttribute(USER_LOGIN_STATE, user); 登录时向Session中设置的key要一样的取出来
        User user = (User) StpUtil.getSessionByLoginId(loginId).get(USER_LOGIN_STATE);
        //这里取出来的应该只有一个值，所以之江单个值的列表返回
        return Collections.singletonList(user.getUserRole());
    }
}
