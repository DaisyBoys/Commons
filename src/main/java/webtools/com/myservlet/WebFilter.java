package webtools.com.myservlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebFilter implements Filter {
	protected  void finalize(){
		
		//System.out.println("------");
    } 
	public void destroy() {
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			final FilterChain arg2) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest)arg0;
			HttpServletResponse response =(HttpServletResponse)arg1;
			
			String acceptEncoding =request.getHeader("Accept-Encoding");
			//支持的编码方式
			
			arg0.setCharacterEncoding(this.enCode);
			arg1.setCharacterEncoding(this.enCode);
//			HttpServletRequest httpRequest = (HttpServletRequest) request;
//			String url = httpRequest.getRequestURL().toString();
//		
//			String param =  httpRequest.getQueryString();
//			boolean bChk=chkParam(param);
//			if(!bChk){
//				response.sendRedirect("/000000");
//				return ;
//			}
//			//System.out.println(param);
//			if(acceptEncoding != null && acceptEncoding.toLowerCase().indexOf("gzip") != -1){
//				//如果客户浏览器支持GZIP格式，则使用GZIP压缩数据
//				GZipResponseWrapper gzipRes = new GZipResponseWrapper(response);
//				
//				arg2.doFilter(request, gzipRes);//doFilter,使用自定义的response
//				gzipRes.finishResponse();//输出压缩数据
//				
//			}else{
//				arg2.doFilter(request, response);//否则不压缩
//			}

			arg2.doFilter(request, response);
			
			/*
			arg0.setCharacterEncoding(this.enCode);
			arg1.setCharacterEncoding(this.enCode);
		 
			
			arg2.doFilter(arg0, arg1);
			*/
		} catch (IOException e) {
			// TODO: handle exception
			//System.gc();
		}catch(ServletException se){
			//System.gc();
		}
		
		//arg0=null;
		//arg1=null;
		//arg2=null;
		//this.finalize();
	}

	private String enCode="UTF-8";
	
	public void init(final FilterConfig arg0) throws ServletException {
		String code=arg0.getInitParameter("encode");
		if (code!=null) {
			if (!code.trim().equals("")) {
				this.enCode=code;
			}
		}
	}
	public void doGet(final HttpServletRequest request, final HttpServletResponse response)
	throws IOException, ServletException {
		OutputStream out = null;
		String encoding = request.getHeader("Accept-Encoding"); 
		if (encoding != null && encoding.indexOf("gzip") != -1){
			((HttpServletResponse) request).setHeader("Content-Encoding" , "gzip");
			out = new GZIPOutputStream(((ServletResponse) request).getOutputStream());
		} 
		else if (encoding != null && encoding.indexOf("comdivss") != -1){
		((HttpServletResponse) request).setHeader("Content-Encoding" , "comdivss");
		out = new ZipOutputStream(((ServletResponse) request).getOutputStream());
		}else{
			out = ((ServletResponse) request).getOutputStream();
		}
	}
	private boolean chkParam(final String param){
		try {
			//==http://www.lnyy.com.cn:80/service/OnlineQTModi.jsp?id=1172%27and+ROW_COUNT%28%29%3DROW_COUNT%28%29+and%27%27%3D%27
			if(param==null)return true;
			String inj_str = "and|exec|insert|select|delete|update|mysql.user|ROW_COUNT|</script>|ScriPt|iframe".toLowerCase();
				//这里的东西还可以自己添加
				String[] inj_stra=inj_str.split("\\|");
				String str=param.toLowerCase();
				for (int i=0 ; i <inj_stra.length ; i++ ){
					if(str.indexOf(inj_stra[i])>=0){
						return false;
					}
				}				
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
}
