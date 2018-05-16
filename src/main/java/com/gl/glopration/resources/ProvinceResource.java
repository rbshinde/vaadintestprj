package com.gl.glopration.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.gl.glopration.model.ProvinceBean;
import com.gl.glopration.service.ProvinceService;
import com.gl.glopration.util.CustomErrorType;

@RestController
@RequestMapping("/provinces")
public class ProvinceResource {
	
	public static final Logger logger = LoggerFactory.getLogger(OprationResource.class);

	@Autowired
	ProvinceService provinceService;

	@RequestMapping(path = "/createProvince/", method = RequestMethod.POST)
	public ResponseEntity<?> createProvince(@RequestBody ProvinceBean bean, UriComponentsBuilder ucBuilder) {
		provinceService.save(bean);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/getProvinces/", method = RequestMethod.GET)
	public ResponseEntity<List<ProvinceBean>> getAllProvinces() {
		List<ProvinceBean> provinces = provinceService.findAll();
		if (provinces.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ProvinceBean>>(provinces, HttpStatus.OK);
	}

	@RequestMapping(path = "/updateProvince/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProvinces(@PathVariable("id") long id, @RequestBody ProvinceBean bean) {
		ProvinceBean currentProvinceBean = provinceService.getProvinceMap().get(id);
		
		if (currentProvinceBean == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. opration with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		currentProvinceBean.setDLTCharge(bean.getDLTCharge());
		currentProvinceBean.setWage(bean.getWage());
		provinceService.save(currentProvinceBean);
		return new ResponseEntity<ProvinceBean>(currentProvinceBean, HttpStatus.OK);
	}

	@RequestMapping(path = "/deleteProvince/{id}", method = RequestMethod.DELETE)
	public void removeProvince(@PathVariable("id") long id) {
		provinceService.delete(id);
	}

}
