sealed trait Color {
  def rbg: (Int, Int, Int)
}
case object Red extends Color {
  val rbg = (255, 0, 0)
}

final case class CustomColor(rbg: (Int, Int, Int)) extends Color

sealed trait DivisionResult
case object Infinite extends DivisionResult
final case class Finite(n: Int) extends DivisionResult

object divide {
  def apply(a: Int, b: Int): DivisionResult = {
    (a, b) match {
      case (a, 0) => Infinite
      case (a, b) => Finite(a / b)
    }
  }
}
println(divide(1, 2))
println(divide(1, 0))
