package nsu.ui;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class User_log {

    private Long id;

    @NotEmpty(message = "Логин - обязательное поле.")
    private String userLogin;

    @NotEmpty(message = "Пароль - обязательное поле.")
    private String userPassword;

    @NotNull(message = "curr - обязательное поле.")
    private Boolean current;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
