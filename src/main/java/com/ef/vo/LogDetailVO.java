package com.ef.vo;

import com.ef.annotation.DirectiveHint;

public class LogDetailVO {
	
	//%...a:          Remote IP-address
	@DirectiveHint("a")
	private String remoteIpAddress;
	
	//%...A:          Local IP-address
	@DirectiveHint("A")
	private String localIpAddress;
	
	//%...B:          Bytes sent, excluding HTTP headers.
	@DirectiveHint("B")
	private String byteSent;
	
	//%...b:          Bytes sent, excluding HTTP headers. In CLF format
	//        i.e. a '-' rather than a 0 when no bytes are sent.
	@DirectiveHint("b")
	private String byteSentClf;
	
	//%...c:          Connection status when response was completed.
	//                'X' = connection aborted before the response completed.
	//                '+' = connection may be kept alive after the response is sent.
	//                '-' = connection will be closed after the response is sent.
	@DirectiveHint("c")
	private String connectionStatus;
	
	//%...{FOOBAR}e:  The contents of the environment variable FOOBAR
	@DirectiveHint("e")
	private String envVariable;
	
	//%...f:          Filename
	@DirectiveHint("f")
	private String filename;
	
	//%...h:          Remote host
	@DirectiveHint("h")
	private String remoteHost;
	
	//%...H       The request protocol
	@DirectiveHint("H")
	private String requestProtocol;
	
	//%...{Foobar}i:  The contents of Foobar: header line(s) in the request
	//                sent to the server.
	@DirectiveHint("i")
	private String headerVar;
	
	//%...l:          Remote logname (from identd, if supplied)
	@DirectiveHint("l")
	private String remoteLogname;
	
	//%...m       The request method
	@DirectiveHint("m")
	private String requestMethod;
	
	//%...{Foobar}n:  The contents of note "Foobar" from another module.
	@DirectiveHint("n")
	private String moduleNote;
	
	//%...{Foobar}o:  The contents of Foobar: header line(s) in the reply.
	@DirectiveHint("o")
	private String responseHeader;
	
	//%...p:          The canonical Port of the server serving the request
	@DirectiveHint("p")
	private String port;
	
	//%...P:          The process ID of the child that serviced the request.
	@DirectiveHint("P")
	private String processId;
	
	//%...q       The query string (prepended with a ? if a query string exists,
	//        otherwise an empty string)
	@DirectiveHint("q")
	private String queryString;
	
	//%...r:          First line of request
	@DirectiveHint("r")
	private String firstLineRequest;
	
	//%...s:          Status.  For requests that got internally redirected, this is
	//                the status of the *original* request --- %...>s for the last.
	@DirectiveHint("s")
	private String status;
	
	//%...t:          Time, in common log format time format (standard english format)
	//%...{format}t:  The time, in the form given by format, which should
	//                be in strftime(3) format. (potentially localized)
	@DirectiveHint("t")
	private String time;
	
	//%...T:          The time taken to serve the request, in seconds.
	@DirectiveHint("T")
	private String procTimeSec;
	
	//%...u:          Remote user (from auth; may be bogus if return status (%s) is 401)
	@DirectiveHint("u")
	private String user;
	
	//%...U:          The URL path requested, not including any query string.
	@DirectiveHint("U")
	private String url;
	
	//%...v:          The canonical ServerName of the server serving the request.
	@DirectiveHint("v")
	private String serverName;
	
	//%...V:          The server name according to the UseCanonicalName setting.
	@DirectiveHint("V")
	private String canonicalServerName;

	/**
	 * @return the remoteIpAddress
	 */
	public String getRemoteIpAddress() {
		return remoteIpAddress;
	}

	/**
	 * @return the localIpAddress
	 */
	public String getLocalIpAddress() {
		return localIpAddress;
	}

	/**
	 * @return the byteSent
	 */
	public String getByteSent() {
		return byteSent;
	}

	/**
	 * %...b: Bytes sent, excluding HTTP headers. In CLF format <br/>
	 * i.e. a '-' rather than a 0 when no bytes are sent.
	 * @return the byteSentClf
	 */
	public String getByteSentClf() {
		return byteSentClf;
	}

	/**
	 * @return the connectionStatus
	 */
	public String getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * @return the envVariable
	 */
	public String getEnvVariable() {
		return envVariable;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * %...h: Remote host
	 * 
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @return the requestProtocol
	 */
	public String getRequestProtocol() {
		return requestProtocol;
	}

	/**
	 * @return the headerVar
	 */
	public String getHeaderVar() {
		return headerVar;
	}

	/**
	 * %...l: Remote logname (from identd, if supplied)
	 * @return the remoteLogname
	 */
	public String getRemoteLogname() {
		return remoteLogname;
	}

	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @return the moduleNote
	 */
	public String getModuleNote() {
		return moduleNote;
	}

	/**
	 * @return the responseHeader
	 */
	public String getResponseHeader() {
		return responseHeader;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * %...r: First line of request
	 * @return the firstLineRequest
	 */
	public String getFirstLineRequest() {
		return firstLineRequest;
	}

	/**
	 * %...s: Status.  For requests that got internally redirected, this is <br/>
	 *               the status of the *original* request --- %...&gt;s for the last.
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * %...t: Time, in common log format time format (standard english format) <br/>
	 * %...{format}t: The time, in the form given by format, which should
	 *               be in strftime(3) format. (potentially localized)
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return the procTimeSec
	 */
	public String getProcTimeSec() {
		return procTimeSec;
	}

	/**
	 * %...u: Remote user (from auth; may be bogus if return status (%s) is 401)
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return the canonicalServerName
	 */
	public String getCanonicalServerName() {
		return canonicalServerName;
	}
	
	
}
