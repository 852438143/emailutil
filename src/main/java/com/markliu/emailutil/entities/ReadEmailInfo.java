package com.markliu.emailutil.entities;

import java.util.Arrays;
import java.util.Date;

/**
 * 读取邮件的POJO
 * 
 * @auther SunnyMarkLiu
 * @time Apr 12, 2016 10:20:32 PM
 */
public class ReadEmailInfo extends EmailInfo {

	private String fromAddress;
	
	/**
	 * 发送的时间
	 */
	private Date sentDate;

	/**
	 * 是否需要邮件回执
	 */
	private boolean needReply;

	/**
	 * 是否已读
	 */
	private boolean isReaded;
	
	/**
	 * 是否包含附件
	 */
	private boolean containsAttachments = false;
	
	/**
	 * 抄送
	 */
	private String[] carbonCopy;
	
	/**
	 * 	暗抄送
	 */
	private String[] darkCopy;
	
	/**
	 * 此邮件的Message-ID
	 */
	private String messageID;
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String[] getDarkCopy() {
		return darkCopy;
	}

	public void setDarkCopy(String[] darkCopy) {
		this.darkCopy = darkCopy;
	}

	public String[] getCarbonCopy() {
		return carbonCopy;
	}

	public void setCarbonCopy(String[] carbonCopy) {
		this.carbonCopy = carbonCopy;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public boolean isNeedReply() {
		return needReply;
	}

	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public boolean isContainsAttachments() {
		return containsAttachments;
	}

	public void setContainsAttachments(boolean containsAttachments) {
		this.containsAttachments = containsAttachments;
	}

	@Override
	public String toString() {
		return super.toString() + "\n[ReadEmailInfo {\n\tfromAddress=" + fromAddress + ", \n\tsentDate="
				+ sentDate + ", \n\tneedReply=" + needReply + ", \n\tisReaded="
				+ isReaded + ", \n\tcontainsAttachments=" + containsAttachments
				+ ", \n\tcarbonCopy=" + Arrays.toString(carbonCopy) + ", \n\tdarkCopy="
				+ Arrays.toString(darkCopy) + ", \n\tmessageID=" + messageID + "\n]}";
	}

	
	
}
