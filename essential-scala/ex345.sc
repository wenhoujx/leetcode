case class Cat(color: String, food: String)

case class Counter(count: Int = 0) {
  def inc = copy(count + 1)
  def dec = copy(count - 1)
}

println(Counter().inc)
