import collection.mutable.ListBuffer
val strings: ListBuffer[String] = ListBuffer("foo", "bar")
// ListBuffer is invariant
// val objects: ListBuffer[AnyRef] = strings

sealed trait Animal
case class Cat(name: String) extends Animal

val fa: Function1[Animal, Cat] = animal => new Cat("foo")
val fb: Function1[Cat, Animal] = fa // this is legal
