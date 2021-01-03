package com.example.demo.controller;

import com.example.demo.businessLogic.SymptomsBusinessLogic;
import com.example.demo.model.Symptoms;
import com.example.demo.model.viewModel.SymptomsViewModel;
import com.example.demo.utils.Constant;
import com.example.demo.utils.EntityResponse;
import com.example.demo.utils.Utility;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/symptoms")
public class SymptomsController {
    private Gson gson;
    private SymptomsBusinessLogic symptomsBusinessLogic;
    private EntityResponse entityResponse;
    private Utility utility;
    Logger logger = Logger.getLogger(SymptomsBusinessLogic.class);

    public SymptomsController(Gson gson, SymptomsBusinessLogic symptomsBusinessLogic, EntityResponse entityResponse, Utility utility) {
        this.gson = gson;
        this.symptomsBusinessLogic = symptomsBusinessLogic;
        this.entityResponse = entityResponse;
        this.utility = utility;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = ArithmeticException.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<SymptomsViewModel>> createSymptoms(@Valid @RequestBody SymptomsViewModel symptomsViewModel, BindingResult bindingResults) {
        MessageResponse<SymptomsViewModel> messageResponse = new MessageResponse<>();
        if (!bindingResults.hasErrors()) {

            List<Symptoms> symptom = symptomsBusinessLogic.getByColumnName("symptomsName", utility.capitalizeFirstLetter(symptomsViewModel.getSymptomsName()));
            if (!symptom.isEmpty()) {
                logger.info("==============Symptom already exist===============");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Symptom already exist!");
            }

            Symptoms symptoms = new Symptoms();
            symptoms.setSymptomsName(utility.capitalizeFirstLetter(symptomsViewModel.getSymptomsName().toLowerCase()));
            symptoms.setDateTimeCreated(new Date());
            symptoms.setDescription(symptomsViewModel.getDescription());
            symptoms.setStatus(Constant.STATUS.ACTIVATED.value);
            symptomsBusinessLogic.create(symptoms);
            messageResponse.setMessage("Symptom created successfully");
            messageResponse.setData(symptomsViewModel);
            messageResponse.setStatus(HttpStatus.OK.value());

            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        return entityResponse.getInvalidFormError(symptomsViewModel);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<MessageResponse<SymptomsViewModel>> updateSymptoms(@Valid @RequestBody SymptomsViewModel symptomsViewModel, BindingResult bindingResults) {
        MessageResponse<SymptomsViewModel> messageResponse = new MessageResponse<>();
        if (!bindingResults.hasErrors()) {
            try {
                Symptoms symptoms = symptomsBusinessLogic.findOne(symptomsViewModel.getId());
                if (symptoms == null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Symptom does not exist!");
                }
                if (!symptoms.getSymptomsName().equalsIgnoreCase(symptomsViewModel.getSymptomsName().trim())) {
                    List<Symptoms> symptomsList = symptomsBusinessLogic.getByColumnName("symptomsName", utility.capitalizeFirstLetter(symptomsViewModel.getSymptomsName()));
                    if (!symptomsList.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Symptom already exist!");
                    }
                }
                symptoms.setSymptomsName(utility.capitalizeFirstLetter(symptomsViewModel.getSymptomsName().toLowerCase()));
                String desc = symptomsViewModel.getDescription();
                symptoms.setDescription(desc == null || desc.isEmpty() ? symptoms.getDescription() : desc);
                symptomsBusinessLogic.update(symptoms);
                messageResponse.setMessage("Symptom update successfully");
                messageResponse.setData(symptomsViewModel);
                messageResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return entityResponse.getInvalidFormError(symptomsViewModel);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<MessageResponse<Symptoms>> deleteSymptom(@NotNull @PathVariable Integer id) {
        MessageResponse<Symptoms> messageResponse = new MessageResponse<>();
        try {
            Symptoms symptoms = symptomsBusinessLogic.findOne(id);
            if (symptoms == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Symptom does not exist!");
            }
            symptoms.setStatus(Constant.STATUS.DELETED.value);
            symptomsBusinessLogic.update(symptoms);
            messageResponse.setMessage("Symptom deleted successfully");
            messageResponse.setData(symptoms);
            messageResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Symptoms>>> getAllSymptoms() {
        MessageResponse<List<Symptoms>> messageResponse = new MessageResponse<>();
        List<Symptoms> symptomsList = symptomsBusinessLogic.getByColumnName("status", Constant.STATUS.ACTIVATED.value);
        messageResponse.setMessage("Symptoms retrieved successfully");
        symptomsList.sort(Comparator.comparing(Symptoms::getSymptomsName));
        messageResponse.setData(symptomsList);
        messageResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
