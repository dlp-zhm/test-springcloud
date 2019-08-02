package com.tedu.sp11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.tedu.web.util.JsonResult;

/**
 * 
 * 访问商品失败时的降级类.
 * @author tedu
 *
 */
@Component
public class ItemServiceFallback implements FallbackProvider {

	/**1.该方法是指定应用此降级类的服务id,星号或null都代表所有服务*/
	@Override
	public String getRoute() {
		return "item-service"; //星号和null都表示所有微服务失败都应用当前降级类
	}

	/**2.该方法返回封装降级响应给客户端的对象,返回值ClientHttpResponse中封装降级响应*/
	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return response();
	}

	/**2.里面的方法-ClientHttpResponse中封装降级响应*/
	private ClientHttpResponse response() {
		
		//ClientHttpResponse是一个接口,所以返回一个匿名内部类对象
		return new ClientHttpResponse() {
			
			/**下面三个方法都是协议号*/
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.OK;//返回http状态
			}
			@Override
			public int getRawStatusCode() throws IOException {
				 return HttpStatus.OK.value();//返回状态值
			}
			@Override
			public String getStatusText() throws IOException {
				return HttpStatus.OK.getReasonPhrase();//返回文本
			}
			
			
			
			@Override
			public HttpHeaders getHeaders() {//返回http头
				HttpHeaders header = new HttpHeaders();
				header.setContentType(MediaType.APPLICATION_JSON);
				return header;
			}
			
			@Override
			public InputStream getBody() throws IOException {
				System.out.println("fallback body");
				String s = JsonResult.err().msg("后台服务错误").toString();
				return new ByteArrayInputStream(s.getBytes("utf-8"));
			}
			
			@Override
			public void close() {}
		};
	}

}


