package com.foobar.facility

import com.foobar.geo.DifferenceCalculatorImpl
import com.foobar.models._
import org.scalatest.FunSuite

class FacilityOptimizerSpec extends FunSuite {

  val customerLocation = Location(0.0, 0.0)
  val customer = Customer(1, "USER1", customerLocation)
  val product1 = Product(ProductInfo(1), 10)
  val product2 = Product(ProductInfo(2), 10)
  val product3 = Product(ProductInfo(3), 10)
  val productList = List(product1, product2, product3)
  val order = Order(customer, productList)

  test("should give all orders from the nearest warehouse all can fullfill the order.") {

    val warehouse1 = Warehouse(1, "A", Location(10, 0), productList)
    val warehouse2 = Warehouse(2, "B", Location(20, 0), productList)
    val warehouse3 = Warehouse(3, "C", Location(30, 0), productList)
    val warehouses = List[Warehouse](warehouse1, warehouse2, warehouse3)

    val differenceCalculator = new DifferenceCalculatorImpl
    val facility = new FacilityOptimizer(differenceCalculator)
    val expectedResult = Right(CalculatedOrder(
      DeliverPrice(0.0, 0.0),
      List(OrderInfo(1, 1, 10, DeliverPrice(0.0, 0.0)),
        OrderInfo(1, 2, 10, DeliverPrice(0.0, 0.0)),
        OrderInfo(1, 3, 10, DeliverPrice(0.0, 0.0)))))
    val result = facility.calculateOrder(order, warehouses)
    assert(result === expectedResult)
  }

  test("should divide the order into parts if its not fullfilled by single warehouse") {

    val product1 = Product(ProductInfo(1), 10)
    val product2 = Product(ProductInfo(2), 5)
    val product3 = Product(ProductInfo(3), 10)
    val productList2 = List(product1, product2, product3)
    val warehouse1 = Warehouse(1, "A", Location(10, 0), productList2)
    val warehouse2 = Warehouse(2, "B", Location(20, 0), productList)
    val warehouse3 = Warehouse(3, "C", Location(30, 0), productList)
    val warehouses = List[Warehouse](warehouse1, warehouse2, warehouse3)
    val differenceCalculator = new DifferenceCalculatorImpl
    val facility = new FacilityOptimizer(differenceCalculator)
    val expectedResult = Right(CalculatedOrder(DeliverPrice(0.0, 0.0),
      List(
        //Fullfilled by A
        OrderInfo(1, 1, 10, DeliverPrice(0.0, 0.0)),
        OrderInfo(1, 2, 5, DeliverPrice(0.0, 0.0)),
        OrderInfo(1, 3, 10, DeliverPrice(0.0, 0.0)),
        //Fullfilled by B
        OrderInfo(2, 2, 5, DeliverPrice(0.0, 0.0)))))
    val result = facility.calculateOrder(order, warehouses)
    assert(result === expectedResult)
  }

  test("should return appropriate message if order is not fulfilled") {

    val product1 = Product(ProductInfo(1), 10)
    val product2 = Product(ProductInfo(2), 1)
    val product3 = Product(ProductInfo(3), 10)
    val productList2 = List(product1, product2, product3)
    val warehouse1 = Warehouse(1, "A", Location(10, 0), productList2)
    val warehouse2 = Warehouse(2, "B", Location(20, 0), productList2)
    val warehouse3 = Warehouse(3, "C", Location(30, 0), productList2)
    val warehouses = List[Warehouse](warehouse1, warehouse2, warehouse3)
    val differenceCalculator = new DifferenceCalculatorImpl
    val facility = new FacilityOptimizer(differenceCalculator)
    val expectedResult = Left("Order Not Fullfilled")
    val result = facility.calculateOrder(order, warehouses)
    assert(result === expectedResult)
  }
}
