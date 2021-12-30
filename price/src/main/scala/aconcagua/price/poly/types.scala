package aconcagua.price.poly

import aconcagua.price.poly

type Amount = BigDecimal
type Rates  = Map[(Currency, Currency), BigDecimal]

object Rates:
  val identity: Rates =
    (for { from <- Currency.all; to <- Currency.all } yield from -> to).map(_ -> (BigDecimal exact 1)).toMap

private type NonEmptyList[T] = ::[T]

class ParseError(input: String)
    extends Exception(s"Couldn't parse <<$input>> as a price. It should be formatted like: ARS 107 + EUR -13")
