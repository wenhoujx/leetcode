trait Feline {
  def color: String
  def sound: String

}
trait BigCat extends Feline {
  override val sound: String = "roar"
}

case class Cat(color: String, food: String) extends Feline {
  val sound = "meow"
}
case class Tiger(color: String) extends BigCat
case class Lion(color: String, maneSize: Int) extends BigCat
case class Panther(color: String) extends BigCat

trait Shape {
  def sides: Int
  def perimeter: Double
  def area: Double
}

case class Circle(radius: Double) extends Shape {
  val sides = 0
  val perimeter = radius * 2 * math.Pi
  def area: Double = math.Pi * radius * radius
}

sealed trait Rectangular extends Shape {
  def height: Double
  def width: Double
  override val sides: Int = 4
  override val perimeter: Double = (height + width) * 2
  override val area: Double = height * width

}
case class Rectangle(height: Double, width: Double) extends Rectangular

case class Square(height: Double) extends Rectangular {
  val width = height
}
