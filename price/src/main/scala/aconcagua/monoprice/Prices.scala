package aconcagua.monoprice

import scala.math.BigDecimal

object Prices:
  def ofCurrency[C](currency: C): Prices[C] = Prices(currency)

class Prices[C](currency: C):

  def fromString(input: String): Option[Price] = tokenize(input) match {
    case AsCurrency(currency) :: AsBigDecimal(amount) :: Nil => Some(Price(amount, currency))
    case _                                                   => None
  }

  def *(amount: BigDecimal): Price = Price(amount, currency)

  case class Price private[monoprice] (amount: BigDecimal, currency: C) extends Ordered[Price]:
    def +(that: Price): Price                 = copy(amount = this.amount + that.amount)
    def -(that: Price): Price                 = copy(amount = this.amount - that.amount)
    def *(factor: BigDecimal): Price          = copy(amount = this.amount * factor)
    def /(dividend: NonZeroBigDecimal): Price = copy(amount = this.amount / dividend.self)
    def unary_- : Price                       = copy(amount = -this.amount)
    override def compare(that: Price): Int    = this.amount.compare(that.amount)

    override def toString: String = s"$currency $amount"

  private def tokenize(input: String): List[String] = input.split(' ').toList.filterNot(_.isBlank)

  private object AsCurrency:
    def unapply(input: String): Option[C] = if input == currency.toString then Some(currency) else None

private object AsBigDecimal:
  def unapply(input: String): Option[BigDecimal] =
    try Some(BigDecimal(input))
    catch { _ => None }
