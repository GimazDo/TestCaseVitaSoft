package com.github.gimaz.testcasevitasoft.service;

import com.github.gimaz.testcasevitasoft.entity.Request;
import com.github.gimaz.testcasevitasoft.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Override
    public boolean add(Request request) {
        if (request.getId() != null && requestRepository.findById(request.getId()) != null) {
            log.error("IN add - Request with id {} already exists", request.getId());
            return false;
        }
        requestRepository.save(request);
        log.info("IN add - Request from user {} with status {} successfully saved", request.getUser().getUsername(), request.getStatus());
        return true;
    }

    @Override
    public List<Request> getByStatus(Request.RequestStatus status) {

        List<Request> requests = requestRepository.findByStatusOrderByLastUpdateTime(status);
        log.info("IN getByStatus - Returned from DB {} requests with status {}", requests.size(), status);
        return requests;
    }

    @Override
    public boolean update(Request request) {
        if (requestRepository.findById(request.getId()) == null) {
            log.error("IN update - Request with id {} doesn't exists", request.getId());
            return false;
        }
        requestRepository.save(request);
        log.info("IN update - Request with id {} successfully updated", request.getId());
        return true;
    }

    @Override
    public Request getById(Long id) {
        Request request = requestRepository.getById(id);
        if (request == null) {
            log.warn("IN getById - Request with id {} not found", id);
            return null;
        }
        log.info("IN getById - Request with id {} found", id);
        return request;
    }
}
