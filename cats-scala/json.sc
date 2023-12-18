sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}

case class Person(name: String, age: Int)

implicit val JsonWriterPerson: JsonWriter[Person] = new JsonWriter[Person] {
  override def write(value: Person): Json = JsObject(
    Map("name" -> JsString(value.name), "age" -> JsNumber(value.age))
  )
}
implicit val JsonWriterString: JsonWriter[String] = new JsonWriter[String] {
  override def write(value: String): Json = JsString(value)
}

implicit def optionWriter[A](implicit
    jw: JsonWriter[A]
): JsonWriter[Option[A]] =
  new JsonWriter[Option[A]] {
    override def write(value: Option[A]): Json = value match {
      case None        => JsNull
      case Some(value) => jw.write(value)
    }
  }

object Jsons {
  def toJson[A](value: A)(implicit writer: JsonWriter[A]) = {
    writer.write(value)
  }
}

implicit class JsonWriteOp[A](value: A) {
  def toJson(implicit w: JsonWriter[A]) = {
    w.write(value)
  }
}

val p1 = Person(name = "wen", age = 35)
println(Jsons.toJson(p1))
println(p1.toJson)
println(Jsons.toJson(Option("hello world")))
