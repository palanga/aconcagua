package aconcagua.price.mono

import scala.math.BigDecimal

opaque type NonZeroBigDecimal = BigDecimal
object NonZeroBigDecimal:
  def apply(input: BigDecimal): Option[NonZeroBigDecimal]  = if input == 0 then None else Some(input)
  extension (self: NonZeroBigDecimal) def self: BigDecimal = self
