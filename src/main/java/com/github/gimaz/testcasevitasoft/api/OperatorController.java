package com.github.gimaz.testcasevitasoft.api;

import com.github.gimaz.testcasevitasoft.entity.Request;
import com.github.gimaz.testcasevitasoft.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/operator/")
@RequiredArgsConstructor
public class OperatorController {
    private final RequestService requestService;

    @GetMapping("getRequests")
    public @ResponseBody
    List<Request> getRequest() {
        List<Request> requests = requestService.getByStatus(Request.RequestStatus.SENT);
        for (Request request : requests) {
            request.setText(request.getText().replaceAll(".(?!$)", "$0-"));
        }
        return requests;
    }

    @PostMapping("updateRequest")
    public @ResponseBody
    HashMap<String, Object> updateRequest(@RequestBody Request request) {
        HashMap<String, Object> response = new HashMap<>();
        String status = "";
        String error = "";

        if (request.getId() == null || requestService.getById(request.getId()) == null) {
            status = "error";
            error = "Request doesn't exists";
            response.put("status", status);
            response.put("error", error);
        } else {
            Request requestFromDB = requestService.getById(request.getId());
            if (requestFromDB.getStatus() == Request.RequestStatus.SENT) {
                if (request.getStatus() == Request.RequestStatus.ACCEPTED || request.getStatus() == Request.RequestStatus.REJECTED) {
                    requestFromDB.setStatus(request.getStatus());
                    requestFromDB.setLastUpdateTime(LocalDateTime.now());
                    requestService.update(requestFromDB);
                    status = "success";
                    response.put("status", status);
                } else {
                    status = "error";
                    error = "Forbidden set Status " + request.getStatus() + " to Request";
                    response.put("status", status);
                    response.put("error", error);
                }
            } else {
                status = "error";
                error = "Forbidden change Request with status " + requestFromDB.getStatus();
                response.put("status", status);
                response.put("error", error);
            }
        }
        return response;
    }
}
