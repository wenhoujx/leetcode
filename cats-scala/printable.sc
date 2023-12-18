trait Printable[A] {
  def format(value: A): String
}
implicit val PrintableString: Printable[String] = new Printable[String] {
  override def format(value: String): String = value
}
implicit val PrintableInt: Printable[Int] = new Printable[Int] {
  override def format(value: Int): String = value.toString()
}

object Printable {
  def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)
  def print[A](value: A)(implicit p: Printable[A]): Unit = println(
    p.format(value)
  )
}

implicit class PrintableOp[A](value: A) {
  def print(implicit p: Printable[A]) = {
    p.format(value)
  }
}

println(Printable.format(10))
println(Printable.format("11"))
Printable.print(10)
Printable.print("11")

println(10.print)
println("11".print)
