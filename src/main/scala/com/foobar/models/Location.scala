package com.foobar.models


case class Location(lat: Double, lon: Double)

case class Warehouse(id: Id, name: Name, location: Location, availableProducts: ProductsInfoList)

case class Customer(id: Id, name: Name, location: Location)

case class Product(id: Id)

case class ProductInfo(product: Product, quantity: Int)

case class Order(customer: Customer, productsInCart: ProductsInfoList)

case class DeliverPrice(grounded: Double, secondDayAir: Double)

case class OrderInfo(warehouseId: Id, productQuantity: Int, deliveryPrice: DeliverPrice)

case class CalculatedOrder(totalPrice: DeliverPrice, orderInfo: OrdersInfoList)

