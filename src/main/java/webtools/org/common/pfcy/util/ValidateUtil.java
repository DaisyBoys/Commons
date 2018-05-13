package webtools.org.common.pfcy.util;


public interface ValidateUtil {


    public boolean matcher(String compileStr, String value);


    public boolean isNull(String value);


    public boolean isSize(int sizeNum, String value);


    public boolean maxSize(int sizeNum, String value);

}
