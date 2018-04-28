package webtools.com.mythread;

import webtools.common.database.JdbcAgent;

import com.user.fun.SaveLoginUsrType;
import com.userrole.bean.FunctionDefine;

/**
 * 初始化任务功能树
 * */
public class TaskInitFunctionTree extends Thread{

	private JdbcAgent jAgent=new JdbcAgent();
	@Override
	public void run(){
		try{
		 
			initFunction();
			
		}catch( Exception e){}
		
		/***
		 * 用户类型
		 * */
		try {
			//SaveLoginUsrType pSaveLoginUsrType =new SaveLoginUsrType();
			//pSaveLoginUsrType.doSave();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private  void initFunction(){
		try{
			String str="insert into funtree(id,funname,pid,level,entrance_addr,addr_order,use_flag,ico_file_name)";
			
			jAgent.update("update funtree set use_flag='0'");//将使用标志设置为不使用状态
			
			for (int i=0;i<FunctionDefine.fun.length;i++){
				try
				{
					Thread.sleep(10);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				String []strList=FunctionDefine.fun[i];
				if (modifyFunByID(strList)>0){
					continue;
				}
				String sqlU=str+"values(";
				for (int j=0;j<strList.length;j++){
					sqlU+="'"+strList[j]+"'";
				  	if (j<strList.length-1){
				  		sqlU+=",";
				  	}
				}
				sqlU+=")";
				int nRet=jAgent.update(sqlU);
				
			}
			jAgent.update("delete from  funtree where use_flag='0'");//删除不用的功能树
			
			
		}catch(Exception e){}
	}
	
	private  int modifyFunByID(String []strList){
		String sqlU="update funtree set funname='"+strList[1]+"'" +
				",pid='"+strList[2]+"'" +
				",level='"+strList[3]+"'" +
				",entrance_addr='"+strList[4]+"'" +
				",addr_order='"+strList[5]+"'" +
				",use_flag='"+strList[6]+"'" +
				",ico_file_name='"+strList[7]+"'" +
				" where id='"+strList[0]+"'";
		
		return jAgent.update(sqlU);
	}
}