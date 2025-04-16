package com.justin.usercenterbd.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class RequestRegister implements Serializable {


    private static final long serialVersionUID = -2392317956528863012L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;

    public String getPlanetCode() {
        return planetCode;
    }

    public void setPlanetCode(String planetCode) {
        this.planetCode = planetCode;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}
