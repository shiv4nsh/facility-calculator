package com.foobar.geo

import com.foobar.models.Location
import org.scalatest.FunSuite

class DifferenceCalculatorImplSpec extends FunSuite {

  test("distance should be 0 between user and warehouse for same location") {
    val userLocation = Location(0.0, 0.0)
    val distance = new DifferenceCalculatorImpl()
      .calculateDistanceInKilometer(userLocation, userLocation)
    assert(distance === 0)
  }

  test("distance should not be 0 between user and warehouse for different location") {
    val userLocation = Location(40.0, 20.0)
    val warehouseLocation = Location(10.0, 20.0)
    val distance = new DifferenceCalculatorImpl()
      .calculateDistanceInKilometer(userLocation, warehouseLocation)
    val actualDistance = 3335
    assert(distance === actualDistance)
  }
}
