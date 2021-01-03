package com.example.demo.controller;

import com.example.demo.businessLogic.SicknessBusinessLogic;
import com.example.demo.model.Sickness;
import com.example.demo.model.viewModel.SicknessViewModel;
import com.example.demo.utils.Constant;
import com.example.demo.utils.EntityResponse;
import com.example.demo.utils.Utility;
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

@RestController
@RequestMapping("/api/sickness")
public class SicknessController {

    private SicknessBusinessLogic sicknessBusinessLogic;
    private EntityResponse entityResponse;
    private Utility utility;

    public SicknessController(SicknessBusinessLogic sicknessBusinessLogic, EntityResponse entityResponse, Utility utility) {
        this.sicknessBusinessLogic = sicknessBusinessLogic;
        this.entityResponse = entityResponse;
        this.utility = utility;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse<SicknessViewModel>> createSickness(@Valid @RequestBody SicknessViewModel sicknessViewModel, BindingResult bindingResults) throws Exception {
        MessageResponse<SicknessViewModel> messageResponse = new MessageResponse<>();
        if (!bindingResults.hasErrors()) {
            List<Sickness> sicknessList = sicknessBusinessLogic.getByColumnName("sicknessName", utility.capitalizeFirstLetter(sicknessViewModel.getSicknessName()));
            if (!sicknessList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Sickness already exist!");
            }
            Sickness sickness = new Sickness();
            sickness.setDateTimeCreated(new Date());
            sickness.setSicknessName(utility.capitalizeFirstLetter(sicknessViewModel.getSicknessName().toLowerCase()));
            sickness.setSicknessDescription(sicknessViewModel.getSicknessDescription());
            sickness.setStatus(Constant.STATUS.ACTIVATED.value);
            sicknessBusinessLogic.create(sickness);
            messageResponse.setMessage("Sickness created successfully");
            messageResponse.setData(sicknessViewModel);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        return entityResponse.getInvalidFormError(sicknessViewModel);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<MessageResponse<SicknessViewModel>> updateSickness(@Valid @RequestBody SicknessViewModel sicknessViewModel, BindingResult bindingResults) throws Exception {
        MessageResponse<SicknessViewModel> messageResponse = new MessageResponse<>();
        if (!bindingResults.hasErrors()) {
            try {
                Sickness sickness = sicknessBusinessLogic.findOne(sicknessViewModel.getId());
                if (sickness == null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Sickness does not exist!");
                }
                if (!sickness.getSicknessName().equalsIgnoreCase(sicknessViewModel.getSicknessName().trim())) {
                    List<Sickness> sicknessList = sicknessBusinessLogic.getByColumnName("sicknessName", utility.capitalizeFirstLetter(sicknessViewModel.getSicknessName()));
                    if (!sicknessList.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Sickness already exist!");
                    }
                }
                sickness.setSicknessName(utility.capitalizeFirstLetter(sicknessViewModel.getSicknessName().toLowerCase()));
                String desc = sicknessViewModel.getSicknessDescription();
                sickness.setSicknessDescription(desc == null || desc.isEmpty() ? sickness.getSicknessDescription() : desc);
                sicknessBusinessLogic.update(sickness);
                messageResponse.setMessage("Sickness update successfully");
                messageResponse.setData(sicknessViewModel);
                messageResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return entityResponse.getInvalidFormError(sicknessViewModel);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<MessageResponse<Sickness>> deleteSickness(@PathVariable @NotNull Integer id) {
        MessageResponse<Sickness> messageResponse = new MessageResponse<>();
        try {
            Sickness sickness = sicknessBusinessLogic.findOne(id);
            if (sickness == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Sickness does not exist!");
            }
            sickness.setStatus(Constant.STATUS.DELETED.value);
            sicknessBusinessLogic.update(sickness);
            messageResponse.setMessage("Sickness deleted successfully");
            messageResponse.setData(sickness);
            messageResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Sickness>>> getAllSickness() {
        MessageResponse<List<Sickness>> messageResponse = new MessageResponse<>();
        List<Sickness> sicknessList = sicknessBusinessLogic.getByColumnName("status", Constant.STATUS.ACTIVATED.value);
        messageResponse.setMessage("Sicknesses retrieved successfully");
        sicknessList.sort(Comparator.comparing(Sickness::getSicknessName));
        messageResponse.setData(sicknessList);
        messageResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
