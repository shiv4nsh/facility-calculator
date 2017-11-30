package com.foobar.models


case class Location(lat: Double, lon: Double)

case class Warehouse(id: Id, name: Name, location: Location, availableProducts: Products)

case class Customer(id: Id, name: Name, location: Location)

case class ProductInfo(id: Id)

case class Product(product: ProductInfo, quantity: Int)

case class Order(customer: Customer, productsInCart: Products)

case class DeliverPrice(grounded: Double, secondDayAir: Double)

case class OrderInfo(warehouseId: Id, productId: Id, productQuantity: Int, deliveryPrice: DeliverPrice)

case class CalculatedOrder(totalPrice: DeliverPrice, orderInfo: OrdersInfoList)

