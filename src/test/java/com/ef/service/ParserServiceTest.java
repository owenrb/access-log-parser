package com.ef.service;

import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.ef.vo.LogDetailVO;
import com.ef.vo.RegexVO;

@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

	private ParserService service = new ParserService();
	
	@Mock
	private Map<String, String> dir2regex;
	
	@Before 
	public void setup() {
		Mockito.when(dir2regex.get("t")).thenReturn("\\[(.+)\\]");
		
		service.setDir2regex(dir2regex);
	}
	
	@Test
	public void computeRegEx() {

		service.setLogformat("%h %l %u %t \\\"%r\\\" %>s %b");
		RegexVO actual = service.computeRegEx();
		
		String logline = "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326";  
		
		Matcher match = service.findMatch(actual.getRegex(), logline);
		
		Assert.assertNotNull(match);
		Assert.assertEquals("127.0.0.1", match.group(1));
		Assert.assertEquals("-", match.group(2));
		Assert.assertEquals("frank", match.group(3));
		Assert.assertEquals("10/Oct/2000:13:55:36 -0700", match.group(4));
		Assert.assertEquals("GET /apache_pb.gif HTTP/1.0", match.group(5));
		Assert.assertEquals("200", match.group(6));
		Assert.assertEquals("2326", match.group(7));
	}
	
	@Test
	public void computeRegExPipe() {
	
		service.setSeparator("|");
		service.setLogformat("%h|%l|%u|%t|\\\"%r\\\"|%>s|%b");
		RegexVO actual = service.computeRegEx();
		
		
		String logline = "127.0.0.1|-|frank|[10/Oct/2000:13:55:36 -0700]|\"GET /apache_pb.gif HTTP/1.0\"|200|2326";  
		
		Matcher match = service.findMatch(actual.getRegex(), logline);
		
		Assert.assertNotNull(match);
		Assert.assertEquals("127.0.0.1", match.group(1));
		Assert.assertEquals("-", match.group(2));
		Assert.assertEquals("frank", match.group(3));
		Assert.assertEquals("10/Oct/2000:13:55:36 -0700", match.group(4));
		Assert.assertEquals("GET /apache_pb.gif HTTP/1.0", match.group(5));
		Assert.assertEquals("200", match.group(6));
		Assert.assertEquals("2326", match.group(7));
	}
	
	@Test	
	public void mapToPojo() throws ServiceException {

		service.setLogformat("%h %l %u %t \\\"%r\\\" %>s %b");
		RegexVO actual = service.computeRegEx();
		
		String logline = "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326";  
		
		Matcher match = service.findMatch(actual.getRegex(), logline);
		Assert.assertNotNull(match);
		
		LogDetailVO pojo = service.mapToPojo(actual, match, LogDetailVO.class);
		Assert.assertNotNull(pojo);
		
		Assert.assertEquals("127.0.0.1", pojo.getRemoteHost());
		Assert.assertEquals("-", pojo.getRemoteLogname());
		Assert.assertEquals("frank", pojo.getUser());
		Assert.assertEquals("10/Oct/2000:13:55:36 -0700", pojo.getTime());
		Assert.assertEquals("GET /apache_pb.gif HTTP/1.0", pojo.getFirstLineRequest());
		Assert.assertEquals("200", pojo.getStatus());
		Assert.assertEquals("2326", pojo.getByteSentClf());
		
		
	}
}
