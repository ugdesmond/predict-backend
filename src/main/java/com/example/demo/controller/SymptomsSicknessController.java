package com.example.demo.controller;

import com.example.demo.businessLogic.SymptomsBusinessLogic;
import com.example.demo.businessLogic.SymptomsSicknessBusinessLogic;
import com.example.demo.model.Sickness;
import com.example.demo.model.Symptoms;
import com.example.demo.model.SymptomsSickness;
import com.example.demo.model.dto.SicknessSymptomDTO;
import com.example.demo.model.viewModel.SymptomsSicknessViewModel;
import com.example.demo.utils.Constant;
import com.example.demo.utils.EntityResponse;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/symptoms-sickness")
public class SymptomsSicknessController {
    Logger logger = Logger.getLogger(SymptomsBusinessLogic.class);
    private Gson gson;
    private SymptomsSicknessBusinessLogic symptomsSicknessBusinessLogic;
    private EntityResponse entityResponse;

    public SymptomsSicknessController(Gson gson, SymptomsSicknessBusinessLogic symptomsSicknessBusinessLogic, EntityResponse entityResponse) {
        this.gson = gson;
        this.symptomsSicknessBusinessLogic = symptomsSicknessBusinessLogic;
        this.entityResponse = entityResponse;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<SymptomsSicknessViewModel>> createSymptomsSickness(@Valid @RequestBody SymptomsSicknessViewModel symptomsSicknessViewModel, BindingResult bindingResults) throws Exception {
        MessageResponse<SymptomsSicknessViewModel> messageResponse = new MessageResponse<>();
        List<SymptomsSickness> symptomsSicknessList = new ArrayList<>();
        if (!bindingResults.hasErrors()) {
            String[] symptomsArray = symptomsSicknessViewModel.getSymptoms().split(",");
            for (String symptom : symptomsArray) {
                SymptomsSickness symptomsSickness = new SymptomsSickness();
                Sickness sickness = new Sickness();
                Integer symptomsId = Integer.parseInt(symptom);
                Symptoms symptoms = new Symptoms();
                symptoms.setId(symptomsId);
                sickness.setId(symptomsSicknessViewModel.getSicknessId());
                List<SymptomsSickness> symptomAndSicknessList = symptomsSicknessBusinessLogic.searchBySymptomAndSickness(symptoms, sickness);
                if (symptomAndSicknessList != null && !symptomAndSicknessList.isEmpty()) {
                    continue;
                }
                symptomsSickness.setSymptoms(symptoms);
                symptomsSickness.setSickness(sickness);
                symptomsSickness.setStatus(Constant.STATUS.ACTIVATED.getValue());
                symptomsSickness.setDateTimeCreated(new Date());
                symptomsSicknessList.add(symptomsSickness);
            }
            symptomsSicknessBusinessLogic.create(symptomsSicknessList);
            messageResponse.setMessage("Sickness-symptoms assigned successfully");
            messageResponse.setData(symptomsSicknessViewModel);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        }
        return entityResponse.getInvalidFormError(symptomsSicknessViewModel);

    }

    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse<SymptomsSicknessViewModel>> updateSymptomsSickness(@Valid @RequestBody SymptomsSicknessViewModel symptomsSicknessViewModel, BindingResult bindingResults) {
        try {
            MessageResponse<SymptomsSicknessViewModel> messageResponse = new MessageResponse<>();
            List<SymptomsSickness> symptomsSicknessList = new ArrayList<>();
            if (!bindingResults.hasErrors()) {
                String[] symptomsArray = symptomsSicknessViewModel.getSymptoms().split(",");
                int[] symptomIntegerArray = new int[symptomsArray.length];
                for (int x = 0; x < symptomsArray.length; x++) {
                    symptomIntegerArray[x] = Integer.parseInt(symptomsArray[x]);
                }

                Sickness sicknessObject = new Sickness();
                sicknessObject.setId(symptomsSicknessViewModel.getSicknessId());
                HashSet<Integer> symptomHashSet = symptomsSicknessBusinessLogic.getByColumnName("sickness", sicknessObject).stream().filter(p -> p.getStatus().equals(Constant.STATUS.ACTIVATED.getValue())).collect(Collectors.toList()).stream().map(p -> p.getSymptoms().getId()).collect(Collectors.toCollection(HashSet::new));
                if (symptomHashSet.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Sickness does not exist!");
                }
                for (Integer symptomVal : symptomIntegerArray) {
                    symptomHashSet.add(symptomVal);
                }

                for (Integer symptom : symptomHashSet) {
                    SymptomsSickness symptomsSickness = new SymptomsSickness();
                    Sickness sickness = new Sickness();
                    Symptoms symptoms = new Symptoms();
                    symptoms.setId(symptom);
                    sickness.setId(symptomsSicknessViewModel.getSicknessId());
                    List<SymptomsSickness> symptomAndSicknessList = symptomsSicknessBusinessLogic.searchBySymptomAndSickness(symptoms, sickness);
                    if (symptomAndSicknessList != null && !symptomAndSicknessList.isEmpty()) {
                        boolean checkIfExistInSymptomsArray = IntStream.of(symptomIntegerArray).anyMatch(n -> n == symptom);
                        if (checkIfExistInSymptomsArray) {
                            continue;
                        }
                        symptomAndSicknessList.get(0).setStatus(Constant.STATUS.DELETED.getValue());
                        symptomsSicknessBusinessLogic.update(symptomAndSicknessList.get(0));
                        logger.info("===============sickness-symptom deleted successfully============");
                    } else {
                        symptomsSickness.setStatus(Constant.STATUS.ACTIVATED.getValue());

                        symptomsSickness.setSymptoms(symptoms);
                        symptomsSickness.setSickness(sickness);
                        symptomsSickness.setDateTimeCreated(new Date());
                        symptomsSicknessList.add(symptomsSickness);
                    }
                }
                symptomsSicknessBusinessLogic.create(symptomsSicknessList);
                messageResponse.setMessage("Sickness-symptoms updated  successfully");
                messageResponse.setData(symptomsSicknessViewModel);
                messageResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(messageResponse, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.info("=========" + e.getMessage());
            throw e;
        }
        return entityResponse.getInvalidFormError(symptomsSicknessViewModel);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<SicknessSymptomDTO>>> getAllSymptomSickness() {
        MessageResponse<List<SicknessSymptomDTO>> messageResponse = new MessageResponse<>();
        List<SymptomsSickness> symptomsList = symptomsSicknessBusinessLogic.getByColumnName("status", Constant.STATUS.ACTIVATED.value);
        HashMap<String, List<String>> symptomListMap = new HashMap<>();
        for (SymptomsSickness symptomsSickness : symptomsList) {
            if (symptomsSickness.getSickness().getStatus().equals(Constant.STATUS.DELETED.value) || symptomsSickness.getSymptoms().getStatus().equals(Constant.STATUS.DELETED.value))
                continue;
            List<String> stringArrayList = symptomListMap.get(symptomsSickness.getSickness().getSicknessName());
            if (stringArrayList == null || stringArrayList.isEmpty()) {
                stringArrayList = new ArrayList<>();
            }
            stringArrayList.add(symptomsSickness.getSymptoms().getSymptomsName());
            symptomListMap.put(symptomsSickness.getSickness().getSicknessName(), stringArrayList);
        }
        List<SicknessSymptomDTO> symptomDTOArrayList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : symptomListMap.entrySet()) {
            SicknessSymptomDTO sicknessSymptomDTO = new SicknessSymptomDTO();
            sicknessSymptomDTO.setSickness(entry.getKey());
            sicknessSymptomDTO.setSymptoms(entry.getValue());
            symptomDTOArrayList.add(sicknessSymptomDTO);
        }
        messageResponse.setMessage("Retrieved successfully");
        messageResponse.setData(symptomDTOArrayList);
        messageResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
