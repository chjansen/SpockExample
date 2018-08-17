package com.ilionx.winestore.repository;

import com.ilionx.winestore.model.Wine;

public interface WineRepository {

    Wine findWineByName(String wineName);
}
