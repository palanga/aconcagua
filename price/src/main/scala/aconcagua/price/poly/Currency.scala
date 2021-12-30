package aconcagua.price.poly

import aconcagua.price.poly.Price.SinglePrice

enum Currency extends Ordered[Currency]:
  case ARS, EUR, USD
  def *(amount: Amount): SinglePrice        = SinglePrice(amount, this)
  override def compare(that: Currency): Int = this.toString compare that.toString

object Currency:
  def fromStringUnsafe(currency: String): Currency =
    currency match {
      case "ARS" => ARS
      case "EUR" => EUR
      case "USD" => USD
    }

  val all: List[Currency] = ARS :: EUR :: USD :: Nil
