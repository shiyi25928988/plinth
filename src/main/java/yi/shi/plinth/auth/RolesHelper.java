package yi.shi.plinth.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RolesHelper {

    private static Map<Object, List<String>> map = new ConcurrentHashMap<>();

    public static void addRoles(Object id, List<String> roles){
        if(map.containsKey(id)){
            map.get(id).addAll(roles);
        }else{
            map.put(id, roles);
        }
    }

    public static void addRole(Object id, String role){
        if(map.containsKey(id)){
            map.get(id).add(role);
        }else{
            List<String> roles = new ArrayList<>();
            roles.add(role);
            map.put(id, roles);
        }
    }

    public static List<String> getRoles(Object id){
        return map.get(id);
    }

}
