package com.github.gimaz.testcasevitasoft.service;

import com.github.gimaz.testcasevitasoft.entity.Request;

import java.util.List;

public interface RequestService {

    boolean add(Request request);

    List<Request> getByStatus(Request.RequestStatus status);

    boolean update(Request request);

    Request getById(Long id);


}
