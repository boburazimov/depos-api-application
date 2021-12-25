/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds.dsvs.json;

import uz.depos.app.web.eds.dsvs.json.view.Pkcs7InfoView;

/**
 *
 * @author Azamat M.
 */
public class JsonVerifyPkcs7Response {

    private boolean success;

    private String message;

    private String reason;

    private Pkcs7InfoView pkcs7Info;

    public JsonVerifyPkcs7Response() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Pkcs7InfoView getPkcs7Info() {
        return pkcs7Info;
    }

    public void setPkcs7Info(Pkcs7InfoView pkcs7Info) {
        this.pkcs7Info = pkcs7Info;
    }
}
