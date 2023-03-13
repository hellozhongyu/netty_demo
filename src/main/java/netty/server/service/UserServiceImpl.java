package netty.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImpl  implements  UserService{

    private Map<String, String> allUserMap = new ConcurrentHashMap();

    {
        allUserMap.put("zhangsan", "123");
        allUserMap.put("lsisi", "1234");
        allUserMap.put("wangwu", "12345");
        allUserMap.put("zhaoliu", "123456");
        allUserMap.put("hongqi", "1234567");
    }


    @Override
    public boolean login(String username, String password) {
        boolean isSuccess = allUserMap.keySet().contains(username)? allUserMap.get(username).equals(password): false;
        return isSuccess;
    }
}
