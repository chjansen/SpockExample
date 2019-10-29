package nl.codeclan.winestore.repository;

import nl.codeclan.winestore.model.Customer;

public interface CustomerRepository {

    Customer findCustomerById(String customerId);
}
