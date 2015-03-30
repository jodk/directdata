package eql.db;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Map;


public class DB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driverClass;
	private String url;
	private String username;
	/**
	 * TODO : 密码应该需要加密，保证安全性
	 */
	private String password;
	

	public String getDriverClass() {
		if(driverClass ==null && url !=null){//默认查找一个驱动
            for(Map.Entry<String,String> entry : Vendor.vendorMap.entrySet()){
                if(url.indexOf(entry.getKey()) !=-1){
                    driverClass = entry.getValue();
                    break;
                }
            }
		}
		return driverClass;
	}

	public String getVendor() {
		this.driverClass = getDriverClass();
		if (driverClass != null) {
            for(Map.Entry<String,String> entry : Vendor.vendorMap.entrySet()){
                if(driverClass.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
		}
		return null;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		if(StringUtils.isEmpty(this.password)){
			this.password = null;
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKey() {
		return driverClass + "," + url;
	}
	public static void main(String[] args) {
	}
}
