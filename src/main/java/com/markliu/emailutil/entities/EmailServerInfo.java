package com.markliu.emailutil.entities;

/**
 * 登陆的邮箱服务器的信息，包括服务器的 host 和 ip，用户名和密码等
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 4:14:50 PM
 */
public class EmailServerInfo {
	/**
	 * 发送邮件的服务器的IP地址
	 */
	private String mailServerHost;
	/**
	 * 邮件服务器的端口号
	 */
	private String mailServerPort;

	/**
	 * 登陆的邮箱
	 */
	private String myEmailAddress;

	/**
	 * 登陆邮件发送服务器的用户名和密码
	 */
	private String userName;
	private String password;

	/**
	 * 是否需要身份验证，默认为true
	 */
	private boolean validate = true;

	/**
	 * 是否支持ssl链接
	 */
	private boolean ssl = true;

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public String getMyEmailAddress() {
		return myEmailAddress;
	}

	public void setMyEmailAddress(String myEmailAddress) {
		this.myEmailAddress = myEmailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

}
