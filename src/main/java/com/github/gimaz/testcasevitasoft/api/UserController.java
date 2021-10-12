package com.github.gimaz.testcasevitasoft.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.gimaz.testcasevitasoft.entity.Request;
import com.github.gimaz.testcasevitasoft.entity.User;
import com.github.gimaz.testcasevitasoft.service.RequestService;
import com.github.gimaz.testcasevitasoft.service.RequestServiceImpl;
import com.github.gimaz.testcasevitasoft.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RequestService requestService;

    @GetMapping("getInfo")
    public @ResponseBody
    User getInfo(@RequestHeader("Authorization") String authorizationHeader) {
        return getUser(authorizationHeader);
    }


    @PostMapping("saveRequest")
    public @ResponseBody
    HashMap<String, Object> sentRequest(@RequestBody Request request, @RequestHeader("Authorization") String authorizationHeader) {
        HashMap<String, Object> response = new HashMap<>();
        String status = "";
        String error = "";

        User user = getUser(authorizationHeader);

        if (request.getId() != null) {
            Request requestFromDB = requestService.getById(request.getId());
            if (requestFromDB == null) {
                status = "error";
                error = "Request doesn't exists;";
                response.put("status", status);
                response.put("error", error);
            } else {
                if (requestFromDB.getUser().getId() == user.getId()) {
                    if (requestFromDB.getStatus() == Request.RequestStatus.DRAFT) {

                        if (request.getText() == null || request.getTitle() == null || request.getStatus() == null) {
                            status = "error";
                            error = "Fields Text, Title and Status cannot be null";
                            response.put("status", status);
                            response.put("error", error);
                        } else {
                            if (request.getStatus() == Request.RequestStatus.SENT || request.getStatus() == Request.RequestStatus.DRAFT) {
                                requestFromDB.setText(request.getText());
                                requestFromDB.setTitle(request.getTitle());
                                requestFromDB.setLastUpdateTime(LocalDateTime.now());
                                requestFromDB.setStatus(request.getStatus());
                                requestService.update(requestFromDB);
                                status = "success";
                                response.put("status", status);
                                response.put("request", requestFromDB);
                            } else {
                                status = "error";
                                error = "Forbidden set Status " + request.getStatus() + " to Request";
                                response.put("status", status);
                                response.put("error", error);
                            }

                        }
                    } else {
                        status = "error";
                        error = "Forbidden change Request with status " + request.getStatus();
                        response.put("status", status);
                        response.put("error", error);

                    }

                } else {
                    status = "error";
                    error = "Forbidden to change requests of other users";
                    response.put("status", status);
                    response.put("error", error);
                    return response;
                }
            }
        } else {
            if (request.getText() == null || request.getTitle() == null || request.getStatus() == null) {
                status = "error";
                error = "Fields Text, Title and Status cannot be null";
                response.put("status", status);
                response.put("error", error);
            } else {
                if (request.getStatus() == Request.RequestStatus.SENT || request.getStatus() == Request.RequestStatus.DRAFT) {
                    request.setCreatedTime(LocalDateTime.now());
                    request.setLastUpdateTime(request.getCreatedTime());
                    request.setUser(user);
                    requestService.add(request);
                    status = "success";
                    response.put("status", status);
                    response.put("request", request);
                } else {
                    status = "error";
                    error = "Forbidden set Status " + request.getStatus() + " to Request";
                    response.put("status", status);
                    response.put("error", error);
                }


            }

        }
        return response;
    }

    private User getUser(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userService.findByUsername(username);
        return user;
    }


}
