package com.testtask.socialnetworkservice.service;

import java.util.List;

public interface ExternalDataLoadService<T> {

    List<T> loadFromUrl(String url, Class<T[]> tClass);
}
