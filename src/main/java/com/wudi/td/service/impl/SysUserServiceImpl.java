package com.wudi.td.service.impl;

import com.wudi.td.dao.primary.SysUserMapper;
import com.wudi.td.entity.primary.SysUser;
import com.wudi.td.service.SysUserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;



    @Override
    public int insert(SysUser sysUser) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        return 1;
    }

    @Override
    public SysUser selectByUserName(String userName) {
        if (!StringUtils.isEmpty(userName)){
            SysUser sysUser = sysUserMapper.selectByUserName(userName);
            if (sysUser!=null){
                return sysUser;
            }
        }
        return null;
    }
}
