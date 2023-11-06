package yi.shi.plinth.auth;

import cn.dev33.satoken.stp.StpUtil;
import yi.shi.plinth.auth.model.User;

public class AuthService {

    public void login(User user){
        RolesHelper.addRoles(user, user.getRoleList());
        StpUtil.login(user);
    }


}
