package yi.shi.plinth.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.collect.Lists;

import java.util.List;


public final class AuthHelper {

    public static void login(Object user, String...roles){
        RolesHelper.addRoles(user, Lists.newArrayList(roles));
        StpUtil.login(user);
    }

    public static void login(Object user, List<String> roles){
        RolesHelper.addRoles(user, roles);
        StpUtil.login(user);
    }

    public static void logout() {
        StpUtil.logout();
    }


}
