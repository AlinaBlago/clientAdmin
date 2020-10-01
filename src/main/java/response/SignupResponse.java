package response;

import java.util.Set;

public class SignupResponse {
    private String name;
    private String login;
    private String password;
    private boolean status;
    private Set<String> role;

    public SignupResponse() {}

    public SignupResponse( String name,  String login,  String password, boolean status) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
