/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds.dsvs.json.view;

/**
 *
 * @author Mukhriddin
 */
public class PublicKeyInfoView {

    /**
     * название алгоритма ключа
     */
    private String keyAlgName;
    /**
     * открытый ключ
     */
    private String publicKey;

    public PublicKeyInfoView() {}

    public PublicKeyInfoView(String keyAlgName, String publicKey) {
        this.keyAlgName = keyAlgName;
        this.publicKey = publicKey;
    }

    public String getKeyAlgName() {
        return keyAlgName;
    }

    public void setKeyAlgName(String keyAlgName) {
        this.keyAlgName = keyAlgName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
