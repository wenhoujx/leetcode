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

They describe a single operation on a type, not the type itself. e.g. int addition forms a monoid, but int subtraction does not b/c it violates the identity and  associative law.

In scala, a monoid for a type `A` is:

```scala
trait Monoid[A] {
    def combine(x: A, y: A): A
    def empty: A
}

// and satisfy the following laws:
// associativity
def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = 
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
// idenetity 
def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = 
    (m.combine(x, m.empty) == x) && (m.combine(m.empty, x) == x)

```

semi-group is a monoid without the `empty` method. e.g. positive int addition forms a semi-group.

```scala
trait Semigroup[A] {
    def combine(x: A, y: A): A
}

// and follow the associative law
def associativeLaw[A](x: A, y: A, z: A)(implicit m: Semigroup[A]): Boolean = 
    m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
```

In cats:

```scala
import cats.Monoid
import cats.Semigroup
import cats.syntax.semigroup._ // for |+|
import cats.instances.string._ 

val stringResult = Monoid[String].combine("Hi ", "there")
// or 
val stringResult = "Hi " |+| "there" // |+| is the alias for combine

```

commutative replicated data types (CRDTs), Monoids in distributive systems and big data map reduce, woah.

## fuctors

`List`, `Option`, `Either`, `Future`, `fucntion`, anything can be `map`ped.

```scala
trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
}
```

`F[_]` type constructor and higher kinded type

`List` is a type constructor, given a type, produces a type, e.g. `List[Int]` is a type.

Functions are value constructors, given a value, produces a value, e.g. `Int => Int` is a value.

Declare type constructor with `[_]`, e.g. `trait Functor[F[_]]`.

Functor has `lift` method:

```scala
def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)
```

declare a method that operates on functor, note the implicit functor serves as a constraint:

```scala
def doMath[F[_]](start: F[Int])
    (implicit functor: Functor[F]): F[Int] =
  start.map(n => n + 1 * 2)

import cats.instances.option._ // for Functor
import cats.instances.list._   // for Functor

doMath(Option(20)) // Some(22)
doMath(List(1, 2, 3))

```

The following is the map implementation for `map`.

```scala
implicit class FunctorOps[F[_], A](src: F[A]) {
  def map[B](func: A => B)
      (implicit functor: Functor[F]): F[B] =
    functor.map(src)(func)
}
```

The more interesting thing is it tells us how to define a functor, by providing the `map` method, e.g. for `Option`:

```scala
implicit val optionFunctor: Functor[Option] =
  new Functor[Option] {
    def map[A, B](value: Option[A])(func: A => B): Option[B] =
      value.map(func)
  }
```

### contramap

`contramap`, take a function, prepend it to `map` operation. Only applicable to a transformation, e.g. `Printable[A] A => String`, not a container, e.g. `List[A]`. These functors are called `contravariant functors`.

This contramap process can be viewed as lifting a function `A => B` to a function `F[B] => F[A]`, hence the name.

```scala
trait ContravariantFunctor[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}
```

### imap

`imap` is a combination of `map` and `contramap`, it's a functor that can transform both input and output.

we know `String` is a monoid, we can use imap to make a class `Symbol` monoid as well:

```scala
import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.invariant._ // for imap
import cats.syntax.semigroup._ // for |+|

implicit val symbolMonoid: Monoid[Symbol] =
  Monoid[String]
    .imap(Symbol.apply) // string => symbol
      (_.name) // symbol => string

Monoid[Symbol].empty

Symbol("a") |+| Symbol("few") |+| Symbol("words")

```

## misc

### why scala allow () for apply and ignore paren entirely?

I used to think ignore paren, and implicit apply was inconsistent, but it's quite nice to write  `Monoid[Int].combine(1, 2)` instead of of `Monoid.apply[Int]()(1,2)`.

### implement memoization

```scala
def memoize[A, B](f: A => B): A => B = new scala.collection.mutable.HashMap[A, B]() {
    override def apply(key: A): B = getOrElseUpdate(key, f(key))
}
```

- function signature is obvious, what's interesting is HashMap is a function via `apply`. the `map(key)` is not just a syntactic sugar for `map.apply(key)`, it's actually a method call.
- the code creates an instance of subclass of HashMap, and override the `apply` method. Same in java.

### define recursive function as a val or var

Use `lazy` keyword, so it's not evaluated until it's called.

```scala
lazy val f: ((Int,Int)) => Int = memoize {
    case (1, n) => 1 
    case (m,1) => 1 
    case (m,n) => f(m, n-1) + f(m-1, n)
}
```
