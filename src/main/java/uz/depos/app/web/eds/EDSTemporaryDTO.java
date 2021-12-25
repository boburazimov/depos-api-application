package uz.depos.app.web.eds;

public class EDSTemporaryDTO {

    private String pinfl;

    private String fullName;

    private String inn;

    private String serialnumber;

    public EDSTemporaryDTO() {}

    public EDSTemporaryDTO(String pinfl, String fullName, String inn, String serialnumber) {
        this.pinfl = pinfl;
        this.fullName = fullName;
        this.inn = inn;
        this.serialnumber = serialnumber;
    }

    public String getPinfl() {
        return pinfl;
    }

    public void setPinfl(String pinfl) {
        this.pinfl = pinfl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    @Override
    public String toString() {
        return (
            "EDSTemporaryDTO{" +
            "pinfl='" +
            pinfl +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", inn='" +
            inn +
            '\'' +
            ", serialnumber='" +
            serialnumber +
            '\'' +
            '}'
        );
    }
}
