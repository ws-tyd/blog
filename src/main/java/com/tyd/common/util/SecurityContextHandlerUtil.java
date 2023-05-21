package com.tyd.common.util;

import com.tyd.module.pojo.User;
import com.tyd.module.pojo.Vo.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHandlerUtil {

    public static User getUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof  MyUserDetails){
            return  ((MyUserDetails) principal).getUser() ;
        }
        return null;
    }
}
