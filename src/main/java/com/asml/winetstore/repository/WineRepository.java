package com.asml.winetstore.repository;

import com.asml.winetstore.model.Wine;

public interface WineRepository {

    Wine findWineByName(String wineName);
}
