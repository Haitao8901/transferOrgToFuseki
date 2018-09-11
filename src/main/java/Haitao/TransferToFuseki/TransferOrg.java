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

public class TransferOrg {
	public static void transferOrg() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection dev_con = DriverManager.getConnection(Constants.DEV_URL, Constants.DEV_NAME, Constants.DEV_PWD);
		Connection security_con = DriverManager.getConnection(Constants.SECURITY_URL, Constants.SECURITY_NAME, Constants.SECURITY_PWD);
		doTransfer(dev_con, security_con);
	}
	
	private static void doTransfer(Connection devCon, Connection securityCon){
		Statement stmt = null;
		ResultSet rs = null;
		String dev_sql = "SELECT * FROM organiztion WHERE ";
		List<OrgInfo> orgList = new ArrayList<OrgInfo>();
		try{
			if(!StringUtils.isEmpty(Constants.ORGID)){
				dev_sql += " id ='" + Constants.ORGID + "' and ";
			}
			dev_sql += " 1=1";
			stmt = devCon.createStatement();
			rs = stmt.executeQuery(dev_sql);
			while(rs.next()){
				OrgInfo orgInfo = new OrgInfo();
				initOrgInfo(rs, orgInfo);
				findSecurityInfo(orgInfo, securityCon);
				orgList.add(orgInfo);
			}
			FusekiTool.saveOrgInfoToFuseki(orgList);
		}catch(Exception e){
			Log.info("Error happened when transfer ORG.");
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
			if(devCon != null){
				try {
					devCon.close();
				} catch (SQLException e) {
				}
			}
			if(securityCon != null){
				try {
					securityCon.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	private static void findSecurityInfo(OrgInfo orgInfo, Connection securityCon) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String security_sql = "SELECT * FROM organize WHERE id ='";
		try{
			security_sql += orgInfo.getId() +"'";
			stmt = securityCon.createStatement();
			rs = stmt.executeQuery(security_sql);
			while(rs.next()){
				initOrgInfo(rs, orgInfo);
			}
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
		}
	}
	
	private static void initOrgInfo(ResultSet rs, OrgInfo orgInfo) throws Exception{
		int size = rs.getMetaData().getColumnCount();
		for(int i = 1; i<= size; i++) {
			String column = rs.getMetaData().getColumnName(i);
			String methodName = "set" + column.substring(0, 1).toUpperCase() + column.substring(1).toLowerCase();
			Method method = OrgInfo.class.getMethod(methodName, new Class[]{String.class});
			method.invoke(orgInfo, rs.getString(column));
		}
	}
}
