package nl.codeclan.winestore.repository;

import nl.codeclan.winestore.model.Wine;

public interface WineRepository {

    Wine findWineByName(String wineName);
}
