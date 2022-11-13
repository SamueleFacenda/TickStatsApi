package com.tickstats.tickstatsapi.controllers;


import com.tickstats.tickstatsapi.repositories.TickDataRepository;
import com.tickstats.tickstatsapi.repositories.UserRepository;
import com.tickstats.tickstatsapi.repositories.entities.Comment;
import com.tickstats.tickstatsapi.repositories.entities.MyUser;
import com.tickstats.tickstatsapi.repositories.entities.ReductedTickData;
import com.tickstats.tickstatsapi.repositories.entities.TickData;
import com.tickstats.tickstatsapi.requestresponse.MultipleTickDataPostRequest;
import com.tickstats.tickstatsapi.requestresponse.TickDataPostRequest;
import com.tickstats.tickstatsapi.requestresponse.UsernamePasswordRequest;
import com.tickstats.tickstatsapi.requestresponse.TickDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class ApiController {


    @Autowired
    private TickDataRepository tickDataRepository;

    @Autowired
    private UserRepository userRepository;

    public static final String ORIGIN = "http://localhost:3000";


    @CrossOrigin(origins = ORIGIN, allowCredentials = "true")
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

    //accessibile anche senza autenticazione, non serve neanche @CrossOrigin
    @PostMapping("/api/register")
    public void register(@RequestBody UsernamePasswordRequest request, HttpServletResponse response) throws Exception {
        if(userRepository.existsByUsername(request.getUsername())){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        MyUser user = new MyUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        userRepository.save(user);

        response.setStatus(HttpStatus.CREATED.value());
    }

    @CrossOrigin(origins = ORIGIN, allowCredentials = "true")
    @PostMapping("/api/postdata")
    public ResponseEntity<?> postdata(@RequestBody TickDataPostRequest data) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        MyUser currentUser = userRepository.findByUsername(currentPrincipalName);

        loadTickData(data.getLabel(), data.getComment(), data.getTimestamp(), currentUser);

        return ResponseEntity.ok("ok");
    }

    @CrossOrigin(origins = ORIGIN, allowCredentials = "true")
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
    @CrossOrigin(origins = ORIGIN, allowCredentials = "true")
    public String testauth() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }




}
