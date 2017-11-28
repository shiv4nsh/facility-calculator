package com.foobar.models


sealed trait LocationT

case class Location(lat:Double,lon:Double) extends LocationT
