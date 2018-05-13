package webtools.req.edit;

import webtools.org.common.pfcy.util.Constant;

/*我的验证错误信息*/
public final class MyValidateInfo {

    public String getValidate(String prex) {

        if (Constant.vdChinese.value().equalsIgnoreCase(prex)) {
            return "必须为中文";

        }

        if (Constant.vdEnglish.value().equalsIgnoreCase(prex)) {
            return "必须为英文";

        }
        if (Constant.vdNumber.value().equalsIgnoreCase(prex)) {
            return "必须为英文";

        }
        if (Constant.vStrNum.value().equalsIgnoreCase(prex)) {
            return "只能是字母、数字、减号、下划线组成";

        }
        if (Constant.vdInteger.value().equalsIgnoreCase(prex)) {
            return "必须为整型";

        }
        if (Constant.vdIntegerZ.value().equalsIgnoreCase(prex)) {
            return "必须为正整型";

        }
        if (Constant.vdFloat.value().equalsIgnoreCase(prex)) {
            return "必须为单精度浮点型";

        }
        if (Constant.vdDouble.value().equalsIgnoreCase(prex)) {
            return "必须为双精度浮点";

        }
        if (Constant.vdString.value().equalsIgnoreCase(prex)) {
            return "必须为字符串";

        }
        if (Constant.vdInt.value().equalsIgnoreCase(prex)) {
            return "必须为正整数";

        }
        if (Constant.vdMinusint.value().equalsIgnoreCase(prex)) {
            return "必须为负整数";

        }
        if (Constant.vdDate.value().equalsIgnoreCase(prex)) {
            return "必须为日期类型(2004-08-12)";

        }
        if (Constant.vdTime.value().equalsIgnoreCase(prex)) {
            return "必须为时间类型(08:37:29)";

        }
        if (Constant.vdDatetime.value().equalsIgnoreCase(prex)) {
            return "必须为日期时间型(2004-08-12 08:37:29)";

        }
        if (Constant.vdDatehm.value().equalsIgnoreCase(prex)) {
            return "必须为日期时分型(2004-08-12 12:25)";

        }
        if (Constant.vdYear.value().equalsIgnoreCase(prex)) {
            return "必须为日期的年";

        }
        if (Constant.vdMonth.value().equalsIgnoreCase(prex)) {
            return "必须为日期的月";

        }
        if (Constant.vdDay.value().equalsIgnoreCase(prex)) {
            return "必须为日期的日";

        }
        if (Constant.vdPostcode.value().equalsIgnoreCase(prex)) {
            return "必须为邮编(100001)";

        }
        if (Constant.vdEmail.value().equalsIgnoreCase(prex)) {
            return "必须为邮箱验证(msm@hotmail.com)";

        }
        if (Constant.vdPhone.value().equalsIgnoreCase(prex)) {
            return "必须为电话号码(010-67891234)";

        }
        if (Constant.vdMobiletel.value().equalsIgnoreCase(prex)) {
            return "必须为移动电话号码(13867891234)";

        }
        if (Constant.vdIp.value().equalsIgnoreCase(prex)) {
            return "必须为IP地址";

        }
        if (Constant.vdIdcard.value().equalsIgnoreCase(prex)) {
            return "必须为身份证验证";

        }
        if (Constant.vdNumAndStr.value().equalsIgnoreCase(prex)) {
            return "必须为数字和字母字符串";

        }
        if (Constant.vdLetterStr.value().equalsIgnoreCase(prex)) {
            return "必须为纯字母字符串";

        }
        if (Constant.vdEmpty.value().equalsIgnoreCase(prex)) {
            return "必须为空字符串(\"\"|\"   \")";

        }

        return "";

    }

}
