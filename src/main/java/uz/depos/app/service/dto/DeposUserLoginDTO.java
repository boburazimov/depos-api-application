package uz.depos.app.service.dto;

public class DeposUserLoginDTO {

    private Integer inn;

    private String login;

    public DeposUserLoginDTO() {}

    public DeposUserLoginDTO(Integer inn, String login) {
        this.inn = inn;
        this.login = login;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "DeposUserLoginDTO{" + "inn=" + inn + ", login='" + login + '\'' + '}';
    }
}
