package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.po.Item;
import com.jt.common.po.ItemDesc;
import com.jt.common.service.HttpClientService;
@Service	//前台Service
public class ItemServiceImpl implements ItemService {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private HttpClientService httpClient;
	
	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
		//获取后台返回的json数据
		String result = httpClient.doGet(url);
		Item item = null;
		try {
			item = objectMapper.readValue(result,Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = 
		"http://manage.jt.com/web/item/findItemDescById/"+itemId;
		//itemDesc的JSON串
		String result = httpClient.doGet(url);
		ItemDesc itemDesc = null;
		try {
			itemDesc = 
			objectMapper.readValue(result, ItemDesc.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
	
	
	
	
	
	
	
	
	
}
