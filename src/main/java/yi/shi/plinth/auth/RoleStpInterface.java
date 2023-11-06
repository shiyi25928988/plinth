package yi.shi.plinth.auth;

import cn.dev33.satoken.stp.StpInterface;

import java.util.ArrayList;
import java.util.List;

public class RoleStpInterface implements StpInterface {

    //TODO 实现人员信息的 持久化
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Roles.getRoles(loginId);
    }
}
