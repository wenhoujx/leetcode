case class Cat(name: String, color: String, food: String)

object ChipShop {
  def willServe: Cat => Boolean = {
    case Cat(_, _, "Chips") => true
    case Cat(_, _, _)       => false
  }
}

println(ChipShop.willServe(Cat("lucky", "orange", "Chips")))
println(ChipShop.willServe(Cat("combo", "orange", "Treats")))
