package Haitao.TransferToFuseki;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Log {
	private static Logger logger = Logger.getLogger("transfer");
	static{
		BasicConfigurator.configure();
	}
	
	public static void info(String message){
		logger.info(message);
	}
	
	public static void error(Exception e){
		logger.error(e);
	}
}
