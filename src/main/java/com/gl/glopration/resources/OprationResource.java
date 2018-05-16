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

import com.gl.glopration.model.OprationBean;
import com.gl.glopration.service.OprationService;
import com.gl.glopration.util.CustomErrorType;

@RestController
@RequestMapping("/oprations")
public class OprationResource {
	public static final Logger logger = LoggerFactory.getLogger(OprationResource.class);
	@Autowired
	OprationService oprationService;

	@RequestMapping(path = "/createOpration/", method = RequestMethod.POST)
	public ResponseEntity<?> createOpration(@RequestBody OprationBean bean, UriComponentsBuilder ucBuilder) {
		
		oprationService.save(bean);
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/getOprations/", method = RequestMethod.GET)
	public ResponseEntity<List<OprationBean>> getAllOprations() {
		List<OprationBean> oprataions = oprationService.findAll();
		if (oprataions.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<OprationBean>>(oprataions, HttpStatus.OK);
	}

	@RequestMapping(path = "/updateOpration/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOprations(@PathVariable("id") long id, @RequestBody OprationBean bean) {
		
		OprationBean currentOpration = oprationService.getOprationHashMap().get(id);
		if (currentOpration == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. opration with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		currentOpration.setOprationCode(bean.getOprationCode());
		currentOpration.setOprationDesciption(bean.getOprationDesciption());
		currentOpration.setPrice(bean.getPrice());
		currentOpration.setDLTCharge(bean.getDLTCharge());
		currentOpration.setWage(bean.getWage());
		oprationService.save(currentOpration);
		return new ResponseEntity<OprationBean>(currentOpration, HttpStatus.OK);
	}

	@RequestMapping(path = "/deleteOpration/{id}", method = RequestMethod.DELETE)
	public void removeOprations(@PathVariable("id") long id) {
		oprationService.delete(id);
	}

}
