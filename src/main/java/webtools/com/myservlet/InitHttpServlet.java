package webtools.com.myservlet;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import tcp.server.TcpInfoRecServer;
import webtools.com.mythread.TaskInitFunctionTree;
import webtools.com.tools.ToolBuildDBFile;
import webtools.org.common.pfcy.database.dbvalidate.dbfile.DBValidateCentral;
import BO.cache.AppCacheCnf;
import BO.components.SysParam;
import BO.thread.ThreadCenter;
import BO.thread.ThreadImgDraw;
import Servlet.callback.CallBackParam;

import com.userrole.bean.FunctionDefine;

import correspondence.tools.CorrespondenceParam;

import dysdataser.client.utils.DataSerParam;
import dystrain.server.bill.pay.PayParam;
import dystrain.server.query.InitQueryCourse;
import dysyscom.SysConfigure;
import dysyscom.cnf.SysCache;
import fscom.tcp.TcpLocalFile2FS;



public class InitHttpServlet extends HttpServlet {
	
	/** 变量:TODO,类型:Logger */
	protected Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskInitFunctionTree pTaskInitFunctionTree;
	private ThreadCenter pThreadCenter = null;
	private ThreadImgDraw pThreadImgDraw=null;
	public boolean g_bDebug=false;
	private TcpInfoRecServer pTcpInfoRecServer=new TcpInfoRecServer();
	
