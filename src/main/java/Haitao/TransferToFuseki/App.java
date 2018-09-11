package Haitao.TransferToFuseki;

public class App 
{
    public static void main( String[] args ) throws Exception{
    	if(args.length > 3 || args.length < 1){
    		System.out.println("Input format is: db [orgid] [appid].");
    		return;
    	}
    	
    	try{
    		int type = Integer.valueOf(args[0]);
    		if(type != 13 && type != 196){
    			System.out.println("Only integer 13 or 196 are allowed for db!");
    			return;
    		}
    		if(args.length == 1){
    			Constants.init(type, null, null);
    		}else if(args.length == 2){
    			Constants.init(type, args[1], null);
    		}else if(args.length == 3){
    			Constants.init(type, args[1], args[2]);
    		}
    		if(Constants.TRANSFERAPP){
    			Log.info("======transfer app=======");
    			TransferApp.transferApp();
    			Log.info("======transfer app end=======");
    		}
    		if(Constants.TRANSFERORG){
    			Log.info("======transfer org=======");
    			TransferOrg.transferOrg();
    			Log.info("======transfer org end=======");
    		}else{
    			Log.info("======No need to transfer org=======");
    		}
        	Log.info("All the app and org have been transfered to fuseki.");
    	}catch(Exception e){
    		Log.error(e);
    		System.out.println("Only integer 13 or 196 are allowed fro db!");
    	}
    	
    }
}
