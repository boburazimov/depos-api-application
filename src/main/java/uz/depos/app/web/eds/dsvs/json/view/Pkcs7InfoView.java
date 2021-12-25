/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds.dsvs.json.view;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mukhriddin
 */
public class Pkcs7InfoView {

    /**
     * список информации о подписчиках
     */
    private List<Pkcs7SignerInfoView> signers = new LinkedList();
    /**
     * документ
     */
    private String documentBase64;

    public Pkcs7InfoView() {}

    public String getDocumentBase64() {
        return documentBase64;
    }

    public void setDocumentBase64(String documentBase64) {
        this.documentBase64 = documentBase64;
    }

    public boolean add(Pkcs7SignerInfoView e) {
        return signers.add(e);
    }

    public List<Pkcs7SignerInfoView> getSigners() {
        return signers;
    }
}
