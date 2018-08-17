package com.ilionx.winestore.service

import com.asml.winetstore.model.*
import com.ilionx.winestore.model.Order
import com.ilionx.winestore.model.OrderLine
import com.ilionx.winestore.repository.customer.CustomerRepositoryImpl
import com.ilionx.winestore.model.Address
import com.ilionx.winestore.model.Customer
import com.ilionx.winestore.model.Wine
import com.ilionx.winestore.repository.WineRepository
import spock.lang.Specification
import spock.lang.Unroll

import java.math.RoundingMode

class OrderServiceTest extends Specification {


    def "Test is the order is created correctly"() {
        given: "a customer repo is setup and a customer service is available"
        def customerRepo = Mock(CustomerRepositoryImpl)
        customerRepo.findCustomerById(_) >> new Customer()
        def orderService = new OrderService(customerRepo, null)

        when: "calling order creation"
        def result = orderService.createOrder("1234Customer")

        then: "the order number should contain '1234Customer'"
        result.orderNumber.contains("1234Customer")
        1 * customerRepo.findCustomerById(_)
    }

    def "test adding order lines to the order"() {
        given: "a wine repo is setup and a customer service is available"
        def wineRepo = Mock(WineRepository)
        wineRepo.findWineByName(_) >> createWine()

        def order = new Order()
        order.setOrderLines(new ArrayList<OrderLine>())
        def orderService = new OrderService(null, wineRepo)

        when: "adding order line"
        def result = orderService.addOrderLine(order, 'some name', 20).orderLines.get(0)

        then: "check if the order line is correct"
        result.wine.name == 'Corbieres'
        result.wine.productionYear == 2010
        result.wine.vineyard == 'Corbieres'
        result.wine.volume == 75
        result.wine.price == 10.50
        result.description == 'Corbieres ;Corbieres ;2010 ;E 10.5'
        result.number == 20
        result.total == 210.00
    }

    @Unroll
    def "test if the order is completed correctly with number of order lines #numberOfLines and shipping address #zipCode, #number "() {
        given: "an order with multiple order lines"
        def orderService = new OrderService(null, null)
        def order = new Order()
        order.orderLines = createOderLines(numberOfLines)
        order.customer = createCustomer()

        when: "calling order creation"
        def result = orderService.completeOrder(order, address)

        then: "check if the numberOfProduct , orderPrice and orderVat are calculated correctly"
        result.numberOfProduct == numberOfProduct
        result.orderPrice == orderPrice
        def roundedOrderVat =new BigDecimal(result.vat).setScale(2, RoundingMode.HALF_UP).doubleValue()
        roundedOrderVat == orderVat
        result.customer.shippingAddress.zipCode == zipCode
        result.shippingAddress.number == number

        where: ""
        numberOfLines | address                                 || numberOfProduct | orderPrice | orderVat | zipCode  | number
        2             | null                                    || 6               | 63.00      | 13.23    | "5503LA" | 1144
        10            | createAddress("De Run", 1110, "5503LA") || 30              | 315        | 66.15    | "5503LA" | 1110
    }

    def createOderLines(int numberOfLines) {
        def orderLines = new ArrayList();
        numberOfLines.times({
            orderLines.add(createOrderLine(3))
        })
        return orderLines
    }


    def createOrderLine(Integer numberOfBottles) {
        OrderLine orderLine = new OrderLine();
        Wine wine = createWine()
        orderLine.setWine(wine);
        orderLine.setDescription(wine.getName()
                + " ;" + wine.getVineyard()
                + " ;" + wine.getProductionYear()
                + " ;E " + wine.getPrice());
        orderLine.setNumber(numberOfBottles);
        orderLine.setTotal(wine.getPrice() * numberOfBottles);
        orderLine.setNumber(numberOfBottles);
        return orderLine
    }

    def createWine() {
        def wine = new Wine()
        wine.name = 'Corbieres'
        wine.productionYear = 2010
        wine.vineyard = 'Corbieres'
        wine.volume = 75
        wine.price = 10.50
        return wine
    }

    def createCustomer() {
        def customer = new Customer()
        customer.name = "ASML"
        customer.shippingAddress = createAddress()
        customer
    }

    def createAddress(String streetName = "De Run",
                      Long number = 1144,
                      String zipCode = "5503LA") {
        def address = new Address()
        address.street = streetName
        address.number = number
        address.city = "Veldhoven"
        address.zipCode = zipCode
        address
    }
}
