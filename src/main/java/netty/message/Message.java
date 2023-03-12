package netty.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {

    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract  int getMessageType();
    public abstract  int getSequenceId();

    public static final int LoginRequestMessgae = 0;
    public static final int LoginResonseMessgae = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResonseMessgae = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResonseMessgae = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResonseMessgae = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResonseMessgae = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResonseMessgae = 11;
    public static final int GroupMemberRequestMessage = 12;
    public static final int GroupMemberResonseMessgae = 13;
    public static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    static{

    }
}
