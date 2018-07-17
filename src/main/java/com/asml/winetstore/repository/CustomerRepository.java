package com.asml.winetstore.repository;

import com.asml.winetstore.model.Customer;

public interface CustomerRepository {

    Customer findCustomerById(String customerId);
}
