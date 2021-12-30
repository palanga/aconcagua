package aconcagua.price.poly

import aconcagua.price.poly.Price.SinglePrice

// TODO remove exponential complexity in CompoundPrice operations
// TODO use a real NonEmptyList

sealed trait Price extends Product with Serializable {
  def +(that: Price): Price
  def *(factor: BigDecimal): Price
  def in(currency: Currency): Rates => Option[SinglePrice]
  def -(that: Price): Price               = this + that.changeSign
  def unary_- : Price                     = this.changeSign
  def inARS: Rates => Option[SinglePrice] = in(Currency.ARS)
  def inEUR: Rates => Option[SinglePrice] = in(Currency.EUR)
  protected def changeSign: Price
}

object Price {

  /** Build a price from a string with the format: ARS 107 + EUR -13
    *
    * In Scala code you can build a price with the * operator like: ARS * 107 + EUR * -13
    */
  def fromString(s: String): Either[ParseError, Price] =
    import aconcagua.std.list.syntax.*
    s
      .split('+')
      .map(singlePriceFromString)
      .toList
      .sequence
      .map(_.reduce[Price](_ + _))
      .toRight(ParseError(s))

  /** Build a price from a string with the format: ARS 107 + EUR -13
    *
    * In Scala code you can build a price with the * operator like: ARS * 107 + EUR * -13
    */
  def fromStringUnsafe(s: String): Price =
    s.split('+').map(singlePriceFromStringUnsafe).reduce[Price](_ + _)

  private def singlePriceFromString(input: String) =
    tokenize(input) match {
      case currency :: amount :: Nil => Some(SinglePrice(BigDecimal(amount), Currency fromStringUnsafe currency))
      case _                         => None
    }

  private def singlePriceFromStringUnsafe(input: String) =
    tokenize(input) match {
      case currency :: amount :: Nil => SinglePrice(BigDecimal(amount), Currency fromStringUnsafe currency)
      case _                         => throw ParseError(input)
    }

  private def tokenize(input: String) = input.split(' ').toList.filterNot(_.isBlank)

  case object Zero extends Price {
    override def +(that: Price): Price                                = that
    override def *(factor: BigDecimal): Price                         = this
    override def in(currency: Currency): Rates => Option[SinglePrice] = _ => Some(SinglePrice(0, currency))
    override protected def changeSign: Price                          = this
  }

  case class SinglePrice(
    amount: Amount,
    currency: Currency,
  ) extends Price
      with Ordered[SinglePrice] {

    override def compare(that: SinglePrice): Int = this.amount compare that.amount

    override def +(that: Price): Price =
      that match {
        case SinglePrice(amount, currency) if this.currency == currency => copy(this.amount + amount)
        case that: SinglePrice                                          => CompoundPrice(::(this, that :: Nil))
        case _                                                          => that + this
      }

    override def *(factor: BigDecimal): Price = copy(this.amount * factor)

    override def in(currency: Currency): Rates => Option[SinglePrice] =
      rates => rates.get(this.currency -> currency).map(rate => SinglePrice(amount * rate, currency))

    override protected def changeSign: Price = copy(-this.amount)

    override def toString: String = s"$currency $amount"

  }

  case class CompoundPrice(
    prices: NonEmptyList[SinglePrice]
  ) extends Price {

    override def +(that: Price): Price =
      that match {
        case Zero                  => this
        case that: SinglePrice     => CompoundPrice(::(that, prices))
        case CompoundPrice(prices) => CompoundPrice(::(this.prices.head, this.prices.tail concat prices))
      }

    override def *(factor: BigDecimal): Price = copy(prices.map(_ * factor).asInstanceOf[::[SinglePrice]])

    override def in(currency: Currency): Rates => Option[SinglePrice] =
      import aconcagua.std.list.syntax.sequence
      rates =>
        prices
          .map(_.in(currency)(rates))
          .sequence
          .map(_.map(_.amount).sum)
          .map(SinglePrice(_, currency))

    override protected def changeSign: Price = copy(prices.map(_ * -1).asInstanceOf[::[SinglePrice]])

    override def equals(that: Any): Boolean =
      that match {
        case CompoundPrice(thatPrices) =>
          this.prices.groupBy(_.currency).view.mapValues(_.map(_.amount).sum).toList ==
            thatPrices.groupBy(_.currency).view.mapValues(_.map(_.amount).sum).toList
        case _                         => false
      }

    override def toString: String =
      this.prices
        .groupBy(_.currency)
        .view
        .mapValues(_.map(_.amount).sum)
        .toList
        .sortBy(_._1)
        .map { case (currency, amount) => s"$currency $amount" }
        .mkString(" + ")

  }

}
