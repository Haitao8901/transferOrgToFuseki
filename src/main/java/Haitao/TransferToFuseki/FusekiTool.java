package Haitao.TransferToFuseki;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class FusekiTool {
	private static final String FUSEKI_DATASET = "apps";
	private static final String FUSEKI_UPDATE = "update";
	private static final String NAME_LINE = "http://www.$org.com$nameSufix";
	private static final String TERM_LINE = "http://www.$org.com$termSufix";
	private static final String GRAPH = "http://www.$org.com$app/profile";
	
	private static final String TERM_PREFIX = "http://www.cyberobject.com/2012/12/term#";
	private static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String RDFS_PREFIX = "http://www.w3.org/2000/01/rdf-schema#";
	private static final String XSD_PREFIX = "http://www.w3.org/2001/XMLSchema#";
	private static Map<String, String> ORGreflectMap = new HashMap<String, String>();
	private static Map<String, String> APPreflectMap = new HashMap<String, String>();
	private static HttpClient httpClient = new HttpClient();
	
	private static VelocityEngine ve;
	
	static{
		
		ORGreflectMap.put("id",  "");
		ORGreflectMap.put("name", "");
		ORGreflectMap.put("address", "");
		ORGreflectMap.put("creator", "");
		ORGreflectMap.put("contactor", "");
		ORGreflectMap.put("createdate", "");
		ORGreflectMap.put("country", "");
		ORGreflectMap.put("phone", "");
		ORGreflectMap.put("orgtype", "");
		ORGreflectMap.put("zip", "");
		ORGreflectMap.put("www", "");
		ORGreflectMap.put("city", "");
		ORGreflectMap.put("domain", "");
		ORGreflectMap.put("state", "");
		
		
		APPreflectMap.put("description", "");
		APPreflectMap.put("id", "id");
		APPreflectMap.put("iscommon", "");
		APPreflectMap.put("domain", "");
		APPreflectMap.put("orgid", "");
		APPreflectMap.put("params", "");
		
		try {
			ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
			ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			ve.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveAppInfoToFuseki(List<AppInfo> appInfoList) throws Exception{
		Log.info(appInfoList.size() + " app info need to transfer.");
		for(AppInfo app: appInfoList){
			VelocityContext context = new VelocityContext();
			Map<String, String> prefixMap = new HashMap<String, String>();
			prefixMap.put("rdf", RDF_PREFIX);
			prefixMap.put("rdfs", RDFS_PREFIX);
			prefixMap.put("xsd", XSD_PREFIX);
			prefixMap.put("term", TERM_PREFIX);
			
			String orgId = app.getOrgid();
			String appId = app.getId();
			Log.info("ORGID: " + orgId + " APPID: " + appId);
			context.put("prefixMap", prefixMap);
			//graph value
			context.put("graph", GRAPH.replace("$org", orgId).replace("$app", "/"+appId));
			context.put("appUri", NAME_LINE.replace("$nameSufix", "/" + appId).replace("$org", orgId));
//			context.put("appValue", appId);
//			context.put("appPrefix", orgId + "_" + appId);
//			context.put("orgUri", NAME_LINE.replace("$nameSufix", "").replace("$org", orgId));
//			context.put("appTermUri", TERM_LINE.replace("$termSufix", "/" + appId + "/term").replace("$org", orgId));
//			context.put("appTermPrefix", orgId + "_" + appId + "_term");
			
//			context.put("appUri", app.getUri());
			context.put("appValue", appId);
			context.put("appPrefix", app.getPrefix());
			context.put("orgUri", NAME_LINE.replace("$nameSufix", "").replace("$org", orgId));
			
			context.put("appDburi", app.getUri());
			context.put("appDbtermuri", app.getTerm_uri());
			
			context.put("appTermUri", (String) context.get("appUri") + "/term");
			
			context.put("appTermPrefix", app.getTerm_prefix());
			
			Map<String, List<String>> profileMap = new HashMap<String, List<String>>();
			String prefix = (String) context.get("appUri");
			if(!prefix.endsWith("#")){
				prefix += "#";
			}
			Iterator<String> itr = APPreflectMap.keySet().iterator();
			for(;itr.hasNext();) {
				String key = itr.next();
				String term = key.toUpperCase();
				
				String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
				Method method = AppInfo.class.getMethod(methodName, new Class[0]);
				String termValue = (String) method.invoke(app);
				
				String profileUri = prefix.concat(key);
				String profileType = TERM_PREFIX.concat(term);
				List<String> list = new ArrayList<String>();
				list.add(profileType);
				list.add(termValue);
				profileMap.put(profileUri, list);
			} 
			
			context.put("profileMap", profileMap);
//			InputStream ins = ClassUtils.getResourceAsStream( ve.getProperty("class.resource.loader.class").getClass(), "Haitao/template/sparql_saveAppInfo.vm" );
//			if(ins != null)
//			System.out.println(IOUtils.toString(ins));
			StringWriter writer = new StringWriter();
			ve.mergeTemplate("Haitao/template/sparql_saveAppInfo.vm", "utf-8", context, writer);
			invokeFuseki(writer.toString());
		}
	}
	
	public static void saveOrgInfoToFuseki(List<OrgInfo> OrgInfoList) throws Exception{
		Log.info( OrgInfoList.size() + " org info need to transfer to fuseki.");
		for(OrgInfo org: OrgInfoList){
			VelocityContext context = new VelocityContext();
			Map<String, String> prefixMap = new HashMap<String, String>();
			prefixMap.put("rdf", RDF_PREFIX);
			prefixMap.put("rdfs", RDFS_PREFIX);
			prefixMap.put("xsd", XSD_PREFIX);
			prefixMap.put("term", TERM_PREFIX);
			
			String orgId = org.getId();
			Log.info("ORGID: " + orgId);
			context.put("prefixMap", prefixMap);
			//graph value
			context.put("graph", GRAPH.replace("$org", orgId).replace("$app", ""));
//			context.put("orgUri", org.getUri());
			context.put("orgUri", NAME_LINE.replace("$nameSufix", "").replace("$org", orgId));
			context.put("orgValue", orgId);
			context.put("orgDburi", org.getUri());
			context.put("orgDbtermuri", org.getTerm_uri());
			context.put("orgTermUri", (String) context.get("orgUri") +"/term");
			context.put("orgTermPrefix", org.getTerm_prefix());
			
			Map<String, List<String>> profileMap = new HashMap<String, List<String>>();
			String prefix = (String) context.get("orgUri");
			if(!prefix.endsWith("#")){
				prefix += "#";
			}
			Iterator<String> itr = ORGreflectMap.keySet().iterator();
			for(;itr.hasNext();) {
				String key = itr.next();
				String term = key.toUpperCase();
				
				String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
				Method method = OrgInfo.class.getMethod(methodName, new Class[0]);
				String termValue = (String) method.invoke(org);
				
				String profileUri = prefix.concat(key);
				String profileType = TERM_PREFIX.concat(term);
				List<String> list = new ArrayList<String>();
				list.add(profileType);
				list.add(termValue);
				profileMap.put(profileUri, list);
				
			}
			context.put("profileMap", profileMap);
			
			StringWriter writer = new StringWriter();
			ve.mergeTemplate("Haitao/template/sparql_saveOrgInfo.vm", "utf-8", context, writer);
			invokeFuseki(writer.toString());
			createDataset(orgId);
		}
	}
	
	/**
	 * Generate the prefix and graph info for each sparql
	 * */
	
	
	
	private static String invokeFuseki(String sparqlContent){
		PostMethod postmethod = null;
		String fusekiUrl = Constants.FUSEKI_URL+ "/" + FUSEKI_DATASET + "/" + FUSEKI_UPDATE;
		String parameter = FUSEKI_UPDATE;
		try{
			postmethod = new PostMethod(fusekiUrl);
			postmethod.addRequestHeader("Accept", "application/sparql-results+json,*/*;q=0.9");
			
			NameValuePair[] data = new NameValuePair[1];
			data[0] = new NameValuePair(parameter, sparqlContent);
			postmethod.setRequestBody(data);
			int rstStatus = httpClient.executeMethod(postmethod);
			if (rstStatus != 200) {
				postmethod.releaseConnection();
				Log.info("Invoke fuseki error. sparql is: " + sparqlContent);
				return null;
			}
			String response = postmethod.getResponseBodyAsString();
			return response;
		}catch(Exception e){
			Log.info("Invoke fuseki error. sparql is: " + sparqlContent);
			Log.error(e);
			e.printStackTrace();
		}finally{
			if(postmethod != null){
				postmethod.releaseConnection();
				postmethod = null;
			}
		}
		return null;
	}
	
	public static boolean createDataset(String name){
		Log.info("FusekiTool.createDataset, datasetName:" + name);
		PostMethod postmethod = null;
		try {
			postmethod = new PostMethod(Constants.FUSEKI_URL + "/$/datasets");
			NameValuePair[] data = new NameValuePair[2];
			data[0] = new NameValuePair("dbName", name);
			data[1] = new NameValuePair("dbType", "tdb");
			postmethod.setRequestBody(data);
			int rstStatus = httpClient.executeMethod(postmethod);
			if (rstStatus != 200) {
				Log.info("Response status "+ rstStatus);
				postmethod.releaseConnection();
				Log.info("Invoke fuseki failed");
				return false;
			}
			String resp = postmethod.getResponseBodyAsString();
			if (resp.contains("Error 409: Name already registered")){
				Log.info("already registered ");
				return true;
			} else if (resp.trim().equals("")){
				Log.info("create dataset");
				return true;
			}
			Log.info("FusekiTool.createDataset, Response :" + resp);
		} catch (Exception e){
			Log.error(e);
		}finally{
			if(postmethod != null){
				postmethod.releaseConnection();
				postmethod = null;
			}
		}
		return false;
	}

	public static boolean removeDataset(String name){
		Log.info("FusekiTool.removeDataset, datasetName:" + name);
		DeleteMethod deleteMethod = null;
		try {
			deleteMethod = new DeleteMethod(Constants.FUSEKI_URL + "/$/datasets/" + name);
			int rstStatus = httpClient.executeMethod(deleteMethod);
			if (rstStatus != 200) {
				Log.info("Response status "+ rstStatus);
				deleteMethod.releaseConnection();
				Log.info("Invoke fuseki failed");
				return false;
			}
			return true;
		} catch (Exception e){
			Log.error(e);
		}finally{
			if(deleteMethod != null){
				deleteMethod.releaseConnection();
				deleteMethod = null;
			}
		}
		return false;
	}
}
