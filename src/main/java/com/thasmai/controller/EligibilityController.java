package com.thasmai.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thasmai.jpa.entities.Eligibility;
import com.thasmai.repository.EligibilityRepository;

import in.prvak.common.annotations.RestApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestApiController
@RequestMapping("/v1/eligibility/")
@Api("Set of endpoints for Creating, Retrieving, Updating and Deleting of Eligibilitys.")
public class EligibilityController {
	@Autowired
	EligibilityRepository eligibilityRepository;

	@GetMapping()
	@ApiOperation("${eligibilityController.getEligibilitys}")
	public List<Eligibility> getEligibilitys() {
		List<Eligibility> eligibilitysList = eligibilityRepository.findBy();
		return eligibilitysList;
	}

	@GetMapping("{id}")
	@ApiOperation("${eligibilityController.getEligibilityById}")
	public Optional<Eligibility> getEligibilityById(
			@ApiParam("Id of the eligibility to be obtained. Cannot be empty.") @PathVariable String id) {
		Optional<Eligibility> eligibility = eligibilityRepository.findById(Long.parseLong(id));
		return eligibility;
	}

	@PutMapping("{id}")
	@ApiOperation("${eligibilityController.updateEligibilityById}")
	public Optional<Eligibility> updateEligibilityById(
			@ApiParam("Modified eligibility information. Cannot be empty.") @RequestBody Eligibility newEmployee,
			@ApiParam("Id of the eligibility to be updated. Cannot be empty.") @PathVariable String id) {
		Optional<Eligibility> optionalEmp = eligibilityRepository.findById(Long.parseLong(id));
		if (optionalEmp.isPresent()) {
			Eligibility eligibility = optionalEmp.get();
			eligibility.setFirstName(newEmployee.getFirstName());
			eligibility.setLastName(newEmployee.getLastName());
			eligibility.setEmail(newEmployee.getEmail());
			eligibilityRepository.save(eligibility);
		}
		return optionalEmp;
	}

	@DeleteMapping(value = "{id}", produces = "application/json; charset=utf-8")
	@ApiOperation("${eligibilityController.deleteEligibilityById}")
	public String deleteEligibilityById(
			@ApiParam("Id of the eligibility to be deleted. Cannot be empty.") @PathVariable String id) {
		Boolean result = eligibilityRepository.existsById(Long.parseLong(id));
		eligibilityRepository.deleteById(Long.parseLong(id));
		return "{ \"success\" : " + (result ? "true" : "false") + " }";
	}

	@PostMapping()
	@ApiOperation("${eligibilityController.createEligibility}")
	public Eligibility createEligibility(
			@ApiParam("Eligibility information for a new eligibility to be created.") @RequestBody Eligibility newEligibility) {
		String id = String.valueOf(new Random().nextInt());
		Eligibility eligibility = new Eligibility(id, newEligibility.getFirstName(), newEligibility.getLastName(),
				newEligibility.getEmail());
		eligibilityRepository.save(eligibility);
		return eligibility;
	}
}
