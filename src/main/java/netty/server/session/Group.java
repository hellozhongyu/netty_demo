package netty.server.session;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
/**
 * 聊天组，即聊天室
 */
public class Group {
    // 聊天室名称
    private String name;

    public String getName() {
        return name;
    }

    public Set<String> getMembers() {
        return members;
    }

    // 聊天室成员
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
