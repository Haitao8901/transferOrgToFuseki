package Haitao.TransferToFuseki;

import org.apache.commons.lang.StringUtils;

public class Constants {
	public static String DEV_URL = "jdbc:mysql://192.168.0.55:6306/iplatform_dev";
	public static String SECURITY_URL = "jdbc:mysql://192.168.0.55:6306/ipf_security_dev";
	public static String DEV_NAME = "iplatform_dev";
	public static String SECURITY_NAME = "ipf_security_dev";
	public static String DEV_PWD = "123456";
	public static String SECURITY_PWD = "123456";
	public static String FUSEKI_URL = "http://192.168.0.51:17021/fuseki_2.3.1";
	public static String ORGID = "";
	public static String APPID = "";
	public static boolean TRANSFERAPP = true;
	public static boolean TRANSFERORG = true;

	public static final int LOCAL = 13;
	public static final int REMOTE = 196;
	
	public static void init(int _13OR196, String orgid, String appid){
		Log.info("DB type is " + _13OR196);
		if(!StringUtils.isEmpty(appid) && !"null".equalsIgnoreCase(appid)){
			Log.info("Transfer APPID is " + appid);
			APPID = appid;
			TRANSFERAPP = true;
			TRANSFERORG = false;
		}
		
		if(!StringUtils.isEmpty(orgid) && !"null".equalsIgnoreCase(orgid)){
			Log.info("Transfer ORGID is " + orgid);
			ORGID = orgid;
			TRANSFERAPP = true;
			TRANSFERORG = true;
		}
		
		switch(_13OR196){
		case LOCAL:
			DEV_URL = "jdbc:mysql://192.168.0.55:6306/iplatform_dev";
			SECURITY_URL = "jdbc:mysql://192.168.0.55:6306/ipf_security_dev";
			DEV_NAME = "iplatform_dev";
			SECURITY_NAME = "ipf_security_dev";
			DEV_PWD = "123456";
			SECURITY_PWD = "123456";
//			FUSEKI_URL = "http://192.168.0.51:17021/fuseki_2.3.1";
			FUSEKI_URL = "http://192.168.254.196:17021/fuseki2";
			break;
		case REMOTE:
			//196 
			DEV_URL = "jdbc:mysql://192.168.254.190:6306/iplatform_prod";
			SECURITY_URL = "jdbc:mysql://192.168.254.190:6306/ipf_security_prod";
			DEV_NAME = "iplatform_prod";
			SECURITY_NAME = "ipf_security_pro";
			DEV_PWD = "123456";
			SECURITY_PWD = "123456";
			FUSEKI_URL = "http://192.168.254.196:17021/fuseki2";
			break;
		default:
			break;
		}
	}
}
