package com.tickstats.tickstatsapi.controllers;


import com.tickstats.tickstatsapi.repositories.TickDataRepository;
import com.tickstats.tickstatsapi.repositories.UserRepository;
import com.tickstats.tickstatsapi.repositories.entities.Comment;
import com.tickstats.tickstatsapi.repositories.entities.MyUser;
import com.tickstats.tickstatsapi.repositories.entities.ReductedTickData;
import com.tickstats.tickstatsapi.repositories.entities.TickData;
import com.tickstats.tickstatsapi.requestresponse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin(origins = {"${access.control.allow.origin}"}, allowCredentials = "true")
public class ApiController {


    @Autowired
    private TickDataRepository tickDataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/api/data")
    public ResponseEntity<?> data() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        MyUser currentUser = userRepository.findByUsername(currentPrincipalName);

        List<TickData> data = currentUser.getUserData();//get data from user (lazy load)

        List<ReductedTickData> redData = data.stream().map(ReductedTickData::fromTickData).toList();

        //qui posso fare l'elaborazione lato server, se voglio, se no posso mandare i dati grezzi

        return ResponseEntity.ok(new TickDataResponse(redData));
    }

    @GetMapping("/api/datacount")
    public ResponseEntity<?> datacount() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        MyUser currentUser = userRepository.findByUsername(currentPrincipalName);

        return ResponseEntity.ok(
            new LabelFrequencyResponse(
                userRepository
                    .findCountLabelForUser(currentUser.getUsername())
                    .stream()
                    .map(dataInterface -> new LabelFrequencyResponse.LabelFrequency(dataInterface.getLabel(), dataInterface.getFrequency()))
                    .toList()
        ));
    }

    //accessibile anche senza autenticazione
    @PostMapping("/api/register")
    public void register(@RequestBody UsernamePasswordRequest request, HttpServletResponse response) throws Exception {
        if(userRepository.existsByUsername(request.getUsername())){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        MyUser user = new MyUser();
        user.setUsername(request.getUsername());

        String password = passwordEncoder.encode(request.getPassword());
        user.setPassword(password);
        try {
            userRepository.save(user);
            response.setStatus(HttpStatus.CREATED.value());
        }catch(DataIntegrityViolationException e){
            // user already exists
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

    }


    @PostMapping("/api/postdata")
    public ResponseEntity<?> postdata(@RequestBody TickDataPostRequest data) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        MyUser currentUser = userRepository.findByUsername(currentPrincipalName);

        loadTickData(data.getLabel(), data.getComment(), data.getTimestamp(), currentUser);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/api/postdatamultiple")
    public ResponseEntity<?> postdatamultiple(@RequestBody MultipleTickDataPostRequest data) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        MyUser currentUser = userRepository.findByUsername(currentPrincipalName);

        for (TickDataPostRequest singleData : data.getTickData())
            loadTickData(singleData.getLabel(), singleData.getComment(), singleData.getTimestamp(), currentUser);

        return ResponseEntity.ok("ok");
    }

    private void loadTickData(String label, String comment, String timestamp, MyUser user){
        TickData tickData = new TickData();
        tickData.setLabel(label);
        tickData.setCreatedat(Timestamp.valueOf(timestamp));
        tickData.setUserid(user);

        if(!comment.isBlank()){
            Comment comment1 = new Comment();
            comment1.setComment(comment);
            tickData.setComment(comment1);
        }

        tickDataRepository.save(tickData);//questo salva anche il commento per il cascade ALL (vedi entity)
    }

    @GetMapping("/api/testauth")
    public String testauth() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }




}
