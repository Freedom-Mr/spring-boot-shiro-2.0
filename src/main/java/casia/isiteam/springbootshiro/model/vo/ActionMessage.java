package casia.isiteam.springbootshiro.model.vo;

/**
 * 行动性线索
 * 
 * @author admin
 *
 */
public class ActionMessage {
	private String uid;
	private String wid;
	private int msgType; // 0 q 1 v
	private String pubtime;
	private String gid;// 群聊为群id 私聊为接收者id
	private String content;
	private int chatType; // 0 群聊 1 私聊

	public String getPubtime() {
		return pubtime;
	}

	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

}
