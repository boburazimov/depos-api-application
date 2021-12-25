///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package uz.depos.app.web.eds;
//
//import java.security.Provider;
//import java.security.Security;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.springframework.stereotype.Service;
//import uz.yt.pkix.jcajce.provider.YTProvider;
//
///**
// *
// * @author Mukhriddin
// */
//@Service("providerFactory")
//public class ProviderFactory {
//
//    private Provider provider = null;
//
//    public Provider getProvider() {
//        if (provider != null) {
//            return provider;
//        }
//        provider = Security.getProvider("BC");
//        if (provider == null) {
//            BouncyCastleProvider bcp = new BouncyCastleProvider();
//            YTProvider.configure(bcp);
//            Security.addProvider(bcp);
//            provider = bcp;
//        }
//        return provider;
//    }
//
//}
