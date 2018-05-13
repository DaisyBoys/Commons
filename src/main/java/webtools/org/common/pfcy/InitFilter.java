package webtools.org.common.pfcy;

import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;

import javax.servlet.*;
import java.io.IOException;

public class InitFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        String root = arg0.getServletContext().getRealPath("");
        if (arg0.getInitParameter("dbfile") != null) {
            String dbfile = arg0.getInitParameter("dbfile");
            if (dbfile != null) {
                DBValidateCentral dCentral = new DBValidateCentral();

                dCentral.initDBData(root + "/" + dbfile);
            }
        }
    }

}
