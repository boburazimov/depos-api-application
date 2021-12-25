/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uz.depos.app.web.eds;

//import com.ibm.icu.text.RuleBasedNumberFormat;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author Mukhriddin
 */
@Service("commonService")
public class CommonServices {

    public String getRequestValue(HttpServletRequest request, String name, String isnull) {
        return request.getParameter(name) != null ? request.getParameter(name) : isnull;
    }

    public String getRandomInt() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    public long getCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis()).getTime();
    }

    public String getPinflOID() {
        return "1.2.860.3.16.1.2";
    }

    public String getYurTinOID() {
        return "1.2.860.3.16.1.1";
    }
    //    public static String numberToWords(BigDecimal summa) {
    //        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag("ru"), RuleBasedNumberFormat.SPELLOUT);
    //        String numericStr = String.format("%1$.2f", summa);
    //        int lengthUntilPoint = numericStr.replaceAll(",", ".").indexOf(".") + 1;
    //        return nf.format(Long.parseLong(numericStr.substring(0, lengthUntilPoint - 1))) + " сум " + Long.parseLong(numericStr.substring(lengthUntilPoint)) + " тийин";
    //    }
}
