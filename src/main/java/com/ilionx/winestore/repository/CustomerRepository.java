package com.ilionx.winestore.repository;

import com.ilionx.winestore.model.Customer;

public interface CustomerRepository {

    Customer findCustomerById(String customerId);
}
