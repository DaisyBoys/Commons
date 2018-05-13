package webtools.org.test.validate;

import webtools.org.common.pfcy.util.Constant;
import webtools.org.common.pfcy.util.ValidateUtilImpl;

public class ValidateTest {

    public static void main(String[] args) throws Exception {

        ValidateUtilImpl validate = new ValidateUtilImpl();

        System.out.println("" + validate.matcher(Constant.vdChinese.value(), "hah"));
        System.out.println("" + validate.matcher(Constant.vdEnglish.value(), "sdfa"));
        System.out.println("IP" + validate.matcher(Constant.vdIp.value(), "1.123.1s2.0"));
        String str = Constant.vdLetterStr.value();

        System.out.println(validate.maxSize(2, ""));
    }


}
