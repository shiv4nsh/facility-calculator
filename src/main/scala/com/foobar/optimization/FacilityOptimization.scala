package com.foobar.optimization

import com.foobar.geo.DifferenceCalculator
import com.foobar.models._

import scala.annotation.tailrec

class FacilityOptimization(differenceCalculator: DifferenceCalculator) {

  //Sorting out the List of Warehouses on basis of Location

  def sortedWarehouses(customerLocation: Location, warehouses: Warehouses): Warehouses = {
    warehouses.map { a =>
      (a, differenceCalculator.calculateDistanceInKilometer(customerLocation, a.location))
    }.sortBy(_._2).map(_._1)
  }

  def calculateOrder(order: Order, warehouses: Warehouses): CalculatedOrder = {
    val products = order.productsInCart
    val sortedWarehousesList = sortedWarehouses(order.customer.location, warehouses)
    val orderinfoList =calulateOrderList(products, sortedWarehousesList).toList.flatten
    val totalGroundedPrice=orderinfoList.map(_.deliveryPrice.grounded).sum
    val totalsecondDayAir=orderinfoList.map(_.deliveryPrice.secondDayAir).sum
    CalculatedOrder(DeliverPrice(totalGroundedPrice,totalsecondDayAir),orderinfoList)
  }

  def calulateOrderList(productsInfoList: ProductsInfoList, warehouses: Warehouses): Option[OrdersInfoList] = {
    (productsInfoList, warehouses) match {
      case (a, _) if a.isEmpty => None
      case (a, b) if b.isEmpty && a.nonEmpty => None
      case _ =>
        val warehouse = warehouses.head
        val productsAvailableInWarehouse =
          warehouse.availableProducts.map(_.product).intersect(productsInfoList.map(_.product))
        val productsList = productsInfoList.filter(a => productsAvailableInWarehouse.contains(a.product))
        val remainingProductsList = productsInfoList.diff(productsList)
        val toupleList = productsList.map { productinfo =>
          val productAvailability = warehouse.availableProducts.filter(_.product == productinfo.product)
          lazy val extraValue = productinfo.quantity - productAvailability.head.quantity
          if (extraValue < 0) {
            (Some(productinfo.copy(quantity = extraValue)), OrderInfo(warehouse.id, productAvailability.head.quantity, DeliverPrice(0, 0)))
          } else {
            (None, OrderInfo(warehouse.id, productinfo.quantity, DeliverPrice(0, 0)))
          }
        }
        val orderInfo = toupleList.map(_._2)
        val notFullfilledQuantityList = toupleList.flatMap(_._1.toList)
        val tolalProductsRequired = remainingProductsList ++ notFullfilledQuantityList
        val remainingWarehouses = warehouses.tail
        Some(orderInfo ++ calulateOrderList(tolalProductsRequired, remainingWarehouses).toList.flatten)
    }
  }

}
