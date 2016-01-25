/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.id.kai.location.controller;

import co.id.kai.lib.location.Province;
import co.id.kai.lib.responses.ResponseResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 *
 * @author Irpan Budiana
 */
@RestController
@RequestMapping("/")
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    final Properties properties = new Properties();
//    properties.load(this.
//            getClass().getResourceAsStream("project.properties"));
//    System.out.println(properties.getProperty("version"));
//    System.out.println(properties.getProperty("artifactId"));
    
    @RequestMapping(method=RequestMethod.GET,headers = "Accept=application/json")
    public String home() throws JsonProcessingException, IOException {
        log.info("Home Location");
        System.err.println("Home Location");
        //getProvinces();
        String reshome;
        reshome = "info: Service Location! version 0.0.1 \n";
        reshome += "province: insert_province, get_province, get_provincebyid, update_province, delete_province\n";
        reshome += "city: insert_city, get_city, get_citybyid, update_city, delete_city\n";
        return reshome;
    }

    public static Date getCurdate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.parse(dateFormat.format(date));
    }
public List<Province> getProvinces() throws JsonProcessingException{
        final ObjectMapper mapper = new ObjectMapper();
        RestTemplate rest = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("action", "get_province");
        requestBody.put("name", "");
        requestBody.put("status","1");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        log.info("datapost:"+mapper.writeValueAsString(requestBody));
        //Creating http entity object with request body and headers
        HttpEntity<String> httpEntity =
        new HttpEntity<>(mapper.writeValueAsString(requestBody), requestHeaders);

        //Invoking the API
        ResponseResource apiResponse = 
                rest.postForObject("http://localhost:8084/sample-location/province", httpEntity, ResponseResource.class);
        log.info("data province:"+mapper.writeValueAsString(apiResponse));
        
        if(apiResponse.getErr_code().equals("0")){
            if(apiResponse.getPageprovince().getTotalElements()>0){
                return apiResponse.getPageprovince().getContent();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
