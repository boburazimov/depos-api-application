/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import uz.depos.app.web.eds.dsvs.json.JsonVerifyPkcs7Response;
import uz.depos.app.web.eds.yt_pkcs7.Pkcs7;
import uz.depos.app.web.eds.yt_pkcs7.Pkcs7Service;

//import uz.yt.cams.client.factory.ClientFactory;

/**
 *
 * @author Mukhriddin
 */
@Service("pkiManager")
public class PkiManager {

    @Autowired
    private GsonBuilder getGson;

    @Autowired
    private Environment environment;

    //    @Autowired
    //    private ClientFactory clientFactory;

    private static final Logger LOG = Logger.getLogger(PkiManager.class.getName());

    private Pkcs7 pkcs7Port;

    private Pkcs7 getPkcs7() throws Exception {
        if (pkcs7Port == null) {
            try {
                Pkcs7Service service = new Pkcs7Service(
                    new URL(environment.getProperty("eimzo.service.url")),
                    new QName("http://v1.pkcs7.plugin.server.dsv.eimzo.yt.uz/", "Pkcs7Service")
                );
                pkcs7Port = service.getPkcs7Port();
            } catch (Throwable t) {
                LOG.log(Level.SEVERE, "", t);
                throw new Exception(t);
            }
        }
        return pkcs7Port;
    }

    public JsonVerifyPkcs7Response verifyPkcs7(String pkcs7) throws Exception {
        try {
            Gson gson = getGson.setPrettyPrinting().create();
            String json = getPkcs7().verifyPkcs7(pkcs7);
            JsonVerifyPkcs7Response r = gson.fromJson(json, JsonVerifyPkcs7Response.class);
            //            if(r.get.getException() != null) {
            //                LOG.log(Level.WARNING, "SIGNATURE VERIFICATION EXCEPTION : {0} SN: {1} SUBJECT: {2} ISSUER: {3}\n\t--------------------------------\n\t{4}\n\t--------------------------------\n{5}\n\t--------------------------------", new String[]{signerInfo.getException(), serialNumber, subjectName, issuerName, pkcs7, signerInfo.get});
            //            }
            //
            return r;
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, "", t);
            throw new Exception(t);
        }
    }
    //    public String getTimeStampToken(String timeStampRequest) throws Exception {
    //
    //        if (timeStampRequest == null || timeStampRequest.trim().isEmpty()) {
    //            throw new Exception("time_stamp_token.timestamprequest_required");
    //        }
    //
    //        TimeStampRequest tsr;
    //        try {
    //            tsr = new TimeStampRequest(Base64.decode(timeStampRequest));
    //        } catch (IOException ex) {
    //            throw new Exception(ex.getMessage());
    //        }
    //        TimeStampToken tst = null;
    //        try {
    //
    //            tst = clientFactory.getRemoteClient(null).getTimeStampToken(tsr);
    //
    //        } catch (Exception ex) {
    //            throw new Exception(ex.getMessage());
    //        }
    //
    //        String timeStampToken = Base64.toBase64String(tst.getEncoded());
    //
    //        return timeStampToken;
    //    }
}
