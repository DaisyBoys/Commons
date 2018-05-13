package webtools.org.common.pfcy.util;

import java.util.regex.Pattern;


public class ValidateUtilImpl implements ValidateUtil {


    @Override
    public boolean matcher(String compileStr, String value) {
        if (isNull(value) || isNull(compileStr)) {
            return false;
        }
        return Pattern.compile(compileStr).matcher(value).find();
    }


    @Override
    public boolean isNull(String value) {
        return value == null ? true : false;
    }


    @Override
    public boolean isSize(int sizeNum, String value) {
        String compileStr = "^.{" + sizeNum + "}$";
        return matcher(compileStr, value);
    }


    @Override
    public boolean maxSize(int sizeNum, String value) {
        String compileStr = "^.{0," + sizeNum + "}$";
        return matcher(compileStr, value);
    }

}
