package netty.message;

public class LoginRequestMessage extends Message{

    private String userName;

    public LoginRequestMessage(String userName, String password, String nickName) {
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String password;
    private String nickName;
    @Override
    public int getMessageType() {
        return LoginRequestMessgae;
    }

    @Override
    public int getSequenceId() {
        return 0;
    }
}
