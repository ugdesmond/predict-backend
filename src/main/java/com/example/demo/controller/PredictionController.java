package com.example.demo.controller;

import com.example.demo.businessLogic.SymptomsSicknessBusinessLogic;
import com.example.demo.utils.EntityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private SymptomsSicknessBusinessLogic symptomsSicknessBusinessLogic;
    private EntityResponse entityResponse;

    public PredictionController(SymptomsSicknessBusinessLogic symptomsSicknessBusinessLogic, EntityResponse entityResponse) {
        this.symptomsSicknessBusinessLogic = symptomsSicknessBusinessLogic;
        this.entityResponse = entityResponse;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<String>>> getSicknessPredictions(@NotNull @NotEmpty @RequestParam String predictionValue) throws Exception {
        MessageResponse<List<String>> messageResponse = new MessageResponse<>();
        String[] predictionArray = predictionValue.split(",");
        if (predictionValue.equals("") || predictionArray.length != 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Param must contain 6 comma separated  values");
        }
        List<String> predictionList = symptomsSicknessBusinessLogic.getPredictionList(predictionArray);
        messageResponse.setMessage("Sicknesses retrieved successfully");
        messageResponse.setData(predictionList);
        messageResponse.setStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
