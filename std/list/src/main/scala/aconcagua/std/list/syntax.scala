package aconcagua.std.list

object syntax:
  extension [T](self: List[Option[T]])
    def sequence: Option[List[T]] = self match {
      case Nil    => Some(Nil)
      case h :: t => h flatMap (r => t.sequence map (r :: _))
    }
    def traverse: Option[List[T]] = sequence
