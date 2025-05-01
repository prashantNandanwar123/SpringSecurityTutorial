package com.security.payload;

public class Response {
	
	private int respCode;
	private String respMsg;
	private Object respData;
	
	
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public Object getRespData() {
		return respData;
	}
	public void setRespData(Object respData) {
		this.respData = respData;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	@Override
	public String toString() {
		return "Response [respCode=" + respCode + ", respMsg=" + respMsg + ", respData=" + respData + "]";
	}

}
