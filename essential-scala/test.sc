object Test2 {
  def name = "best object ever"
}
println(Test2.name)

object Test3 {
  def hello(name: String) = s"Hello $name"
}

println(Test3.hello("Scala"))

object Test4 {
  val name = "Scala"
  def hello(other: String) = s"$name Hello $other"
}

println(Test4.hello("World"))

println("------------------")
object Test7 {
  val name = {
    println("this is a field")
    "Scala"
  }
  def hello(other: String) = {
    println("this is a method")
    s"$name Hello $other"
  }
}

println(Test7.hello("World"))
println(Test7.hello("World2"))

println("------------------")
object Oswald {
  val color = "Black"
  val food = "Milk"
}
object Henderson {
  val color = "Ginger"
  val food = "Chips"
}
object Quentin {
  val color = "Tabby and white"
  val food = "Curry"
}
println(s"Oswald: ${Oswald.color}, ${Oswald.food}")
println(s"Henderson: ${Henderson.color}, ${Henderson.food}")
println(s"Quentin: ${Quentin.color}, ${Quentin.food}")
println("------------------")
object calc {
  def square(n: Double): Double = n * n
  def cube(n: Double): Double = n * square(n)
}
println(calc.square(2))
println(calc.cube(2))
println("------------------")
object calc2 {
  def square(n: Double): Double = n * n
  def cube(n: Double): Double = n * square(n)
  def square(n: Int): Int = n * n
  def cube(n: Int): Int = n * square(n)
}
println(s"calc2.square(2.0): ${calc2.square(2.0)}")
println(s"calc2.cube(2.0): ${calc2.cube(2.0)}")
println(s"calc2.square(2): ${calc2.square(2)}")
println(s"calc2.cube(2): ${calc2.cube(2)}")

// println("------------------")
object person {
  val firstName = "wen"
  val lastName = "hou"
}

object alien {
  def greet(p: person.type) = {
    println(s"hello ${p.firstName}")
  }
}
alien.greet(person)
println("------------------")
class Cat(val name: String, val color: String, val food: String)
val oswald = new Cat("Oswald", "Black", "Milk")
val henderson = new Cat("Henderson", "Ginger", "Chips")
val quentin = new Cat("Quentin", "Tabby and white", "Curry")
println(s"Oswald: ${Oswald.color}, ${Oswald.food}")
println(s"Henderson: ${Henderson.color}, ${Henderson.food}")
println(s"Quentin: ${Quentin.color}, ${Quentin.food}")

object chipShop {
  def willServe(cat: Cat): Boolean = cat.food == "Chips"
}
println(s"serve Oswald ${chipShop.willServe(oswald)}")
println(s"serve Henderson ${chipShop.willServe(henderson)}")

println("------------------")
class Director(
    val firstName: String,
    val lastName: String,
    val yearOfBirth: Int
) {
  def name = s"$firstName $lastName"
}
class Film(
    val name: String,
    val yearOfRelease: Int,
    val imdbRating: Double,
    val director: Director
) {
  def directorsAge = yearOfRelease - director.yearOfBirth
  def isDirectedBy(d: Director) = d.name == director.name
  def copy(
      name: String = this.name,
      yearOfRelease: Int = this.yearOfRelease,
      imdbRating: Double = this.imdbRating,
      director: Director = this.director
  ) = new Film(name, yearOfRelease, imdbRating, director)
}
val eastwood = new Director("Clint", "Eastwood", 1930)
val mcTiernan = new Director("John", "McTiernan", 1951)
val nolan = new Director("Christopher", "Nolan", 1970)
val someBody = new Director("Just", "Some Body", 1990)

val memento = new Film("Memento", 2000, 8.5, nolan)
val darkKnight = new Film("Dark Knight", 2008, 9.0, nolan)
val inception = new Film("Inception", 2010, 8.8, nolan)

val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7, eastwood)
val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9, eastwood)
val unforgiven = new Film("Unforgiven", 1992, 8.3, eastwood)
val granTorino = new Film("Gran Torino", 2008, 8.2, eastwood)
val invictus = new Film("Invictus", 2009, 7.4, eastwood)

val predator = new Film("Predator", 1987, 7.9, mcTiernan)
val dieHard = new Film("Die Hard", 1988, 8.3, mcTiernan)
val huntForRedOctober =
  new Film("The Hunt for Red October", 1990, 7.6, mcTiernan)
val thomasCrownAffair =
  new Film("The Thomas Crown Affair", 1999, 6.8, mcTiernan)

println("eastwood yearOfBirth: " + eastwood.yearOfBirth)
// res16: Int = 1930

println(s"diehard director name ${dieHard.director.name}")
// res17: String = John McTiernan

println(s"invictus.isDirectedBy(nolan) ${invictus.isDirectedBy(nolan)}")
// false

println("------------------")
class Adder(amount: Int) {
  def add(in: Int) = in + amount
}
class Counter(val count: Int = 0) {
  def inc: Counter = inc()
  def dec: Counter = dec()
  def inc(amount: Int = 1) = new Counter(count + amount)
  def dec(amount: Int = 1) = new Counter(count - amount)
  def adjust(adder: Adder) = new Counter(adder.add(count))
}
println(
  s"new Counter(10).inc.dec.inc.inc.count, ${new Counter(10).inc.dec.inc.inc.count}"
)
println(
  s"new Counter(10).inc.inc(10).count ${new Counter(10).inc.inc(10).count}"
)
// println("------------------")
// println("------------------")
// println("------------------")
// println("------------------")
