package com.gl.glopration.test;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;

import com.gl.glopration.model.ReceiptCode;

public class TestOprations {
	
    public static final String REST_SERVICE_URI = "http://localhost:8080/GLOprations/oprations";
	public static void main(String[] args) {
		getOpration();
	}
	
	private static void getOpration() {
		RestTemplate restTemplate = new RestTemplate();
        List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/getOprations/", List.class);
        
        if(usersMap!=null){
            for(LinkedHashMap<String, Object> map : usersMap){
               System.out.println(map);
            }
        }else{
            System.out.println("No opration exist----------");
        }
	}
}
