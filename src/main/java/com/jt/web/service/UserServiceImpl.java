package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.po.User;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public void saveUser(User user) {
		String url = "http://sso.jt.com/user/register";
		
		Map<String,String> params = new HashMap<>();
		params.put("username", user.getUsername());
		String md5Pass = DigestUtils.md5Hex(user.getPassword());
		params.put("password", md5Pass);
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		String resultJSON = 
				httpClient.doPost(url, params);
		
		//判断后台入库操作是否成功
		try {
			SysResult sysResult = 
					objectMapper.readValue(resultJSON,SysResult.class);
			if(sysResult.getStatus() != 200){
				//抛出异常
				throw new RuntimeException();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
			
	}


	@Override
	public String findUserByUP(User user) {
		String token = null;
		String url = "http://sso.jt.com/user/login";
		String md5Pass = DigestUtils.md5Hex(user.getPassword());
		Map<String,String> params = new HashMap<>();
		params.put("username", user.getUsername());
		params.put("password", md5Pass);
		//请求sso单点登录系统的返回值结果.
		String resultJSON = httpClient.doPost(url, params);
		try {
			SysResult sysResult = 
					objectMapper.readValue(resultJSON,SysResult.class);
			
			if(sysResult.getStatus() == 200){
				//表示后台返回数据成功
				token = (String) sysResult.getData();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//调用异常处理机制
			throw new RuntimeException();
		}
		return token;
	}
	
	
	
	
	
	
	

}
