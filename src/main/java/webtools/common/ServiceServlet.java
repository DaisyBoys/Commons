package webtools.common;

import org.apache.log4j.PropertyConfigurator;

import webtools.common.database.DBManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.*;
import java.io.*;


public class ServiceServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 4978979132779921212L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String real_path = getServletContext().getRealPath("") + "/";
        Utility.setAppPath(real_path);
        // --- initialize log4j ---
        ini_log_properties(real_path);
        // --- initialize db --
        ini_db_properties(real_path);
        // --- initialize app --
        ini_app_properties(real_path);
    }

    protected void ini_app_properties(String real_path) {
        try {
            String dbPropFile = getServletConfig().getInitParameter("db_property_file");
            FileInputStream fin = new FileInputStream(real_path + dbPropFile);
            Properties p = new Properties();
            p.load(fin);
            fin.close();

            String appchar = p.getProperty("application.charset");
            String pagechar = p.getProperty("htmlform.charset");


            Utility.set_app_charcode(appchar, pagechar);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    protected void ini_db_properties(String real_path) {
        try {
            String dbPropFile = getServletConfig().getInitParameter("db_property_file");
            DBManager.InitialConnection(real_path + dbPropFile);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    protected void ini_log_properties(String real_path) {
        try {
            String logPropFile = getServletConfig().getInitParameter("log4j_property_file");
            Properties log_pro = getProperties(real_path, logPropFile);
            if (log_pro != null)
                PropertyConfigurator.configure(log_pro);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected Properties getProperties(String real_path, String logPropFile) {
        String file = real_path + logPropFile;
        try {
            Properties log_pro = new Properties();
            FileInputStream in = new FileInputStream(file);
            log_pro.load(in);
            in.close();
            String err_logfile = (String) log_pro.get("log4j.appender.ERROR_LOG.File");
            String file_app = (String) log_pro.get("log4j.appender.fileAppender.File");
            log_pro.put("log4j.appender.ERROR_LOG.File", real_path + err_logfile);
            log_pro.put("log4j.appender.fileAppender.File", real_path + file_app);
            return log_pro;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
