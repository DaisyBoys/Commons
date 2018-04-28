package webtools.org.common.pfcy;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;

public class InitFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
		String root=arg0.getServletContext().getRealPath("");
		// ��ʼ����ݽṹ
		if (arg0.getInitParameter("dbfile") != null) {
			String dbfile = arg0.getInitParameter("dbfile");// ��ݽṹ�ļ��洢λ��
			if (dbfile != null) {
				DBValidateCentral dCentral = new DBValidateCentral();
				
				dCentral.initDBData(root + "/"+ dbfile);
			}
		}
	}

}
