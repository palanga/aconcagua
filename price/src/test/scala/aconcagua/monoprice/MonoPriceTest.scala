package aconcagua.monoprice

import aconcagua.monoprice.{ NonZeroBigDecimal, Prices }
import zio.test.Assertion.{ equalTo, isLeft, isLessThan, isNone, isSome }
import zio.test.*

object MonoPriceTest extends DefaultRunnableSpec {

  private val ARS = Prices.ofCurrency(Currency.ARS)
  private val EUR = Prices.ofCurrency(Currency.EUR)

  private val operationsSuite =
    suite("operations")(
      test("summing") {
        val actualPrice   = ARS * 100 + ARS * 20
        val expectedPrice = ARS * 120
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("subtracting") {
        val actualPrice   = ARS * 100 - ARS * 120
        val expectedPrice = ARS * -20
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("unary minus") {
        val actualPrice   = -(ARS * 100)
        val expectedPrice = ARS * -100
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("multiplying") {
        val actualPrice   = (ARS * 100) * 0.3
        val expectedPrice = ARS * 30
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("multiplying negative") {
        val actualPrice   = (ARS * 100) * -0.3
        val expectedPrice = ARS * -30
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("divide") {
        val actualPrice   = (ARS * 100) / NonZeroBigDecimal(2).get
        val expectedPrice = ARS * 50
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("divide negative") {
        val actualPrice   = (ARS * 100) / NonZeroBigDecimal(-2).get
        val expectedPrice = ARS * -50
        assert(actualPrice)(equalTo(expectedPrice))
      },
      test("ordering") {
        val min = ARS * 100
        val max = ARS * 200
        assert(min)(isLessThan(max))
      },
      test("ordering negative") {
        val max = ARS * -100
        val min = ARS * -200
        assert(min)(isLessThan(max))
      },
    )

  private val toStringSuite =
    suite("to string")(
      test("ars") {
        assert((ARS * 100).toString)(equalTo("ARS 100")) &&
        assert((ARS * -100).toString)(equalTo("ARS -100"))
      },
      test("eur") {
        assert((EUR * 100).toString)(equalTo("EUR 100")) &&
        assert((EUR * -100).toString)(equalTo("EUR -100"))
      },
    )

  private val fromStringSuite =
    suite("from string")(
      test("simple") {
        assert(ARS.fromString("ARS -100").get)(equalTo(ARS * -100))
      },
      test("extra spaces") {
        assert(ARS.fromString("   ARS     100     ").get)(equalTo(ARS * 100))
      },
      test("invalid") {
        val invalidPriceString = "ARS - 10"
        val result             = ARS.fromString(invalidPriceString)
        assert(result)(isNone)
      },
    )

  private val toStringIdentitySuite = {

    def id = (price: ARS.Price) => ARS.fromString(price.toString).get

    suite("to string andThen from string identity")( // this is not true the other way around !
      testM("single") {
        check(Gen.bigDecimal(-10, 10)) { amount =>
          val ars = ARS * amount
          assert(id(ars))(equalTo(ars))
        }
      },
      testM("complex") {
        check(Gen.bigDecimal(-10, 10), Gen.bigDecimal(-10, 10), Gen.bigDecimal(-10, 10), Gen.bigDecimal(-10, 10)) {
          (ars1amount, ars2amount, ars3amount, ars4amount) =>
            val ars1 = ARS * ars1amount
            val ars2 = ARS * ars2amount
            val ars3 = ARS * ars3amount
            val ars4 = ARS * ars4amount
            assert(id(ars1 - ars3 + ars2 + ars4))(equalTo(ars1 - ars3 + ars2 + ars4))
        }
      },
    )
  }

  override def spec =
    suite("mono price")(
      operationsSuite,
      toStringSuite,
      fromStringSuite,
      toStringIdentitySuite,
    )

}
