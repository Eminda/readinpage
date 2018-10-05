package ethirium.eth.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 3566858539223708422L;

    private String userName;
    private String password;
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserDto{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", newPassword='").append(newPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
