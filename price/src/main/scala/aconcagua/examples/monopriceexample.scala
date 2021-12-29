package aconcagua.examples

import aconcagua.examples.monopriceexample.BRSS
import aconcagua.monoprice.Prices
import aconcagua.monoprice.Currency

object monopriceexample:

  val ARS = Prices.ofCurrency(Currency.ARS)
  val EUR = Prices.ofCurrency(Currency.EUR)

  val ars: ARS.Price = ARS * 100
  val eur: EUR.Price = EUR * 10

  val arss: ARS.Price = ARS * 100 + ARS * 200

////  this doesn't compile:
//  val mixed = ARS * 100 + EUR * 10

  val BRS: "BRS" = "BRS"

  val BRSS = Prices.ofCurrency(BRS)

  val brs: BRSS.Price = BRSS * 100

  val res = for {
    a <- ARS.fromString("ARS 100")
    b <- ARS.fromString("ARS 100")
    c <- ARS.fromString("ARS 100")
  } yield a + b + c
