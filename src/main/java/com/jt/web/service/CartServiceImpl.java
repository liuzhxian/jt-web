package com.jt.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.po.Cart;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private HttpClientService httpClient;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public List<Cart> findCartByUserId(Long userId) {
		String url = "http://cart.jt.com/cart/query/"+userId;
		String resultJSON = httpClient.doGet(url);
		List<Cart> cartList = new ArrayList<>();
		try {
			
			SysResult sysResult = 
			objectMapper.readValue(resultJSON,SysResult.class);
			if(sysResult.getStatus() == 200){
				cartList = (List<Cart>) sysResult.getData();
			}else{
				System.out.println("后台查询数据失败:201");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			System.out.println("后台回传数据,前台解析报错"+e.getMessage());
		}
		return cartList;
	}
	
	
	@Override
	public void updateCartNum(Long userId, Long itemId, Integer num) {
		String url = 
	"http://cart.jt.com/cart/update/num/"+userId+"/"+itemId+"/" +num;
		httpClient.doGet(url);
	}


	@Override
	public void saveCart(Cart cart) {
		String url = "http://cart.jt.com/cart/save";
		//为了简化参数传递将cart对象转化为cartJSON
		String cartJSON = null;
		try {
			cartJSON = 
					objectMapper.writeValueAsString(cart);
		} catch (Exception e) {
			System.out.println("cart转化json异常"+e.getMessage());
		}
		Map<String,String> params = new HashMap<>();
		params.put("cartJSON",cartJSON);
		httpClient.doPost(url, params);
	}
	
	
	
	
	
	
	
	
}
