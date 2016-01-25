package co.id.kai.location.controller;

import co.id.kai.lib.PageImplBean;
import co.id.kai.lib.location.Province;
import co.id.kai.lib.responses.ResponseResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.ParseException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import co.id.kai.location.repository.ProvinceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

/**
 *
 * @author Irpan Budiana
 */
@RestController
@RequestMapping("/province")
public class ProvinceController {
    
    @Autowired
    ProvinceRepository repo;
    final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody
    ResponseResource service(@RequestBody @Valid Province province, @PageableDefault(page=0, value = Integer.MAX_VALUE) Pageable page) throws IOException, ParseException {
        log.info("Masuk Service Province");
        switch (province.getAction()) {
            case "insert_province":
                log.info("Insert Service Province");
                return insertProvince(province);
            case "get_province":
                log.info("Get Service Province");
                return getProvince(province, page);
            case "get_provincebyid":
                log.info("Get Service Province By Id");
                return getProvinceById(province, page);
            case "update_province":
                log.info("Update Service Province");
                return updateProvince(province);
            case "delete_province":
                log.info("Delete Service Province");
                return deleteProvince(province);
            default:
                log.info("1:Service Not Found");
                return new ResponseResource("1", "Service Not Found");
        }
    }

    private ResponseResource insertProvince(Province province) throws ParseException {
        province.setModifiedon(HomeController.getCurdate());
        log.info(province.toString());
        province = repo.save(province);
        if (!province.getId().isEmpty()) {
            ResponseResource response = new ResponseResource("0", "Insert Success");
            response.setProvince(province);
            return response;
        } else {
            log.info("2:Insert Failed");
            return new ResponseResource("2", "Insert Failed");
        }

    }

    private ResponseResource getProvince(Province data, Pageable page) throws JsonProcessingException, IOException {
        log.info("get Datanya:"+mapper.writeValueAsString(data));
        @SuppressWarnings("unchecked")
        PageImplBean<Province> lprovince = ((PageImplBean<Province>)new ObjectMapper().readValue(mapper.writeValueAsString(
                repo.findByStatusAndNameContainingIgnoreCase(data.getStatus(), data.getName(), page)), new TypeReference<PageImplBean<Province>>() {}));
                
         if(lprovince.getTotalElements()>0){
             log.info("0:Data Found");
             ResponseResource res =  new ResponseResource("0", "Get Data Success");
             res.setPageprovince(lprovince);
             return res;
        }else{
             log.info("1:Data Not Found");
                return new ResponseResource("1", "Data Not Found");
        }
    }
   
    private ResponseResource getProvinceById(Province data, Pageable page) throws IOException {
        log.info("get Datanya:"+mapper.writeValueAsString(data));
        @SuppressWarnings("unchecked")        
        PageImplBean<Province> lprovince = ((PageImplBean<Province>)new ObjectMapper().readValue(mapper.writeValueAsString(
                repo.findByStatusAndId(data.getStatus(), data.getId(), page)), new TypeReference<PageImplBean<Province>>() {}));
                        

        if(lprovince.getTotalElements()>0){
             log.info("0:Data Found");
             ResponseResource res =  new ResponseResource("0", "Get Data Success");
             res.setPageprovince(lprovince);
             return res;
         }else{
             log.info("1:Data Not Found");
                return new ResponseResource("1", "Data Not Found");
         }
    }
    
    private ResponseResource updateProvince(Province data) throws ParseException, IOException {
        Province update = repo.findOne(data.getId());
        if (update != null) {
            update.setName(data.getName());
            update.setStatus(data.getStatus());
            update.setModifiedby(data.getModifiedby());
            update.setModifiedon(HomeController.getCurdate());
            //update.setCdaoplastupdate(daop.getCdaoplastupdate());
            update = repo.save(update);
            if (!update.getId().isEmpty()) {
                log.info("0:Update Success");
                ResponseResource response = new ResponseResource("0", "Update Data Success");
                response.setProvince(update);
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

    private ResponseResource deleteProvince(Province data) throws ParseException, IOException {
        Province update = repo.findOne(data.getId());
        if (update != null) {
            update.setStatus("1");
            update.setModifiedby(data.getModifiedby());
            update.setModifiedon(HomeController.getCurdate());
            //update.setCdaoplastupdate(daop.getCdaoplastupdate());
            update = repo.save(update);
            if (!update.getId().isEmpty()) {
                log.info("0:Delete Success");
                ResponseResource response = new ResponseResource("0", "Delete Data Success");
                response.setProvince(update);
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
