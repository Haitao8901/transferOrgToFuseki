package Haitao.TransferToFuseki;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TransferApp {
	public static void transferApp() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(Constants.DEV_URL, Constants.DEV_NAME, Constants.DEV_PWD);
		doTransfer(con);
	}
	
	private static void doTransfer(Connection con){
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM application WHERE ";
		List<AppInfo> appList = new ArrayList<AppInfo>();
		try{
			if(!StringUtils.isEmpty(Constants.ORGID)){
				sql += " orgid ='" + Constants.ORGID + "' and ";
			}
			if(!StringUtils.isEmpty(Constants.APPID)){
				sql += " id ='" + Constants.APPID + "' and ";
			}
			sql += " 1=1";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				AppInfo appInfo = new AppInfo();
				initAppInfo(rs, appInfo);
				appList.add(appInfo);
			}
			FusekiTool.saveAppInfoToFuseki(appList);
		}catch(Exception e){
			Log.info("Error happened when transfer APP.");
			Log.error(e);
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	private static void initAppInfo(ResultSet rs, AppInfo appInfo) throws Exception{
		int size = rs.getMetaData().getColumnCount();
		for(int i = 1; i<= size; i++) {
			String column = rs.getMetaData().getColumnName(i);
			String methodName = "set" + column.substring(0, 1).toUpperCase() + column.substring(1).toLowerCase();
			Method method = AppInfo.class.getMethod(methodName, new Class[]{String.class});
			method.invoke(appInfo, rs.getString(column));
		}
	}
}
