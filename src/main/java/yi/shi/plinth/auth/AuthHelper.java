package yi.shi.plinth.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.collect.Lists;

import java.util.List;


public final class AuthHelper {

    public static void login(Long userId, String...roles){
        RolesHelper.addRoles(userId, Lists.newArrayList(roles));
        StpUtil.login(userId);
    }

    public static void login(Long userId, List<String> roles){
        RolesHelper.addRoles(userId, roles);
        StpUtil.login(userId);
    }

    public static void logout() {
        StpUtil.logout();
    }


}
