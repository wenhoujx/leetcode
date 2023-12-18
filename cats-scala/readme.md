# note of scala with cats

[book](https://www.scalawithcats.com/dist/scala-with-cats)

## existing class extension

imagine a json class defined below:

```scala
sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
final case object JsNull extends Json
```

You want to create methods like `toJson` for generic type `A`, there are two ways of doing this.

### the OO way: add method to supported class

Note this is impossible in java. In fact, there is only one common way of extending existing class, that's the visitor pattern.

But with scala implicit magic, you can do this via `interface syntax`.

```scala
trait JsonWriter[A] {
    def write(value: A): Json
}

object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
        def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
    }
}
```

Whenever you call `foo.toJson` on a `foo` object of type `Foo`, the complier looks for an implicit `JsonWriter[Foo]` in the implicit scope.

Note so far we only wrote wiring logics, let's implement some actual `JsonWriter`:

```scala
final case class Person(name: String, email: String)

object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
        def write(value: String): Json = JsString(value)
    }

    implicit val numberWriter: JsonWriter[Double] = new JsonWriter[Double] {
        def write(value: Double): Json = JsNumber(value)
    }

    implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
        def write(value: Person): Json = JsObject(Map(
            "name" -> JsString(value.name),
            "email" -> JsString(value.email)
        ))
    }
}
```

btw, this is how scala adds all those magic methods on java class, e.g. `String.foldLeft` etc.

### the FP way, use a companion object

If you don't like adding methods to existing class, you can always create a companion object. In java this would be a final class with only static methods.

```scala
object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

Json.toJson(Person("Dave", "dave@gmail.com")) 
```

Here again, the compiler looks for an implicit `JsonWriter[Person]` in the implicit scope, to fill in the second argument of the curried `toJson` method.

I prefer the latter, since it keep the original class, e.g. `Person`, a pure data or value class, without any method or behavior. but you can also argue I lose the dot autocompletion.

### recursive implicit resolution

What if now you want to add `JsonWriter` for `Option[Int]`, `Option[Person]` etc.?

Of course you don't have to add them one by one, you can use the `JsonWriter[A]` for `A` to create `JsonWriter[Option[A]]` for `Option[A]` via `implicit def`.

```scala
implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = new JsonWriter[Option[A]] {
    def write(option: Option[A]): Json = option match {
        case Some(aValue) => writer.write(aValue)
        case None => JsNull // if None, return JsNull 
    }
}
```

### cats utils

Since most of the logic of wiring up implicit is the same, cats provides a bunch of utils to help you.

for example, if you want to add a show method on `Person`, you can use `cats.Show`:

```scala
import cats.syntax.show._ // for show
// then you define a method to show date 
implicit val dateShow: Show[Date] =
  Show.show(date => s"${date.getTime}ms since the epoch.")
// finally you can call 
new Date().show
```

same for `cats.Eq` and other type classes.

- Note you can't use `Some[Int]` as `Option[Int]`, b/c they are considered two diff types, two different containers. You need to do `Some(1): Option[Int]`, then use it as `Option[Int]`. Or use smart constructors like `Option.apply(1)` `Option(1)`, `Option.empty, some, none` etc.

## covariance, contravariance, invariance

### covariance

scala `List[+A]` is covariant, meaning `List[Cat]` can be used in place asking for `List[Animal]`. Note this is b/c

1. `Cat` is a subtype of `Animal`
2. `List` is immutable, so you can't add a `Dog` to a `List[Cat]` anyway

### invariance

however, `ListBuffer` is invariant, meaning `ListBuffer[Cat]` can't be used in place asking for `ListBuffer[Animal]`. This is b/c `ListBuffer` is mutable, so you can add a `Dog` to a `ListBuffer[Cat]` if it's covariant.

```scala
val cats = ListBuffer.empty[Cat]
val animals: ListBuffer[Animal] = cats // this is illegal
animals += new Dog // now cats contains a dog
```

### contravariance

If you have a generic `Feeder[-A]` that can feed `A`, and you have a `Feeder[Animal]` class, you can use it to feed any animal or a `Cat`.

### Notes

- when we talk about various variances, we are talking about container plus the type it contains, e.g. `List[Cat]`, `ListBuffer[Animal]` etc, not the generic type `A`.
- `Function1[-A, +B]`, take `A` and produces `B`, is contravariant in `A` and covariant in `B`, meaning you can use a `Function1[Animal, Cat]` in place asking for `Function1[Cat, Animal]`.

```scala
sealed trait Animal
case class Cat(name: String) extends Animal

val fa: Function1[Animal, Cat] = animal => new Cat("foo")
val fb: Function1[Cat, Animal] = fa // this is legal
```

In the example above, you can use `fa` as a `fb` b/c `fa` can take any `Animal` and produce a `Cat`, so it can take a `Cat` and produce an `Cat`, which is an `Animal`.

- common rules are:
  - producers are covariant, e.g. List, Option
  - consumers are contravariant, e.g. Ordering: if an Ordering can order `Animal`, it can order `Cat` as well.
  - functions are contravariant in their argument types and covariant in their result types.

## monoids and semigroups

