package com.foobar.facility

import com.foobar.geo.DifferenceCalculator
import com.foobar.models._


class FacilityOptimizer(differenceCalculator: DifferenceCalculator) {

  //Sorting out the List of Warehouses on basis of Location

  private def sortedWarehouses(customerLocation: Location, warehouses: Warehouses): Warehouses = {
    warehouses.map { a =>
      (a, differenceCalculator.calculateDistanceInKilometer(customerLocation, a.location))
    }.sortBy(_._2).map(_._1)
  }

  def calculateOrder(order: Order, warehouses: Warehouses): Either[String, CalculatedOrder] = {
    val products = order.productsInCart
    val sortedWarehousesList = sortedWarehouses(order.customer.location, warehouses)
    calulateOrderList(products, sortedWarehousesList) match {
      case Right(a) =>
        val orderinfoList = a.toList.flatten
        val totalGroundedPrice = orderinfoList.map(_.deliveryPrice.grounded).sum
        val totalsecondDayAir = orderinfoList.map(_.deliveryPrice.secondDayAir).sum
        Right(CalculatedOrder(DeliverPrice(totalGroundedPrice, totalsecondDayAir), orderinfoList))
      case Left(a) => Left(a)
    }

  }

  //Todo : Add the functionality for calculating the delivery Price
  private def getDeliveryPrice = DeliverPrice(0, 0)

  private def calulateOrderList(productsInfoList: Products, warehouses: Warehouses): Either[String, Option[OrdersInfoList]] = {
    (productsInfoList, warehouses) match {
      case (a, _) if a.isEmpty => Right(None)
      case (a, b) if b.isEmpty && a.nonEmpty => Left("Order Not Fullfilled")
      case _ =>
        val warehouse = warehouses.head
        val productsAvailableInWarehouse =
          warehouse.availableProducts.map(_.product).intersect(productsInfoList.map(_.product))
        val productsList = productsInfoList.filter(a => productsAvailableInWarehouse.contains(a.product))
        val remainingProductsList = productsInfoList.diff(productsList)
        val toupleList = productsList.map { productinfo =>
          val productAvailability = warehouse.availableProducts.filter(_.product == productinfo.product)
          lazy val extraValue = productinfo.quantity - productAvailability.head.quantity
          if (extraValue > 0) {
            (Some(productinfo.copy(quantity = extraValue)), OrderInfo(warehouse.id, productinfo.product.id, productAvailability.head.quantity, DeliverPrice(0, 0)))
          } else {
            val deliverPrice: DeliverPrice = getDeliveryPrice
            (None, OrderInfo(warehouse.id, productinfo.product.id, productinfo.quantity, deliverPrice))
          }
        }
        val orderInfo = toupleList.map(_._2)
        val notFullfilledQuantityList = toupleList.flatMap(_._1.toList)
        val tolalProductsRequired = remainingProductsList ++ notFullfilledQuantityList
        val remainingWarehouses = warehouses.tail
        calulateOrderList(tolalProductsRequired, remainingWarehouses) match {
          case Left(a) => Left(a)
          case Right(a) => Right(Some(orderInfo ++ a.toList.flatten))
        }
    }
  }

}
