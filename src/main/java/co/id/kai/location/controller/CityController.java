package co.id.kai.location.controller;

import co.id.kai.lib.PageImplBean;
import co.id.kai.lib.location.City;
import co.id.kai.lib.location.Province;
import co.id.kai.lib.responses.ResponseResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import co.id.kai.location.repository.CityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

/**
 *
 * @author Irpan Budiana
 */

@RestController
@RequestMapping("/city")
public class CityController {
@Autowired
CityRepository repo;
final ObjectMapper mapper = new ObjectMapper();
private final Logger log = LoggerFactory.getLogger(this.getClass());
List<Province> listprovince;
    @RequestMapping(method = RequestMethod.POST,headers = "Accept=application/json")
    public @ResponseBody ResponseResource service(@RequestBody @Valid City city, @PageableDefault(page=0, value = Integer.MAX_VALUE) Pageable page) throws IOException, ParseException {
        log.info("Masuk Service City");
        switch (city.getAction()) {
        case "insert_city":
            log.info("Insert Service City");
            return insertCity(city);
        case "get_city":
            log.info("Get Service City");
            return getCity(city, page);
        case "get_citybyid":
            log.info("Get Service City");
            return getCityById(city, page);
        case "update_city":
            log.info("Update Service City");
            return updateCity(city);
        case "delete_city":
            log.info("Delete Service City");
            return deleteCity(city);
        default:
            log.info("1:Service Not Found");
            return new ResponseResource("1", "Service Not Found");
        }
    
    }
    
    private ResponseResource insertCity(City city) throws ParseException{
       
            city.setModifiedon(HomeController.getCurdate());
            log.info(city.toString());
            city  = repo.save(city);
            if(!city.getId().isEmpty()){
                ResponseResource response =  new ResponseResource("0", "Insert Success");
                response.setCity(city);
                return response;
            }else{
                log.info("2:Insert Failed");
                return new ResponseResource("2", "Insert Failed");
            }
        
    }
    
    private ResponseResource getCity(City  data, Pageable page) throws JsonProcessingException, IOException{
        log.info("get Datanya:"+mapper.writeValueAsString(data));
        @SuppressWarnings("unchecked")
        PageImplBean<City> lcity = ((PageImplBean<City>)new ObjectMapper().readValue(mapper.writeValueAsString(
                repo.findByStatusAndNameContainingIgnoreCase(data.getStatus(), data.getName(), page)), new TypeReference<PageImplBean<City>>() {}));
        
        if (lcity.getTotalElements()>0){
            log.info("0:Data Found");
            listprovince = new HomeController().getProvinces();
            if(listprovince!=null){
                lcity.getContent().stream().forEach((content) -> {
                    content.setProvince(listprovince.stream().filter(p -> p.getId().equals(content.getProvinceid())).collect(Collectors.toList()).get(0));
                });
                listprovince.clear();
            }
            ResponseResource res =  new ResponseResource("0", "Get Data Success");
            res.setPagecity(lcity);
            return res;
        } else {
            log.info("1:Data Not Found");
            return new ResponseResource("1", "Data Not Found");
        }
           
        
    }

    private ResponseResource getCityById(City data, Pageable page) throws IOException {
        log.info("get Datanya:" + mapper.writeValueAsString(data));
        @SuppressWarnings("unchecked")        
        PageImplBean<City> lcity = ((PageImplBean<City>)new ObjectMapper().readValue(mapper.writeValueAsString(
                repo.findByStatusAndId(data.getStatus(), data.getId(), page)), new TypeReference<PageImplBean<City>>() {}));
        
        if(lcity.getTotalElements()>0){
            log.info("0:Data Found");
            ResponseResource res =  new ResponseResource("0", "Get Data Success");
            res.setPagecity(lcity);
            return res;
        } else {
            log.info("1:Data Not Found");
            return new ResponseResource("1", "Data Not Found");
        }
    }
        
    private ResponseResource updateCity(City data) throws IOException, ParseException{
     
        City update = repo.findOne(data.getId());
        if (update != null) {
            update.setGmt(data.getGmt());
            update.setProvinceid(data.getProvinceid());
            update.setName(data.getName());
            update.setStatus(data.getStatus());
            update.setModifiedby(data.getModifiedby());
            update.setModifiedon(HomeController.getCurdate());
            //update.setCdaoplastupdate(daop.getCdaoplastupdate());
            update = repo.save(update);
            if(!update.getId().isEmpty()){
                log.info("0:Update Success");
                ResponseResource response = new ResponseResource("0", "Update Data Success");
                response.setCity(update);
                return response;
            } else {
                log.info("2:Update Failed");
                return new ResponseResource("2", "Update Failed");
            }
        } else {
            log.info("1:Data Not Found");
            return new ResponseResource("1", "Data Not Found");
        }
    }
    
    private ResponseResource deleteCity(City data) throws IOException, ParseException{
        
        City update = repo.findOne(data.getId());
        if (update != null) {
            update.setStatus("1");
            update.setModifiedby(data.getModifiedby());
            update.setModifiedon(HomeController.getCurdate());
            //update.setCdaoplastupdate(daop.getCdaoplastupdate());
            update = repo.save(update);
            if(!update.getId().isEmpty()){
                log.info("0:Delete Success");
                ResponseResource response = new ResponseResource("0", "Delete Data Success");
                response.setCity(update);
                return response;
            } else {
                log.info("2:Delete Failed");
                return new ResponseResource("2", "Delete Failed");
            }
        } else {
            log.info("1:Data Not Found");
            return new ResponseResource("1", "Data Not Found");
        }
    }
}