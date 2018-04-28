package webtools.org.common.pfcy.database.dbvalidate.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误消息编码相关处理
 * @author jack.dong
 *
 */
public class ErrorMessage implements ErrorInfo {

	private static List<String> errorMessages=new ArrayList<String>();
	
	public ErrorMessage(){
		initData();
	}
	
	//初始化数据
	private void initData(){
		if (errorMessages.size()==0) {
			errorMessages.add("校验数据没有初始化。");//0
			errorMessages.add("没有找到要校验的数据");
			errorMessages.add("为空验证失败。");
			errorMessages.add("长度验证失败。");
			
			errorMessages.add("自定义验证没有提供验证规则。");
			errorMessages.add("自定义验证规则不匹配。");//11
			
			
			errorMessages.add("整型数据验证失败。");
			errorMessages.add("双精度浮点数据验证失败。");
			errorMessages.add("单精度浮点数据验证失败。");
			errorMessages.add("日期时间类型数据验证失败。");
			errorMessages.add("日期数据验证失败。");
			
			errorMessages.add("没有提供该类型数据的验证。");
			
		}
	}
	
	public String getErrorMessage(int errorNumber) {
		String result="未检测到错误";
		if(errorNumber>-1&&errorNumber<errorMessages.size()){
			
			result=errorMessages.get(errorNumber);
			
		}
		return result;
	}

}
