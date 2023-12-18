class Person(val firstName: String, val lastName: String) 
object Person {
    def apply(fullName:String) = {
        val splits = fullName.split(" ")
        new Person(firstName = splits(0), lastName = splits(1))
    }
}
val p = Person("wen hou")
println(s"firstName: ${p.firstName}, lastName: ${p.lastName}")
