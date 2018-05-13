package webtools.req.edit.test;

import webtools.org.common.pfcy.util.Constant;
import webtools.req.edit.EditInfo;
import webtools.req.edit.ReqEditForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class testP1 {
    private ReqEditForm pReqEditForm = new ReqEditForm();
    private EditInfo pEditInfo = new EditInfo();

    public EditInfo getPEditInfo() {
        return pEditInfo;
    }

    public void setPEditInfo(EditInfo editInfo) {
        pEditInfo = editInfo;
    }

    public ReqEditForm getPReqEditForm() {
        return pReqEditForm;
    }

    public void setPReqEditForm(ReqEditForm reqEditForm) {
        pReqEditForm = reqEditForm;
    }

    public void doRespose(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequst(request, response);
    }

    public void doRequst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //初始化参数配置
        pReqEditForm.setQueryTermName("factory_id");
        pReqEditForm.doRespose(request, response);
        String tablename = "factory_info";
        String s[] = pReqEditForm.getPageParamValues("factory_id");

        if (s.length > 0) {
            String str = s[0];
            pReqEditForm.SetMyValidateInfo("factory_id", s[0], "");
            Map mapErr = pReqEditForm.getMapErr();
            if (mapErr != null) {
                pEditInfo.setMapErr(mapErr);
                pEditInfo.setFiledErr("factory_id", "factory_id");
                return;

            }

            //	pReqEditForm.setQuery("quer1","select count(*) from factory_info where factory_id='"+s[0]+"'");
        }

        pReqEditForm.setInitMoidfyByTable("factory_id", tablename, " factory_id='1@1.1'");
        pReqEditForm.setModiInfo(tablename, "factory_id", pReqEditForm.getPageParamValues("factory_id"), Constant.vdEmail.value());
        pReqEditForm.setModiTableWhere(tablename, " factory_id='" + s[0] + "'");
        pReqEditForm.runModify();
        String str = pReqEditForm.getErrMessage("quer1");
        pEditInfo = pReqEditForm.getPEditInfo();//得到相关信息
        pEditInfo.setFiledErr("quer1", "queryerr");
        pEditInfo.setFiledErr(tablename + ".factory_id", "factory_id");

        //System.out.println(str);

    }


}
