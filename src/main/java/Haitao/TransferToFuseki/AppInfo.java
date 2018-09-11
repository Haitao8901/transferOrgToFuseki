package Haitao.TransferToFuseki;

public class AppInfo {
	private String orgid;            
	private String id;                
	private String params;            
	private String uri;               
	private String description;       
	private String domain;            
	private String prefix;            
	private String iscommon;          
	private String term_prefix;       
	private String term_uri;
	
	public AppInfo(){}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getIscommon() {
		return iscommon;
	}

	public void setIscommon(String isCommon) {
		this.iscommon = isCommon;
	}

	public String getTerm_prefix() {
		return term_prefix;
	}

	public void setTerm_prefix(String term_prefix) {
		this.term_prefix = term_prefix;
	}

	public String getTerm_uri() {
		return term_uri;
	}

	public void setTerm_uri(String term_uri) {
		this.term_uri = term_uri;
	}
}
