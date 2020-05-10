package com.example.demo.controller;

import com.example.demo.businessLogic.BusinessLogicModle;
import com.example.demo.businessLogic.BusinessUserLogic;
import com.example.demo.model.Business;
import com.example.demo.model.BusinessUser;
import com.example.demo.model.User;
import com.example.demo.model.viewModel.UserViewModel;
import com.example.demo.repository.UserLogic;
import com.example.demo.support.StringSupport;
import com.example.demo.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api")

public class UserController {
    private final UserLogic userLogic;

    private final StringSupport stringSupport;
    private BusinessLogicModle businessLogicModle;
    private BusinessUserLogic businessUserLogic;
    private Logger logger = Logger.getLogger(UserController.class);

    public UserController(StringSupport stringSupport, UserLogic userLogic,BusinessLogicModle businessLogicModle,BusinessUserLogic businessUserLogic) {
        this.stringSupport = stringSupport;
        this.userLogic = userLogic;
        this.businessLogicModle=businessLogicModle;
        this.businessUserLogic=businessUserLogic;
    }

    @Transactional
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<UserViewModel>> createUser(UserViewModel userViewModel) throws Exception {
        MessageResponse<UserViewModel> messageResponse ;

        try {
            List<User> userObject = userLogic.getByColumnName("username",userViewModel.getUserName());
            if(userObject != null  && !userObject.isEmpty()){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"User already exist!");
            }
            messageResponse = new MessageResponse<>();
            User user = new User();
            String salt = stringSupport.generate();
            user.setPassword(BCrypt.hashpw(userViewModel.getPassword(), salt));
            user.setUsername(userViewModel.getUserName());
            user.setRole(Constant.ROLES.USER.getValue());
            user.setSalt(salt);
            userLogic.create(user);
            messageResponse.setMessage("User created successfully");
            messageResponse.setData(userViewModel);
            messageResponse.setSuccessful(true);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            throw  new Exception(e);
        }


    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<User>>> getAllUsers(
    ) throws Exception {
        MessageResponse<List<User>> messageResponse = new MessageResponse<>();
        try {
            List<User> userList = userLogic.findAll();
            messageResponse.setMessage("Fetched successfully");
            messageResponse.setStatus(HttpStatus.OK.value());
            messageResponse.setData(userList);
            logger.info("<<<<<<<<<<<<<<<<User Accessed Successfully>>>>>>>>>>>>>>>");
        } catch (Exception e) {
            throw new Exception(e);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }




    @Async
    @RequestMapping(value = "/buisness-user", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<MessageResponse<List<BusinessUser>>>> createBusinessUser(@RequestParam Long business,@RequestParam Integer userId){
        MessageResponse<List<BusinessUser>> messageResponse = new MessageResponse<>();
        HttpHeaders headers = null;
        List<BusinessUser> businessList = new ArrayList<>();
        try {

            Business business1 = new Business();
            User user = new User();
            business1.setId(business);
            BusinessUser businessUser = new BusinessUser();
            businessUser.setBusiness(business1);
            user.setId(userId);
            businessUser.setUser(user);
            businessUserLogic.create(businessUser);
            messageResponse.setMessage("Fetched successfully");
            businessList.add(businessUser);
            messageResponse.setData(businessList);
            headers = new HttpHeaders();
            logger.info("<<<<<<<<<<<<<<<<User Accessed Successfully>>>>>>>>>>>>>>>");
        } catch (Exception e) {

            return CompletableFuture.completedFuture(new ResponseEntity<>(messageResponse, headers, HttpStatus.BAD_REQUEST));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(messageResponse, headers, HttpStatus.OK));
    }

    @Async
    @RequestMapping(value = "/buisness-user", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<MessageResponse<List<BusinessUser>>>> createBusinessUser(){
        MessageResponse<List<BusinessUser>> messageResponse = new MessageResponse<>();
        HttpHeaders headers = null;
        List<BusinessUser> businessList;
        try {

            businessList = businessUserLogic.findAll();
            messageResponse.setMessage("Fetched successfully");
            messageResponse.setData(businessList);
            headers = new HttpHeaders();
            logger.info("<<<<<<<<<<<<<<<<User Accessed Successfully>>>>>>>>>>>>>>>");
        } catch (Exception e) {

            return CompletableFuture.completedFuture(new ResponseEntity<>(messageResponse, headers, HttpStatus.BAD_REQUEST));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(messageResponse, headers, HttpStatus.OK));
    }

}