	@Override
	public void destroy() {
		super.destroy();
		
		try {
			if(pThreadCenter!=null){
				pThreadCenter.setFlagStop(true);
				while (!pThreadCenter.bStoped){
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if(pThreadImgDraw!=null){
				pThreadImgDraw.setFlagStop(true);
				while (!pThreadImgDraw.bStoped){
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if(pTcpInfoRecServer!=null){
				pTcpInfoRecServer.setFlagStop(true);
				while (!pTcpInfoRecServer.bStoped){
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private String detectWebRootPath() {
		try {
			String path = InitHttpServlet.class.getResource("/").toURI().getPath();
			return new File(path).getParentFile().getParentFile().getCanonicalPath();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void init(ServletConfig arg0) throws ServletException {
		super.init();
		
		/**/
		//PropKit.use(new File(PathKit.getWebRootPath()+"/WEB-INF/config/db.properties"));
		/**/
		
//		String root=arg0.getServletContext().getRealPath("");
		String root=detectWebRootPath();
		SysParam.webAppRoot=root;//存储用的根目录
		//System.out.println(SysParam.webAppRoot);
		
		try {
			FunctionDefine.g_appDir=root;
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 初始化数据结构
		if (arg0.getInitParameter("dbfile") != null) {
			String dbfile = arg0.getInitParameter("dbfile");// 数据结构文件存储位置
			if (dbfile != null) {
				DBValidateCentral dCentral = new DBValidateCentral();
				
				dCentral.initDBData(root + "/"+ dbfile);
				
			}
		}
		if (arg0.getInitParameter("mydbtable") != null) {
			String mydbtable = arg0.getInitParameter("mydbtable");// 数据结构文件存储位置
			if (mydbtable != null) {
				ToolBuildDBFile pToolBuildDBFile=new ToolBuildDBFile();
				pToolBuildDBFile.doBuild(mydbtable, "dyres_db");
				
			}
		}
		
		
			
		if (arg0.getInitParameter("uploadSerId") != null) {
			String val = arg0.getInitParameter("uploadSerId");// 
			//启动删除任务
			if (!"".equals(val)){
				try {
					CorrespondenceParam.uploadSerId=val;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		//==上传的文件信息=============
		if (arg0.getInitParameter("uploadTcpIp") != null) {
			String val = arg0.getInitParameter("uploadTcpIp");// 
			//启动删除任务
			if (!"".equals(val)){
				try {
					TcpLocalFile2FS.tcp_ip=val.trim();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		if (arg0.getInitParameter("uploadTcpPort") != null) {
			String val = arg0.getInitParameter("uploadTcpPort");// 
			//启动删除任务
			if (!"".equals(val)){
				try {
					TcpLocalFile2FS.tcp_port=Integer.parseInt(val);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		//=======================================
		//==资源信息存储的根目录
		if (arg0.getInitParameter("resInfoRoot") != null) {
			String val = arg0.getInitParameter("resInfoRoot");// 
			if (!"".equals(val)){
				try {
					SysParam.resInfoRoot=val;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		if (arg0.getInitParameter("http_url_fileSer") != null) {
			String val = arg0.getInitParameter("http_url_fileSer");// 
			if (!"".equals(val)){
				try {
					CorrespondenceParam.http_url_fileSer=val;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		//==获取当前业务应用编号
		if (arg0.getInitParameter("appId") != null) {
			String val = arg0.getInitParameter("appId");// 
			//启动删除任务
			if (!"".equals(val)){
				try {
					
					CorrespondenceParam.appId=val;
					AppCacheCnf pAppCacheCnf=new AppCacheCnf();
					pAppCacheCnf.init_mapAppFileCacheCnf(CorrespondenceParam.appId);//初始化文件上传服务
					pAppCacheCnf.init_mapAppLunceneCache(SysParam.resmgr);//初始化全文搜索目录(信息类)
					pAppCacheCnf.init_mapAppLunceneCache(SysParam.publog);//初始化全文搜索目录（发布日志）
					pAppCacheCnf.init_mapAppLunceneCache(SysParam.dainfo);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		if (arg0.getInitParameter("resInfoRoot") != null) {
			String val = arg0.getInitParameter("resInfoRoot");// 
			if (!"".equals(val)){
				try {
					final SysConfigure pSysConfigure=new SysConfigure();
					SysCache.resInfoRoot=val;//系统缓存资源存储目录
					pSysConfigure.initLocalCnf(SysParam.dainfo);//初始化数据采集接口
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		//==本机应用的服务HTTP地址
		if (arg0.getInitParameter("appWebRoot") != null) {
			String appWebRoot = arg0.getInitParameter("appWebRoot");// 数据结构文件存储位置
			if (!"".equals(appWebRoot)){
				CallBackParam.http_url=appWebRoot;
			}
		}
		//==个人用户中心清除栏目缓存
		if (arg0.getInitParameter("puc_CallBackClearRecCatCache") != null) {
			String puc_CallBackClearRecCatCache = arg0.getInitParameter("puc_CallBackClearRecCatCache");// 数据结构文件存储位置
			if (!"".equals(puc_CallBackClearRecCatCache)){
				CallBackParam.puc_CallBackClearRecCatCache=puc_CallBackClearRecCatCache;
			}
		}
		//==算法相关配置信息==BEGIN=
		
		//==网站上首页用数据服务地址
		if (arg0.getInitParameter("ServletURL") != null) {
			String val = arg0.getInitParameter("ServletURL");// 
			if (!"".equals(val)){
				try {
					DataSerParam.ServletURL=val;
					System.out.println(DataSerParam.ServletURL);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}
		
//		String http_url_websvr = SysConfig.get("http_url_websvr");// 
//		if (http_url_websvr != null) {
//			CorrespondenceParam.http_url_websvr=http_url_websvr;
//		}
		//==AJAX的服务地址
		if (arg0.getInitParameter("http_url_websvr") != null) {
			String http_url_websvr = arg0.getInitParameter("http_url_websvr");// 数据结构文件存储位置
			if (!"".equals(http_url_websvr)){
				CorrespondenceParam.http_url_websvr=http_url_websvr;
			}
		}
		//==付款回调地址
		if (arg0.getInitParameter("paymentCallbakUrl") != null) {
			String paymentCallbakUrl = arg0.getInitParameter("paymentCallbakUrl");// 数据结构文件存储位置
			if (!"".equals(paymentCallbakUrl)){
				PayParam.callback_url=paymentCallbakUrl;
			}
		}
		//==支付地址
		if (arg0.getInitParameter("paymentUrl") != null) {
			String paymentUrl = arg0.getInitParameter("paymentUrl");// 数据结构文件存储位置
			if (!"".equals(paymentUrl)){
				PayParam.paymentUrl=paymentUrl;
			}
		}
		if (arg0.getInitParameter("paywaiturl") != null) {
			String paywaiturl = arg0.getInitParameter("paywaiturl");// 数据结构文件存储位置
			if (!"".equals(paywaiturl)){
				PayParam.paywaiturl=paywaiturl;
			}
		}
		
		if(!g_bDebug){
			if (arg0.getInitParameter("InitWebFunctionTree") != null) {
				String val = arg0.getInitParameter("InitWebFunctionTree");// 数据结构文件存储位置
				//启动删除任务
				if ("1".equals(val)){
					pTaskInitFunctionTree=new TaskInitFunctionTree();
					//pTaskDeleteThread.setFlagStop(false);
					pTaskInitFunctionTree.start();
				}
			}
			//==启动信息传输TCP服务
			//this.start_TcpInfoRecServer();
			//doStart_ThreadImgDraw(root);//图片处理
			try {
				this.doStart_ThreadCenter();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//==课程初始化的
		InitQueryCourse pInitQueryCourse=new InitQueryCourse();
		pInitQueryCourse.Init();
	}
	/**
	 * 线程处理中心开始
	 * */
	private void doStart_ThreadCenter(){
		if(pThreadCenter==null) pThreadCenter=new ThreadCenter();
		pThreadCenter.setFlagStop(false);
		pThreadCenter.start();
	}
	private void doStart_ThreadImgDraw(String root){
		try {
			if(pThreadImgDraw==null) pThreadImgDraw=new ThreadImgDraw();
			pThreadImgDraw.root=root;
			pThreadImgDraw.setFlagStop(false);
			pThreadImgDraw.start();
			
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 启动TCP服务器
	 * */
	private void start_TcpInfoRecServer(){
		try {
			if(pTcpInfoRecServer==null) pTcpInfoRecServer=new TcpInfoRecServer();
			pTcpInfoRecServer.setFlagStop(false);
			pTcpInfoRecServer.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
