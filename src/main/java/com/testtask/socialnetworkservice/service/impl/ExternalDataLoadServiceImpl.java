package com.testtask.socialnetworkservice.service.impl;

import com.testtask.socialnetworkservice.exception.UnableLoadDataException;
import com.testtask.socialnetworkservice.service.ExternalDataLoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ExternalDataLoadServiceImpl<T> implements ExternalDataLoadService<T> {
    private final RestTemplate restTemplate;

    @Autowired
    public ExternalDataLoadServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param url    is the full URL to get data from
     * @param tClass is the class of data
     * @return list of {@code tClass} objects
     * @throws UnableLoadDataException if it is unable to load data by provided URL
     */
    @Override
    public List<T> loadFromUrl(String url, Class<T[]> tClass) {
        try {
            return Arrays.asList(restTemplate.getForObject(url, tClass));
        } catch (RestClientException e) {
            log.error("Failed to load {} by URL '{}'", tClass.getSimpleName(), url);
            throw new UnableLoadDataException(String.format("Failed to load %s by URL '%s'", tClass.getSimpleName(), url), e);
        }
    }
}
