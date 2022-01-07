// Basic

import scala.collection.mutable.ArrayBuffer

// var - variable
var test = true

test = false

// val - value. We can not change value.
// Expression returns Unit
val ifTest: Unit = if (test) {
  "Tested"
}

println(ifTest)

// functional literal
// Anonymous lambda function
((x: Int) => x + 1)(5)

// Defining lambda function and assigning to the value
val addOne = (x: Int) => x + 1
addOne(5)

// Defining functions
def abs(x: Int) = if (x > 0) x else -x

def sum(x: Int, y: Int) = x + y

// Arrays...
val nums = new Array[Int](10)
val str = new Array[String](10)
val a = Array("Hello", "World", 5) // Array[Any] Bad practice

// Dynamic array
val b = ArrayBuffer[Int]() // variable array
b += 1
b += (2, 3, 4)
b ++= Array(5, 6)
b ++= ArrayBuffer(7, 8)
b.trimEnd(2)
b.trimStart(2)

// Tuples
val tuple: (String, Int, Boolean) = ("Cat", 5, true)
val tuple3: Tuple3[String, Int, Boolean] = ("Cat", 10, true) // Tuple1 ... Tuple22
tuple == tuple3 // true

// Getting access to the members of the tuple
val animal = tuple._1
val weight = tuple._2
val vaccinated = tuple._3

// Another example of tuple defining
val tuple2: ((Int, String), Boolean) = 1 -> "One" -> true
val t: Tuple2[Tuple2[Int, String], Boolean] = ((1, "One"), true)
tuple2 == t // true

// Essential

class Animal {
  def printName = println("Animal")
  def eat = println("Eating")
  def eat(food: String) = println(s"Eating $food")
}

class Cow extends Animal {
  //def printMyName = println("Cow")
  override def printName  = println("Cow")
}







class Counter {
  // public | protected | private
  private [project] var value = 0 // Setting default value is mandatory

  // by default - public
  def increment = {
    value += 1
  }

  // by default - public
  def current() = value
}

val counter = new Counter()
// counter.counter - Trying to get access to the private member
counter.increment
counter.increment
counter.current() // 2

// Example of using qualifiers
package project {
  package count {
    class Counter {
      // public | protected | private
      private [project] var value = 0 // Setting default value is mandatory

      private [count] def increment = {
        value += 1
      }

      // Accessable only within current class
      private [this] def current() = value
    }

    class A {
      new Counter().value // we can get access to the value because we are inside of project package
    }
  }
}

package main {
  import project.count.Counter

  class A {
    new Counter().value // we can get access to the value because we are outside of project package
  }
}





class Counter {
  // private [this] var value = 0
  private var value = 0

  def increment = {
    value += 1
  }

  def current() = value

  // Here we use so called object private access
  // But it won't be accessible if we add qualifier in the value (see comment above)
  def <(other: Counter) = value < other.value
}

val c1 = new Counter()
c1.increment
c1.current() // 1

val c2 = new Counter()
c2.current() // 0

// Pointless style
c1 < c2 // false



// Every class has his own main constructor
// We can specify so called parameters of main constructor:
// class Klass(a: Int, b: Int, ...) {}
// All that parameters will be assigned to the class as its own members automatically
// In this case we use default value
// class Counter(val v: Int = 16) {
// class Counter(var v: Int = 16) {
// class Counter(private var v: Int = 16) {
// class Counter(v: Int = 16) { <=> class Counter(val v: Int = 16) {
class Counter(v: Int = 16) {
  private var value = 0

  def increment = {
    value += 1
  }

  // Using in methods main constructor parameter automatically define this parameter as a private class member
  // private [this] val v: Int = value from the constructor's initializer list
  def printV = println(v)

  def current() = value

  def <(other: Counter) = value < other.value

  // One of the additional constructor
  def this(value: Int) = {
    this() // It calls main constructor
    this.value = value
  }

  def this(value1: Int, value2: Int) = {
    this(value1 + value2) // It calls one of the additional constructor
  }

  // It's triggered by calling main constructor!!!
  // This kind of behaviour is similar to Ruby classes
  println(s"Current value = $value")
}

val c1 = new Counter(3)
c1.current() // 3

val c2 = new Counter(5, 6)
c2.current() // 11

//

// Private constructor
class Counter private () {
  private var value = 0

  def increment = {
    value += 1
  }

  def current() = value
}

// val c1 = new Counter()






import scala.collection.mutable.ArrayBuffer

class Network {
  // Inner class
  class Member(name: String) {
    val contacts = new ArrayBuffer[Member]()
  }

  private val members = new ArrayBuffer[Member]()

  def join(name: String) = {
    val m = new Member(name)
    members += m
    m
  }
}

val n1 = new Network
val n2 = new Network

new n2.Member("Bob")

val bob: n1.Member = n1.join("Bob")
val sara: n1.Member = n1.join("Sara")

bob.contacts += sara
bob.contacts

val nick = n2.join("Nick")
// We can not do that cause there are two different types (n1.Member and n2.Member)
// bob.contacts += nick







// Case classes
// Every constructor parameter becomes val value unless explicitly declared as var
// A companion object is created with a method "apply" to be able to create objects without "new"
// A method "unaplly" is given for pattern matching
// Methods "toString", "equals", "hashCode", "copy" are automatically given unless explicitly declared
case class Cat(name: String, age: Int) // Wow, it's short :)
val cat = Cat("Tomas", 3)
println(cat) // Cat(Tomas,3) - toString in action

// The principle of comparing instances is the same as in Java

val cat1 = Cat("Pipsy", 3)
val cat2 = Cat("Pipsy", 3)
val cat3 = cat2.copy() // It creates a new instance with all copied values
val cat4 = cat2.copy(name = "Pupsy")
cat1 == cat2 // true (it compares contents of two instances)
cat2 == cat3 // true
cat2 == cat4 // false

class Dog(name: String, age: Int)
val dog1 = new Dog("Pigy", 2)
val dog2 = new Dog("Pigy", 2)
dog1 == dog2 // false (it compares addresses of two instances)





// Objects

class Counter {
  protected var count = 0

  def newCount = {
    count += 1
    count
  }
}

// Singleton object
object ChildCounter extends Counter {
  def incr = {
    count += 1
    count
  }

  println("Constructor")
}

ChildCounter.incr     // Constructor   1
ChildCounter.newCount //               2
ChildCounter.incr     //               3

// Objects can not use constructor's parameters
// object Settings(path: String) {}

// The companion object is analogous to a static object

object Counter {
  private var value = 0

  def incr = {
    value += 1
    value
  }
}

class Counter {
  val id = Counter.incr
}

val c1 = new Counter
val c2 = new Counter

c1.id // 1
c2.id // 2

// We can use companion object in case classes

object Counter {
  private var value = 0

  def incr = {
    value += 1
    value
  }

  // This method is accessible inside case class with the exact constructor name
  private def incr2 = {
    value += 2
    value
  }
}

case class Counter() {
  val id = Counter.incr2 // Access to the private method of the companion object
}

val cc1 = Counter()
val cc2 = Counter()

cc1.id // 1
cc2.id // 2

// We can use companion object in case classes

object Counter {
  private var value = 0

  def incr = {
    value += 1
    value
  }

  // This method is accessible inside case class with the exact constructor name
  private def incr2 = {
    value += 2
    value
  }
}

case class Counter() {
  val id = Counter.incr2 // Access to the private method of the companion object
}

val cc1 = Counter()
val cc2 = Counter()

cc1.id // 1
cc2.id // 2

abstract class UndoableAction(val description: String) {
  def undo(): Unit
  def redo(): Unit
}

// ******** Object with class should be in the same file
object DoNothing extends UndoableAction(description = "Do nothing") {
  override def undo(): Unit = {}
  override def redo(): Unit = {}

  def apply(description: String): DoNothing = new DoNothing(description)
}

// Do not put here any code!!!
// Otherwise we will get error: not found: type DoNothing

class DoNothing(description: String) {}
// *********

// Short forms of instantiation (take your attention to the apply method)
DoNothing("Do nothing from apply") // DoNothing@19031b73

// Short forms of instantiations (except of using new Array)
Array("1", "2", "3")
Array.apply("1", "2", "3")
Array(Array(1, 2), Array(3, 4))

val action = Map(
  "open" -> DoNothing,
  "save" -> DoNothing
)

// Application object creation

object Hello {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
  }
}






object Hello extends App {
  println("Hello, world")
}






// args are accessible because we extends App trait
object Hello extends App {
  if (args.length > 0) println(s"Hello, ${args(0)}!") else println("Hello, world!")
}







import scala.io.StdIn

object Hello extends App {
  val name = StdIn.readLine("Your name: ")

  println(s"Hello, $name!")
}

// Enumerations

//object TrafficLights extends Enumeration {
//  val Red, Yellow, Green = Value
//}

object TrafficLights extends Enumeration {
  val Red = Value("Super Red")              // id -> 0
  val Yellow = Value(10, "Super Yellow")    // id -> 10
  val Green = Value("Super Green")          // id -> 11
}

TrafficLights.Green   // Super Green
TrafficLights.Yellow  // Super Yellow
TrafficLights.Red     // Super Red

TrafficLights.Green.id  // 11
TrafficLights.Yellow.id // 10
TrafficLights.Red.id    // 0

def onTheRoad(color: TrafficLights.Value): String = {
  import TrafficLights._ // imports all fields

  /* if (color == Green) "go"
  else if (color == Yellow) "attention"
  else "stop" */

  // The pattern matching is more beautiful than using if-else expressions
  color match {
    case Green => "go"
    case Yellow => "attention"
    case Red => "stop"
    case _ => "something went wrong" // to prevent error: scala.MatchError
  }
}

onTheRoad(TrafficLights.Red) // stop

for (c <- TrafficLights.values) println(c.id + ": " + c) // 0: Super Red
                                                         // 10: Super Yellow
                                                         // 11: Super Green

TrafficLights(0)                     // Super Red
TrafficLights.withName("Super Red")  // Super Red

// Overriding a parameterless method in a parametric field

abstract class Element {
  def contents: Array[String]
}

// bad (with the smell of code)
class ArrayElement(conts: Array[String]) extends Element {
  val contents: Array[String] = conts
}

// good
class ArrayElement(val contents: Array[String]) extends Element

// Another example
class Cat {
  val dangerous = false
}

// with the smell of code
class Tiger(p1: Boolean, p2: Int) extends Cat {
  override val dangerous = p1
  private val age = p2
}

class Tiger (override val dangerous: Boolean, private val age: Int) extends Cat








// There are methods without parameters.
// It is recommended to use methods without parameters in case of
// methods without side effects

abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length
}

class ArrayElement(conts: Array[String]) extends Element {
  override def contents: Array[String] = conts
}

val arrayElement = new ArrayElement(Array("hello", "world"))
arrayElement.height // 2
arrayElement.width // 5

val el: Element = new ArrayElement(Array("hello", "world"))








abstract class Element {
  def contents: Array[String]
  val height: Int = contents.length
  val width: Int = if (height == 0) 0 else contents(0).length

  println(s"Element height: $height, width: $width")
}

class ArrayElement(conts: Array[String]) extends Element {
  override def contents: Array[String] = conts

  println(s"ArrayElement height: $height, width: $width")
}

val arrayElement = new ArrayElement(Array("hello", "world"))
arrayElement.height // 2
arrayElement.width // 5

val el: Element = new ArrayElement(Array("hello", "world"))

// Here we use parent constructor for passing parameter in array as parent's API
// Also we can not define methods - override def ... - because in parent class
// there are also <val>s but otherwise you can (def in parent -> val in child)
class LineElement(s: String) extends ArrayElement(Array(s)) {
  override val width = s.length
  override val height = 1

  println(s"LineElement height: $height, width: $width")
}

val le = new LineElement("LineElement")
le.height // 1
le.width  // 11

// We can omit override cause in parent there is abstract method
class UniformElement(
                      ch: Char,
                      override val width: Int,
                      override val height: Int
                    ) extends Element {
  private val line = ch.toString * width

  def contents: Array[String] = Array.fill(height)(line)

  println(s"UniformElement height: $height, width: $width")
}

/*
  Element height: 1, width: 3
  ArrayElement height: 1, width: 3
  val el1: Element = ArrayElement@6b4d2032
  Element height: 0, width: 0
  ArrayElement height: 0, width: 0
  LineElement height: 1, width: 3
  val ae1: ArrayElement = LineElement@55256b38
  val el2: Element = LineElement@55256b38
*/

// Polymorphism in action
val el1: Element = new ArrayElement(Array("123"))
val ae1: ArrayElement = new LineElement("123")
val el2: Element = ae1
// Here we will get exception java.lang.NullPointerException
// Overriding an immutable value in an abstract class is dangerous
// => overriding def in an abstract class by val parameter in child class is ok
// but overriding val in an abstract class by val parameter in child class is not
val el3: Element = new UniformElement('*', 2, 5)







// Dynamic binding

abstract class E {
  def demo() = println(s"E invoked")
}

class AE extends E {
  override def demo(): Unit = println("AE invoked")
}

class LE extends AE {
  override def demo(): Unit = println("LE invoked")
}

class UE extends E

def invokeDemo(e: E): Unit = {
  e.demo()
}

invokeDemo(new AE)  // AE invoked
invokeDemo(new LE)  // LE invoked
invokeDemo(new UE)  // E invoked







// If we need to prohibit overriding a class method, then we need to use the modifier - final
class API {
  final def version: String = "0.0.1"
}

// We can also prohibit the creation of subclasses for the class.
// As result we will get - so called - terminal class
final class API {}







// Inheriting LineElement from ArrayElement doesn't look right,
// so let's fix the inheritance scheme LineElement -> Element.
// Also let's add to class LineElement a composition relation with class ArrayElement
class LineElement(s: String) extends Element {
  override val width = s.length
  override val height = 1

  val contents = Array(s)
}








// src/main/scala/composition/Element.scala
package composition

abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length

  def above(that: Element): Element =
    new ArrayElement(this.contents ++ that.contents)

  def beside(that: Element): Element =
    new ArrayElement(
      for (
        (line1, line2) <- this.contents zip that.contents
      ) yield line1 + line2
    )

  override def toString: String = contents mkString("\n")
}

// src/main/scala/composition/ArrayElement.scala
package composition

class ArrayElement(conts: Array[String]) extends Element {
  override def contents: Array[String] = conts
}

// src/main/scala/composition/Composition.scala
package composition

object Composition extends App {
  val column1 = new ArrayElement(Array("hello")) above new ArrayElement(Array("***"))
  val column2 = new ArrayElement(Array("***")) above new ArrayElement(Array("world"))
  println(column1 beside column2)
}

/*
  hello***
  ***world
*/



// Option (as a small collection)

val opt: Option[String] = Some("opt")
val optNone: Option[String] = None

val intToString = Map(1 -> "1", 2 -> "2")
intToString.get(1) // val res0: Option[String] = Some(1)
intToString.get(3) // val res1: Option[String] = None

// val res2: String = 1
intToString.get(1) match {
  case Some(v) => v
  case None => "0"
}

intToString.get(1).get // val res3: String = 1

// java.util.NoSuchElementException: None.get
// map.get(10).get

// bad style
if (intToString.get(10).isEmpty) "0" else intToString.get(10).get // val res6: String = 0

intToString.get(10).getOrElse("0") // val res7: String = 0

// Using loop for comprehension
for (v <- intToString.get(1)) println(v)  // 1
for (v <- intToString.get(10)) println(v) // empty

intToString.get(10).isDefined // false

val a1 = intToString.get(10) // None
val a2 = intToString.get(1)  // Some(1)

a1.map(v => v + " map") // None
a2.map(v => v + " map") // Some(1 map)

a2.flatMap(v => Some(v + " flatMap")) // Some(1 flatMap)

// Better to use with collections
a2.filter(v => v == "1") // Some(1)

a2.contains("1") // true
a2.exists(v => v == "2") // false

a1.foreach(println) //
a2.foreach(println) // 1

// null -> Option | Try
// No NullPointerException
// It's more safety
// No if-else expressions
// Options[T] => Some[R] | None

class User(email: String, password: String) {
  // _ <=> null
  var firstName: String = _
  var lastName: String = _
}

new User("test", "test").firstName // null
new User("test", "test").lastName // null

//
class User(email: String, password: String) {
  var firstName = None: Option[String]
  var lastName = None: Option[String]
}

val u = new User("test", "test")
u.firstName match {
  case Some(v) => v
  case None => "?"     // ?
}





class User(email: String, password: String) {
  var firstName = Some(email)
  var lastName = Some(password)
}

val u = new User("test", "test")
u.firstName.getOrElse("?") // test

// If you need to use some of old Java piece of codes ...

class User(email: String, password: String) {
  var firstName = null
  var lastName = null
}

def doSomething: Option[User] = { None }

// bad style
def getName(user: User): Option[String] = {
  val name = user.lastName
  if (name == null) None else Some(name)
}

// scala style
def getName(user: User): Option[String] = Option(user.lastName)






def upperCase(s: String) = s.toUpperCase()

val o = Option("test string")
o.fold("empty string")(upperCase) // TEST STRING

val o = None
o.fold("empty string")(upperCase) // empty string

// Exceptions

import scala.io.StdIn
import scala.math.sqrt
import scala.util.{Failure, Success, Try}

// throw new IllegalArgumentException("x should not be negative")

class MyException extends Exception("My exception")

def sqrtWithException(x: Int) =
  if (x >= 0) sqrt(x) else throw new MyException

// It's my exception
/*
// bad sinse we expect either double or Any = ()
val a: AnyVal() = try {
  sqrtWithException(-5)
} catch {
  case _: MyException => println("It's my exception")
  case ex: Exception => ex.getMessage
  case _ => println("something went wrong")
}
*/

// better since we expect concrete type
val a: Double = try {
  sqrtWithException(-5)
} catch {
  case _: MyException =>
    println("It's my exception")
    0.0
  case ex: Exception =>
    ex.getMessage
    0.0
  case _ =>
    println("something went wrong")
    0.0
}

// Try[+T] => Success[T] | Failure[T]

// Try allows us to create conveyor or chain processing
def divide: Try[Int] = {
  val numerator = Try(StdIn.readLine("Numerator:\n").toInt)
  val denominator = Try(StdIn.readLine("Numerator:\n").toInt)
  val result = numerator.flatMap(x => denominator.map(y => x / y))

  result match {
    case Success(v) =>
      println(s"Result of ${numerator.get} / ${denominator.get} is: $v")
      Success(v)
    case Failure(e) =>
      println("You must've divided by zero or entered something that's not an Int.Try again!")
      println("Info from the exception: " + e.getMessage)
      divide
  }
}

def toInt(s: String): Option[Int] = {
  try {
    Some(Integer.parseInt(s.trim))
  } catch {
    case _: Exception => None
  }
}

// Exception
toInt("qwerty") match {
  case Some(v) => println(v)
  case None => println("Exception")
}

val y: Option[Int] = for {
  a <- toInt("1")
  b <- toInt("2")
  c <- toInt("3")
} yield a + b + c  // Some(6)

def toIntTry(s: String) = Try(Integer.parseInt(s))

toIntTry("16")      // Success(16)
toIntTry("qwerty")  // Failure(java.lang.NumberFormatException: For input string: "qwerty")

// Traits

// Trait constructors cannot have parameters
// Trait can have abstract fields

trait HelloWorld {
  val name: String
  def sayHello(msg: String = "") =
    println(s"Hello, world! $msg")
}

trait TimestampHelloWorld extends HelloWorld {
  override def sayHello(msg: String): Unit =
    super.sayHello(new java.util.Date() + " " + msg)
}

trait UpperCaseHelloWorld extends HelloWorld {
  override def sayHello(msg: String = ""): Unit =
    super.sayHello(msg.toUpperCase())
}

trait HelloSea {
  def sayHelloSea = println("Hello, sea!")
}

case class B()

// ... with ... means trait mixing to the class
class A extends B with HelloWorld with HelloSea {
  override val name = "Bob"
}

new A().sayHello() // Hello, world!

val a = new A()
val helloWorld: HelloWorld = a
a.sayHelloSea

// First call -> UpperCaseHelloWorld
// Second call -> TimestampHelloWorld
// Only then calls -> C -> HelloWorld
class C extends HelloWorld with TimestampHelloWorld with UpperCaseHelloWorld {
  override val name = "Andy"
}

new C().sayHello() // Hello, world! Fri Sep 24 23:08:03 SAMT 2021
new C().sayHello("super") // Hello, world! Fri Sep 24 23:11:06 SAMT 2021 SUPER

/*
But if we do like that (take a look to the super[HelloWorld]):

trait UpperCaseHelloWorld extends HelloWorld {
  override def sayHello(msg: String = ""): Unit =
    super[HelloWorld].sayHello(msg.toUpperCase())
}

then the method call from the trait will be skipped.
The call will be addressed to the HelloWorld trait

new C().sayHello()        // Hello, world!
new C().sayHello("super") // Hello, world! SUPER
*/

// Constructor call order:
// 1. Super (Parent class).
// 2. Traits in order from left to right in the class description (with <1> with <2> ...)
// But within each trade, the parent constructor of the parent trait is called first.
// Repeatable trait constructors are called only once (common parent).
// 4. Subclass (Child class) | current class.

// We can not inherit traits with the same method names
// It that case usually we get next error message: class ... inherits conflicting members

// Traits also can inherit classes

trait LoggedException extends Exception {
  def Log = println(getMessage)
}

// Here we can not inherit classes that are not subclasses of Exception class
// because one class can inherits one class otherwise we will break the rule of
// inheritance of classes in Scala (Java). But we can inherit classes within
// one hierarchy of classes (i.e. exception classes)
class MyException extends IllegalArgumentException with LoggedException {
  getMessage
}

// Self types
// this: Type =>
// Let's have a look to the next trait definition

trait MyException extends MyTrait {
  this: Exception => def log() = getMessage()
}

// It means that that trait can be mixed ONLY to the classes
// that are subclasses (subtypes) of Exception class (type).
// Also note that this trait did not inherit the class itself.
// After mixing that trait we will be able to call methods of self typed class.

// Self types could give us flexibility
// For example, it could by usefull in the next moments:
// - Handling circular dependencies between traits
// - Defining Cyclic Structural Types

// Structural types
// Structural type - a type that defines the methods that a class should have,
// without specifying the class name

trait MyException extends MyTrait {
  this: { def getMessage(): String } =>
    def log() = getMessage()
}

// Higher-order functions

import scala.math._

val function1: Double => Double = ceil _
val function2: Double => Double = sqrt _
val number = 6.9
function1(number) // 7
Array(3.14, 8.34).map(function1)   // Array(4.0, 9.0)
Array(3.14, 8.34).map(x => x + 1) // Array(4.140000000000001, 9.34)
Array(3.14, 8.34).map(_ + 1)
Array(3.14, 8.34).map {_ + 1}
Array(3.14, 8.34).map { x =>
  x + 1
}

// Error: missing parameter type for expanded function ((x$1: <error>) => 2.<$times: error>(x$1))
// val fun = 2 * _
val fun1 = 2 * (_: Int)
val fun2: Int => Int = 2 * _

def highOrderFn1(f: Double => Double, value: Double) = f(value)

highOrderFn1(function1, 2.0) // 2.0
highOrderFn1(function2, 2) // 1.4142135623730951

def add(value: Int) = (x: Int) => value + x

val addTwoTo = add(2)
addTwoTo(5) // 7

// (Int) => (Int => Int) <=> Int => Int => Int
def highOrderFn2(f: Int => Int => Int, value: Int) = f(value)

val addTen = highOrderFn2(add, 10)
addTen(2) // 12 = 2 + 10

highOrderFn2(add, 10)(6) // 16 = 10 + 6 <=> (add 10 6) <=> (+ 10 6)

(1 to 9).map(_ + 1) // val res11: IndexedSeq[Int] = Vector(2, 3, 4, 5, 6, 7, 8, 9, 10)

(1 to 9).map("*" * _) // Vector(*, **, ***, ****, *****, ******, *******, ********, *********)

// foreach returns Unit
/*
*
**
***
****
*****
******
*******
********
*********
*/
(1 to 9).map("*" * _).foreach(println)

(1 to 9).filter(_ % 2 == 0) // Vector(2, 4, 6, 8)

// 1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9
(1 to 9).reduceLeft(_ * _) // 362880

"Learn Scala and be happy"
  .split(" ")
  .sortWith(_.length < _.length) // Array(be, and, Learn, Scala, happy)

// Closure

def seqNumberGenerator = {
  var number = 0

  // closure
  () => {
    number = number + 1
    number
  }
}

val seqNumber = seqNumberGenerator
seqNumber()  // 1
seqNumber()  // 2
seqNumber()  // 3

// List collection

/*
Hierarchy of immutable collections:

Traversable
  Iterable
    Set
      HashSet
      BitSet
      ListSet
      SortedSet
        TreeSet
    Map
      HashMap
      ListMap
      SortedMap
        TreeMap
    Seq
      IndexedSeq
        Vector
        NumericRange
        Range
        Array
        String
      LinearSet
        List
        Stream
        Queue
        Stack
*/

val fruit = List("apples", "oranges", "pears")
val nums = List(1, 2, 3, 4)
val diag3 = List(
  List(1, 0, 0),
  List(0, 1, 0),
  List(0, 0, 1)
)
val epmty = List()

// covariance:
// if S is subtype of T then List[S] is subtype of List[T]

// :: - infix operator to build lists (cons method)
// Nil - empty list

List(1, 2, 3, 4)      // List(1, 2, 3, 4)
1 :: 2 :: 3 :: Nil    // List(1, 2, 3)
(1::(2::(3::Nil)))    // List(1, 2, 3)
Nil.::(3).::(2).::(1) // List(1, 2, 3)

val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
val nums = 1 :: (2 :: (3 :: (4 :: Nil)))
val diag3 = (1 :: (0 :: (0 :: Nil))) ::
  (0 :: (1 :: (0 :: Nil))) ::
  (0 :: (0 :: (1 :: Nil))) :: Nil
val empty = Nil

val l1 = List(1, 2, 3, 4, 5)
val l2 = 1 :: 2 :: 3 :: Nil

l1.head    // 1
l1.tail    // List(2, 3, 4, 5)
l1.isEmpty // false

val emptyList1 = Nil
val emptyList2 = List()

// We can not get head or tail from an empty list:
// java.util.NoSuchElementException: head of empty list
// emptyList1.head
// emptyList1.tail

// In recursion function it requires to define returning type
def insert(x: Int, xs: List[Int]): List[Int] =
  if (xs.isEmpty || x <= xs.head) x :: xs
  else xs.head :: insert(x , xs.tail)

def sort(xs: List[Int]): List[Int] = if (xs.isEmpty) Nil else insert(xs.head, sort(xs.tail))

val l3 = 4 :: 1 :: 8 :: 0 :: Nil
sort(l3) // List(0, 1, 4, 8)

// Without _ in that case (list contains extra elements) we will get error:
// scala.MatchError: List(4, 1, 8, 0) (of class scala.collection.immutable.$colon$colon)
val List(a, b, c, _) = l3
a // 4
b // 1
c // 8

// Using pattern matching is more preferable and more readable
def insertPM(x: Int, xs: List[Int]): List[Int] = xs match {
  case Nil => List(x)
  case y :: ys => if (x <= y) x :: xs else y :: insertPM(x, ys)
}

def sortPM(xs: List[Int]): List[Int] = xs match {
  case Nil => Nil
  case z :: zs => insertPM(z, sortPM(zs))
}

sortPM(l3) // List(0, 1, 4, 8)

val xs = List(1, 2, 3)
val ys = List(4, 5, 6)
val zs = List("04", "05", "06")
xs ::: ys          // List[Int] = List(1, 2, 3, 4, 5, 6)
ys.:::(xs)         // List[Int] = List(1, 2, 3, 4, 5, 6)
// Right associativity
ys ::: xs ::: zs   // List[Any] = List(4, 5, 6, 1, 2, 3, 04, 05, 06)
ys.:::(xs).:::(zs) // List[Any] = List(04, 05, 06, 1, 2, 3, 4, 5, 6)

// Expensive
xs.length

// Be careful with empty lists (java.lang.UnsupportedOperationException: empty.init)
xs.last  // 3
xs.init  // List(1, 2)

xs.reverse // List(3, 2, 1)

xs.reverse.reverse == xs           // true
xs.reverse.init == xs.tail.reverse // true
xs.reverse.tail == xs.init.reverse // true
xs.reverse.head == xs.last         // true
xs.reverse.last == xs.head         // true

xs.take(2)  // List(1, 2)
xs.take(10) // List(1, 2, 3)

xs drop 1  // List(2, 3)
xs drop 10 // List()

xs splitAt 2 // (List(1, 2),List(3))

// (xs drop n).head
xs apply 2  // 3
xs(2)       // 3

xs.indices  // val res28: scala.collection.immutable.Range = Range 0 until 3
xs.indices.toList // List(0, 1, 2)
xs.toArray  // Array(1, 2, 3)

val arr = xs.toArray   // Array(1, 2, 3)
xs copyToArray(arr, 1) // 2
arr // Array(1, 1, 2)

val it = xs.iterator
it.next // 1
it.next // 2
// Be careful if you call length on the iterator and then call next it will raise error
// java.util.NoSuchElementException: head of empty list
it.length // 1
// it.next

// linearization of a list of lists
val xss: List[List[Int]] = List(xs, ys)
xss.flatten // List(1, 2, 3, 4, 5, 6)

// combining lists
val zipped = xs zip zs // val zipped: List[(Int, String)] = List((1,04), (2,05), (3,06))
xs.zipWithIndex // List((1,0), (2,1), (3,2))
zipped.unzip // (List(1, 2, 3),List(04, 05, 06))

xs.mkString                       // 123
xs.mkString(" | ")                // 1 | 2 | 3
xs.mkString(" | ", " * ", " | ")  // " | 1 * 2 * 3 | "

val words = List("one", "two", "three")
words.map(_.toList)         // List(List(o, n, e), List(t, w, o), List(t, h, r, e, e))
words.map(_.toList).flatten // List(o, n, e, t, w, o, t, h, r, e, e)
words.flatMap(_.toList)     // List(o, n, e, t, w, o, t, h, r, e, e)

words.partition(_.length > 3) // (List(three),List(one, two))

words.find(_ == "two") // Some(two)

// Other collections

import scala.collection.immutable.{TreeMap, TreeSet}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable

/*
Sequences:
  - List
  - Array
  - ListBuffer
  - ArrayBuffer
  - StringOps
*/

// Array is a mutable collection
val arr = new Array(3) // Array[Nothing] = Array(null, null, null)
val arrInt = new Array[Int](3) // Array[Int] = Array(0, 0, 0)
val arrString = new Array[String](3) // Array[String] = Array(null, null, null)
val arr1 = Array(3) // Array[Int] = Array(3)
arrString(1) = "Hello"
arrString // Array[String] = Array(null, Hello, null)

// ListBuffer is a mutable collection
// if you aware of stuck overflow and using not tail recursion algorithm then we can use it
val listBuffer = ListBuffer(3) // ListBuffer(3)
listBuffer += 4 // ListBuffer(3, 4)
// listBuffer.+=:(2)
2 +=: listBuffer // ListBuffer(2, 3, 4)

// ArrayBuffer seems like a array but we can add elements to the beginning or to the ending
val arrBuffer = ArrayBuffer[String]()
arrBuffer += "hello" // ArrayBuffer(hello)
arrBuffer += "world" // ArrayBuffer(hello, world)
arrBuffer -= "world" // ArrayBuffer(hello)

// Any string automatically (implicitly) transforms to the StringOps
"hello world".map(x => x + " !") // ArraySeq(h !, e !, l !, l !, o !, "  !", w !, o !, r !, l !, d !)

// By default it's an immutable set
val set = Set(1, 1) // Set(1)
val mutSet = mutable.Set(1)
// "var" with "+="  -  syntactic sugar   -    !!! Creates new collection and reassigns to it
// You can easily transform from immutable to mutable (as result it will work differently (from mutability perspective))
var setVar = Set(1)
setVar += 2

// By default it's an immutable map
val map = Map(1 -> "q")
val mutMap = mutable.Map(1 -> "q")

val str = "Hello world, my beautiful world! It is a wonderful day."
val words = str.split("[!,. ]+") // Array(Hello, world, my, beautiful, world, It, is, a, wonderful, day)
words.map(_.toLowerCase()).toSet // scala.collection.immutable.Set[String] = HashSet(beautiful, is, world, a, my, hello, day, wonderful, it)

val res = mutable.Set.empty[String]
for (word <- words) res += word.toLowerCase
res // scala.collection.mutable.Set[String] = HashSet(beautiful, a, world, is, wonderful, it, hello, my, day)

val nums = Set(1, 2, 3)
nums + 5 // Set(1, 2, 3, 5)
nums - 3 // Set(1, 2)
nums // Set(1, 2, 3)
nums ++ List(4, 6) // HashSet(1, 6, 2, 3, 4)
nums -- List(4, 6) // Set(1, 2, 3)
// Intersection
nums & Set(1, 4, 6) // Set(1)
nums.size // 3
nums.contains(1) // true

// Associative arrays

def countWords(text: String) = {
  val counts = mutable.Map.empty[String, Int]
  for (rawWord <- text.split("[!,. ]+")) {
    val word = rawWord.toLowerCase
    val oldCount = if (counts.contains(word)) counts(word) else 0
    counts += (word -> (oldCount + 1))
  }
  counts
}

countWords(str) // scala.collection.mutable.Map[String,Int] = HashMap(beautiful -> 1, a -> 1, world -> 2, is -> 1, wonderful -> 1, it -> 1, hello -> 1, my -> 1, day -> 1)

val exampleMap = countWords(str)
exampleMap + ("hi" -> 0) // scala.collection.mutable.Map[String,Int] = HashMap(beautiful -> 1, a -> 1, hi -> 0, world -> 2, is -> 1, wonderful -> 1, it -> 1, hello -> 1, my -> 1, day -> 1)
exampleMap - "is" // HashMap(beautiful -> 1, a -> 1, world -> 2, wonderful -> 1, it -> 1, hello -> 1, my -> 1, day -> 1)
exampleMap -- List("is", "world", "it") // HashMap(beautiful -> 1, a -> 1, wonderful -> 1, hello -> 1, my -> 1, day -> 1)
exampleMap ++ List("y" -> 8, "p" -> 7) // HashMap(p -> 7, beautiful -> 1, a -> 1, world -> 2, y -> 8, is -> 1, wonderful -> 1, it -> 1, hello -> 1, my -> 1, day -> 1)
exampleMap.keys // Iterable[String] = Set(beautiful, a, world, is, wonderful, it, hello, my, day)
exampleMap.isEmpty // false

// Based on black red tree
// It sort by values
val ts = TreeSet(1, 9, 5, 8, 3, 2)                     // TreeSet(1, 2, 3, 5, 8, 9)
// It sorts by keys
val tm = TreeMap(3 -> "three", 1 -> "one", 2 -> "two") // TreeMap(1 -> one, 2 -> two, 3 -> three)

// An empty mutable map in its resulting hashmap takes about 80 bytes and about 16 additional bytes are required for each added entry.
// And an empty immutable map presents as a single object that is shared by all links.
// Currently, the Scala collection library stores up to 4 records of immutable sets and maps in one object, which, depending on the number of records stored in the collection,
// usually takes from 16 to 40 bytes, which is twice as efficient in memory as mutable collections

val coll: mutable.Set[Any] = mutable.Set("one")
coll += 1 // HashSet(1, one)

val l = List("one", "two")
// val treeSet = TreeSet(l) // error because we don not have information about order form
val treeSet = TreeSet[String]() ++ l // TreeSet(one, two)
treeSet.toList
// immutable -> mutable
val mutTs = mutable.TreeSet.empty[String] ++= treeSet // mutTs: scala.collection.mutable.TreeSet[String] = TreeSet(one, two)

// Scala advanced

//
def mul(x: Int, y: Int): Int = x * y
def mulCarrying(x: Int) = (y: Int) => x * y
mulCarrying(2)(5) // (y: Int) => 2 * y // 5

val a = List("Alex", "Jon")
val b = List("alex", "jon")

a.corresponds(b)(_.equalsIgnoreCase(_)) // (a: String, b: String) => a.equalsIgnoreCase(b) // true

// Using an abstraction of control structures

/*
def runInThread(block: () => Unit): Unit = {
  new Thread {
    override def run(): Unit = { block }
  }.start()
}

runInThread { () => println("Hello, world!"); Thread.sleep(10000); println("Bye, world!") }
*/

// We can use so-called call-by-name notation to simplify
def runInThread(block: => Unit): Unit = {
  new Thread {
    override def run(): Unit = { block }
  }.start()
}

runInThread {
  println("Hello, world!")
  Thread.sleep(10000)
  println("Bye, world!")
}

// The value parameter is not calculated at the time of the function call
def until(condition: => Boolean)(block: => Unit) {
  if (!condition) {
    block
    until(condition)(block)
  }
}

var x = 10
until (x == 0) {
  x -= 1
  println(x)
}

// Partial function

val f: PartialFunction[Char, Int] = {
  case '+' => 1;
  case '-' => -1;
}

// It calls apply
f('-') // -1
f.isDefinedAt('=') // false
f.isDefinedAt('+') // true
// scala.MatchError: = (of class java.lang.Character)
// f('=')

"-3+4".collect { case '+' => 1; case '-' => -1 } // IndexedSeq[Int] = Vector(-1, 1)
List(1, 2, -2, 0, 90).collect { // List[String] = List(+1, +2, -1, 0, +90)
  case 0 => "0"
  case x if x < 0 => "-1"
  case x if x > 0 => s"+$x"
}

List(1, 2, -2, 0, 90).collect { // List[String] = List(-1, 0)
  case 0 => "0"
  case x if x < 0 => "-1"
}

val divide = new PartialFunction[Int, Int] {
  def apply(x: Int) = 42 / x
  def isDefinedAt(x: Int) = x != 0
}

// java.lang.ArithmeticException: / by zero
// divide(0)
if (divide.isDefinedAt(0)) divide(0)

val divide2: PartialFunction[Int, Int] = {
  case x if x != 0 => 42 / x
}

// scala.MatchError: 0 (of class java.lang.Integer)
// divide2(0)
if (divide2.isDefinedAt(0)) divide2(0) // val res10: AnyVal = ()

val convertOneToFive = new PartialFunction[Int, String] {
  val nums = Array("One", "Two", "Three", "Four", "Five")
  def apply(x: Int) = nums(x - 1)
  def isDefinedAt(x: Int) = x > 0 && x < 6
}

val convertSixToTen = new PartialFunction[Int, String] {
  val nums = Array("Six", "Seven", "Eight", "Nine", "Ten")
  def apply(x: Int) = nums(x - 6)
  def isDefinedAt(x: Int) = x > 0 && x < 6
}

val convertOneToTen = convertOneToFive orElse convertSixToTen

convertOneToTen(3) // Three
convertOneToTen(9) // Nine
// java.lang.ArrayIndexOutOfBoundsException: -6
// convertOneToTen(0)

// Pattern matching

// Guard clause

x match {
  case '+' => sign = 1
  case '-' => sign = -1
  // using guard clause
  case _ if Character.isDigit(x) => digit = Character.digit(x, 10)
  case _ => sign = 0
}

y match {
  case '+' => sign = 1
  case '-' => sign = -1
  case x => digit = Character.digit(x, 10)
}

// We can not use in case the same name as constant name so be careful

// with object
obj match {
  case x: Int => x
  case s: String => Integer.getInteger(s)
  case _: BigInt => Int.MaxValue
  // case: _ => 0 // ?
}

// with array
arr match {
  case Array(0) => "0"
  // Destructuring
  case Array(x, y) => x + " " + y
  case Array(0, _*) => "0 ..."
  case _ => "something else"
}

// with list
lst match {
  case 0 :: Nil => "0"
  case x :: y :: Nil => x + " " + y
  case 0 :: tail => "0 ..."
  case _ => "something else"
}

// with tuples
pair match {
  case (0, _) => "0 ..."
  case (y, 0) => y + " 0"
  case _ => "neither is 0"
}

val (x, y) = (1, 2)

// val q: scala.math.BigInt = 3
// val r: scala.math.BigInt = 1
val (q, r) = BigInt(10) /% 3

// val first: Int = 1
// val second: Int = 2
val Array(first, second, _*) = Array(1, 2, 3, 4, 5)

val map = Map(1 -> "1", 2 -> "2")
// 1, 1
// 2, 2
for ((k, v) <- map) println(s"$k, $v")
// 2
for ((k, "") <- Map(1 -> "1", 2 -> "")) println(k)
// 2
for ((k, v) <- map if v == "") println(k)

class User(name: String, password: String)
val user = new User("Bob", "qwerty")
val userCopy = new User("Bob", "qwerty")
println(user) // $line8.$read$$iw$User@3452b54c
user == userCopy // false

case class UserCaseClass(name: String, password: String)
val userCaseClass = UserCaseClass("Bob", "qwerty")
val userCaseClassCopy = UserCaseClass("Bob", "qwerty")
userCaseClass == userCaseClassCopy // true
println(userCaseClass) // UserCaseClass(Bob,qwerty)

// sealed file
sealed abstract class Amount
case class Dollar(value: String) extends Amount
case class Euro(value: String, unit: String) extends Amount

// In pattern matching we can use infix notation

val d = Euro("100", "Euro")
d match {
  case value Euro unit => println(s"value=$value, unit=$unit")
}

val list = List(1, 2, 3)
list match {
  case h :: t => print(s"head=$h, tails=$t")
}

// matching with nested structures

abstract class Item
case class Article(description: String, price: Double) extends Item
case class Bundle(description: String, discount: Double, item: Item*) extends Item

val bundle = Bundle(
  "Black Friday",
  20.0,
  Article("Iphone", 300.0),
  Article("Macbook Air", 1000.0),
  Article("Macbook Pro", 1500.0)
)

bundle match {
  case Bundle(_, _, Article(desc, _), _*) => println(desc) // Iphone
  case _ =>
}
// We can assign result of pattern matching to the variable (as let)
bundle match {
  case Bundle(_, _, a @ Article(desc, _), _*) => println(a) // Article(Iphone,300.0)
  case _ =>
}

// Parameterized types

import scala.collection.AbstractSeq

// Generic class with type parameters T and S
class Pair[T, S](val first: T, val second: S)
// Type inference
val p = new Pair(10, "Ten") // val p: Pair[Int,String] = Pair@7898e7ae
val p1 = new Pair[Any, Any](10, "Ten") // val p1: Pair[Any,Any] = Pair@e115646
p == p1 // false (we should implement equal method)
case class Pair1[T, S](val first: T, val second: S)
val p2 = Pair1(10, "Ten")
val p3 = Pair1[Any, Any](10, "Ten")
p2 == p3 // true // regardless of type

def getMiddle[T](a: Array[T]) = a(a.length / 2)
getMiddle(Array(1, 2, 3, 4, 5)) // 3
getMiddle(Array("1", "2", "3", "4", "5")) // "3"

def f = getMiddle[Int] _
f(Array(1, 2, 3, 4, 5)) // 3
// f(Array("1", "2", "3", "4", "5"))

// We can not be sure if a of type T has a length method...
// def getMiddle[T](a: T) = a(a.length / 2)
// ... so we have to give to compiler more information, for example
// def getMiddle[T](a: List[T]) = a(a.length / 2)
// But better to define so-called boundaries

// Upper bound
def getMiddle[S, T <: AbstractSeq[S]](a: T) = a(a.length / 2)
getMiddle[Int, List[Int]](List(1, 2, 3, 4, 5)) // 3
getMiddle[Int, Vector[Int]](Vector(1, 2, 3, 4, 5)) // 3
// inferred type arguments [Nothing,scala.collection.immutable.Vector[Int]] do not conform to method getMiddle's type parameter bounds [S,T <: scala.collection.AbstractSeq[S]]
// getMiddle(Vector(1, 2, 3, 4, 5))

// lower bottom

case class Pair2[T](first: T, second: T) {
  def replaceFirst(newFirst: T) = Pair2[T](newFirst, second)
  def replaceFirst1[R >: T](newFirst: R) = Pair2[R](newFirst, second)
}

Pair2[Int](1, 2).replaceFirst(3) // val res8: Pair2[Int] = Pair2(3,2)

class Person { override def toString = "Person" }
class Student extends Person { override def toString = "Student" }
// Compiler allows us here another type cause Person is a super class of Student class
Pair2(new Student, new Student).replaceFirst1(new Person) // val res8: Pair2[Person] = Pair2(Person,Student)

// Restriction of types

/*
// Type T is equal to U type (T == U)
T =:= U

// T is a subtype of U type (U -> T)
/T <:< U

// Type T can be implicitly converted to U type (T -(implicit)-> U)
T <%< U
*/

// Implicit evidence parameter

class Pair[T](val first: T, val second: T)(implicit ev: T <:< Comparable[T])

// def firstLast[A, C <: Iterable[A]](it: C) = (it.head, it.last)
// inferred type arguments [Nothing,List[Int]] do not conform to method firstLast's type parameter bounds [A,C <: Iterable[A]]
// firstLast(List(1, 2, 3))
// to match first type C and then type A (not together), we need to add an implicit
def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) = (it.head, it.last)
firstLast(List(1, 2, 3)) // val res9: (Int, Int) = (1,3)

// Covariant type

/*
class Pair[T](val first: T, val second: T)
class Person(...)
class Student(...) extends Person
def makeFriends(p: Pair[Person]) = ...
// It won't work because Pair[Student] is not a subtype of Pair[Person] !!!
makeFriends(new Pair[Student](...))

There for we should add plus sign
*/

class Pair[+T](val first: T, val second: T)

// Covariance is the preservation of the inheritance hierarchy of the original types in derived types in the same order

// Contravariant type

trait Friend[-T] {
  def beFriend(someone: T)
}

class Person1 extends Friend[Person1] {
  override def beFriend(someone: Person1): Unit = println(s"we are friends")
}
class Student1 extends Person1

val susan = new Student1
val fred = new Person1

def makeFriendsWith(s: Student1, f: Friend[Student1]) = {
  f.beFriend(s)
}

makeFriendsWith(susan, fred) // we are friends

// Here Student is a subtype of Person type and Friend[Student] is a supertype of Friend[Person]
// Contravariance is the reversal of the hierarchy of the original types in the derived types.

// We can define both variants:
// trait Function1[-T1, +R] extends AnyRef

def friends(students: Array[Student1], find: Function1[Student1, Person1]) = {
  for (s <- students) yield find(s)
}

def findStudent(p: Person1): Student1 => Person1

// Functions are contravariant to their arguments and covariant to their results
friends(Array(new Student1), findStudent(new Person1))

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
// Additional types

// To be able to use chain methods in subclasses we need
// to specify return type as this.type otherwise it wouldn't work
// (cause in child instances parent methods return parent type (in our case it's a User type))
class User {
  def setName(name: String): this.type = {
    // some code
    this
  }

  def setAge(age: Int): this.type = {
    // some code
    this
  }
}

val user = new User
user.setName("Bob").setAge(19)

class Admin extends User {
  def setPassword(password: String) = {
    // some code
    this
  }
}
val admin = new Admin
admin.setName("Jon").setAge(25).setPassword("qwerty")

// Free interfaces. We can write like pure English

// singleton
object Name
class User1 {
  private var useNextArgs: Any = null
  var name = " "

  def set(obj: Name.type): this.type = {
    useNextArgs = obj
    this
  }

  def to(arg: String) = if (useNextArgs == Name) name = arg
}
val user1 = new User1
user1 set Name to "Andy"

// Type projections

class Network {
  class Member

  def makeMember = new Member

  val members: ArrayBuffer[Member] = ArrayBuffer.empty[Member]
  // type projection
  val members1: ArrayBuffer[Network#Member] = ArrayBuffer.empty[Network#Member]
}

val n1 = new Network
val n2 = new Network
val m1: n1.Member = n1.makeMember
val m2: n2.Member = n2.makeMember

n1.members += m1
// we can not add instance from other class
// n1.members += m2
n1.members1 += m1
// but we can do it with using type projection
n1.members1 += m2

// Chains

// com.company.scalaadvanced.Member
// Every component in the chain, except the last one, must be "stable", that is, denote a single, defined scope.
// All of the following components meet this condition:
// package
// object
// value (val)
// this, super, super[S], C.this, C.super, C.super[S]

// Type aliases

class Book {
  import scala.collection.mutable._
  type Index = HashMap[String, (Int, Int)]
  ...
}

// Compound type

// T1 with T2 with T3 ...

trait Openable {
  def open()
}

trait Closable {
  def close()
}

// type intersection
def openAndClose(p: Openable with Closable): Unit = {
  p.open()
  p.close()
}

import java.io.Serializable
import scala.collection.mutable.ArrayBuffer

class Shape
class Rectangle extends Shape
val img = new ArrayBuffer[Shape with java.io.Serializable]
val rect = new Rectangle
img += rect

// An instance of this type must be a subtype of the Shape and Serializable and must has a method contains
val a: Shape with Serializable { def contains(p: Point): Boolean }

// An infix type is a type with two type parameters, the definition of which is written in "infix" notation,
// when the name of the type being defined is placed between the type parameters

/*
Instead of
    Map[String, Int]
we can write like that
    String Map Int

In math: (<- means "belongs" )
A x B = { (a, b) | a <- A, b <- B }
In scala:
(A, B)
*/

// An existential type is a type expression followed by a "forSome {...}" construct,
// where curly braces surround the "type" and "val" declarations
//
// Instead of
// Array[T] forSome { type T <: JComponent }                       T is the inheritor of the JComponent
// we can use so-called wild card type - "_" to expsess more shortly
// Array[_ <: JComponent]

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

// A wildcard type
//val a: Array[_]
//val c: Map[_, _]

// An existential type
//val b: Array[T] forSome { type T }
//val d: Map[T, U] forSome { type T; type U }
//val e: Map[T, U] forSome { type T; type U <: T }

class Network {
  class Member

  def makeMember = new Member

  val members: ArrayBuffer[Member] = ArrayBuffer.empty[Member]
  // type projection
  val members1: ArrayBuffer[Network#Member] = ArrayBuffer.empty[Network#Member]
}

val n1 = new Network
val n2 = new Network
val m1: n1.Member = n1.makeMember
val m2: n2.Member = n2.makeMember

n1.members += m1
// we can not add instance from other class
// n1.members += m2
n1.members1 += m1
// but we can do it with using type projection
n1.members1 += m2

val n = new Network
//val m: n.Member forSome { val n: Network } // Also we could use type projection

def process[M <: n.Member forSome { val n: Network }](m1: M, m2: M) = (m1, m2)

// inferred type arguments [Network#Member] do not conform to method process's type parameter bounds [M <: n.Member forSome { val n: Network }] process(m1, m2)
// process(m1, m2)
process(m1, m1)

// Abstract type in action

trait Reader {
  type Contents // Abstract type
  def read(fileName: String): Contents
}

class StringReader extends Reader {
  override type Contents = String
  def read(fileName: String): String = Source.fromFile(fileName, "UTF-8").mkString
}

class ImageReader extends Reader {
  override type Contents = BufferedImage
  def read(fileName: String) = ImageIO.read(new File(fileName))
}

// But we also can re-write it with using parameterized types

trait Reader1[C] {
  def read(fileName: String): C
}

class StringReader extends Reader1[String] {
  def read(fileName: String): String = Source.fromFile(fileName, "UTF-8").mkString
}

class ImageReader extends Reader1[BufferedImage] {
  def read(fileName: String) = ImageIO.read(new File(fileName))
}

// Both of them is presents tradeoff of using each other

// Higher-order types

trait MyIterable[E] {
  def iterator(): Iterator[E]
  def map[F](f: (E) => F): Iterable[F]
}

abstract class Buffer[E] extends Iterable[E] {
  override def iterator: Iterator[E] = ???
  def map[F](f: E => F): Buffer[F]
}

// Higher-order type
trait MyIterableConst[E, C[_]] {
  def iterator: Iterator[E]
  def build[F](): C[F]
  def map[F](f: (E) => F): C[F]
}

class MyRange extends MyIterableConst[Int, Buffer]

// Implicit parameters and conversions

/********** /src/main/scala/implicits/MyString.scala **************/

package implicits

case class MyString(s: String) {
  def +(myString: MyString): String = this.s + " " + myString.s
}

object MyString {
    implicit def int2MyString(n: Int) = MyString(n.toString)
}

/********** /src/main/scala/implicits/Run.scala **************/

package implicits

object Run extends App {
    val myString = MyString("Hello")
    2 + myString // 2 Hello
}

// Or we can do with another aproach

/********** /src/main/scala/implicits/conversions/Conversions.scala **************/

package implicits.conversions

object Conversions {
    implicit def int2MyString(n: Int) = MyString(n.toString)
}

/********** /src/main/scala/implicits/Run.scala **************/

package implicits

import implicits.conversions.Conversions._

object Run extends App {
    val myString = MyString("Hello")
    2 + myString // 2 Hello
}

// Names of implicits are not mater for compiler (its only for us)

/********** /src/main/scala/implicits/conversions/Conversions.scala **************/

package implicits.conversions

object Conversions {
    implicit def int2MyString(n: Int) = MyString(n.toString)
    implicit def int2MyString2(n: Int) = MyString(n.toString)
}

/********** /src/main/scala/implicits/Run.scala **************/

package implicits

// It's not working due to two the same implicits
// import implicits.conversions.Conversions._

// Eliminating unnecessary implicits
import implicits.conversions.Conversions.{ int2MyString2 => _, _ }

object Run extends App {
    val myString = MyString("Hello")
    // import implicits.conversions.Conversions.int2MyString
    2 + myString // 2 Hello
}



// Implicit parameters

def double(implicit value: Int) = value * 2

implicit val multiplier = 2

println(double) // 4

case class MyDelimiters(left: String, right: String)

//object MyDelimiters {
//  implicit val myDelimiters = MyDelimiters("<<", ">>")
//}

// Simple object
object Del {
  implicit val myDelimiters = MyDelimiters("<<", ">>")
}

def quote(s: String)(implicit delims: MyDelimiters) = delims.left + s + delims.right

// Bad practice (read bellow)
def quote2(s: String)(implicit left: String, right: String) = left + s + right

// Both of them has String type and for compiler it's not obvious => Bad practice
// Error: ambiguous implicit values: both value left of type String and value right of type String
// implicit val left = "["
// implicit val right = "]"
// quote2("Hello, world!")

// implicit val myDelimiters = MyDelimiters("<<", ">>")

quote("Hello, world!")(MyDelimiters("<<", ">>")) // val res0: String = <<Hello, world!>>
// It works due to we have the companion object - MyDelimiters
// or it can work with object containing implicit
import Del._ // or import Del.myDelimiters
quote("Hello, world!") // val res1: String = <<Hello, world!>>

// def smaller[T](a: T, b: T) = if (a < b) a else b
def smaller[T](a: T, b: T)(implicit order: T => Ordered[T]) = if (a < b) a else b
smaller(1, 2) // val res2: Int = 1

// Context border
class Pair[T: Ordering](val first: T, val second: T) {
  def smaller(implicit ord: Ordering[T]) = if (ord.compare(first, second) < 0) first else second
  def smaller2 = if (implicitly[Ordering[T]].compare(first, second) < 0) first else second
  def smaller3 = {
    import Ordered._
    if (first < second) first else second
  }
}

val pair = new Pair(1, 2)
pair.smaller   // val res3: Int = 1
pair.smaller2  // val res4: Int = 1
pair.smaller3  // val res5: Int = 1

// Restricting types

// <:< - means type C can be implicitly convertible to type iterable A
// ev - evidence object, identity function
// implicit ev: String <:< AnyRef
def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) = (it.head, it.last)

// Cannot prove that List[Int] <:< Iterable[String]
// firstLast[String, List[Int]](List(1, 2, 3))




// Abstract elements

trait Abstract {
    type T                      # Abstract type
    def transform(x: T): T      # Abstract method
    val initial: T              # Abstract val
    var current: T              # Abstract var
}

class Concrete extends Abstract {
    type T = String
    def transform(x: String) = x + x
    val initial = "Hi"
    var current = initial
}

// type aliasing

val a: package1.package2.package3.A = package1.package2.package3.A()

trait B {
    type A = package1.package2.package3.A // type alias
    var b: A                              // now we can use that type alias
}

trait Abstract {
    type A
    def a(a: A) = println(a)
}

class ConcreteString extends Abstract {
    type A = String
}

class ConcreteInt extends Abstract {
    type A = Int
}

val cS = new ConcreteString
cs.a("Hello")  // Hello
vak cI = new ConcreteInt
cI.a(16)       // 16


abstract class Fruit {
  val v: String
  def m: String
}

abstract class Apple extends Fruit {
  val v: String
  val m: String
}

abstract class Apple1 extends Fruit {
  // def v: String
  val m: String
}

trait Rational {
  val n: Int
  val d: Int
  require(d != 0)
}

// We will get error (due to require in trait and specific initialization of attributes):
// java.lang.IllegalArgumentException: requirement failed
new Rational {
  // These fields are initialized later than the trait
  override val n: Int = 1
  override val d: Int = 2
}


// Pre-initialized fields

new { // Initializers
    val n = 1 * x
    val d = 2 * x
} with RationalTrait
res1: RationalTrait = 1/2

object myObject extends {
    val n = 1 * x
    val d = 2 * x
} with RationalTrait

// pre-initialized fields in class definition
class RationalClass(num: Int, den: Int) extends {
    val n = num
    val d = den
} with RationalTrait {
    def +(that: RationalClass) = new RationalClass(
        number * that.denom + that.number * denom,
        denom * that.denom
    )
}

// Lazy val-variables

// If you put the lazy modifier in front of the val-variable definition,
// then the initialization expression on the right side will be executed
// only the first time the val-variable is used

object Demo {
  val x = {
    println("Initializing x")
    "done"
  }
}

Demo // Initializing x
     // val res0: Demo.type = Demo$@1a4619c0

Demo.x // val res0: String = done

object Demo2 {
  lazy val x = {
    println("Initializing x")
    "done"
  }
}

Demo2    // val res2: Demo2.type = Demo2$@a2b497c

Demo2.x  // Initializing x
         // val res3: String = done


/********** /src/main/scala/currency/Currency.scala **************/

package currency

abstract class Currency {
  val amount: Long
  def designation: String
  override def toString: String = s"$amount $designation"
  def +(that: Currency): Currency
  def *(that: Currency): Currency
}

/********** /src/main/scala/currency/CurrencyZone.scala **************/

package currency

abstract class CurrencyZone {
  type Currency <: AbstractCurrency

  def make(amount: Long): Currency

  abstract class AbstractCurrency {
    val amount: Long

    def designation: String

    override def toString: String = s"$amount $designation"

    def +(that: Currency): Currency = make(this.amount + that.amount)

    def *(that: Currency): Currency = make(this.amount * that.amount)
  }
}

/********** /src/main/scala/currency/US.scala **************/

package currency

object US extends CurrencyZone {
  abstract class Dollar extends AbstractCurrency {
    override def designation: String = "USD"
  }

  type Currency = Dollar

  def make(x: Long) = new Dollar {
    override val amount: Long = x
  }
}

/********** /src/main/scala/currency/Run.scala **************/

package currency

object Run extends App {
  val us1 = US.make(100)
  println(us1) // 100 USD

  val us2 = us1.+(US.make(900))
  println(us2) // 1000 USD
}





// Extractors

def isEmail(s: String): Boolean
def domain(s: String): String
def user(s: String): String

if (isEmail(s)) println(user(s)) + " AT " + domain(s)
else println("not an email address")

s match {
    case Email(user, domain) => println(user + " AT " + domain)
    case _ => println("not an email address")
}

ss match {
    case Email(u1, d1) :: Email(u2, d2) :: _ if (u1 == u2) => ...
    ...
}

// An extractor is an object that has a method unupply as one of its elements

object EMail {
  def apply(user: String, domain: String): String = user + "@" + domain

  def unapply(str: String): Option[(String, String)] = {
    val parts = str split "@"
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }
}

val bob = EMail("bob", "gmail.com") // val res0: String = bob@gmail.com
val anotherStr = "123"

EMail.unapply(bob)        // val res0: Option[(String, String)] = Some((bob,gmail.com))
EMail.unapply(anotherStr) // val res1: Option[(String, String)] = None

// unapply("radik.khisamutdinov@gmail.com") -> Some("radik.khisamutdinov", "gmail.com")
// unapply("radik.khisamutdinov.com")       -> None

selectorString match { case EMail(user, domain) => ... }
EMail.unapply(selectorString)

val x: Any = bob

x match { case EMail(user, domain) => s"$user and $domain" }   // val res2: String = bob and gmail.com
// scala.MatchError: x (of class java.lang.String)
// "x" match { case EMail(user, domain) => s"$user and $domain" }
"test@test.com" match { case EMail(user, domain) => s"$user and $domain" } // val res4: String = test and test.com

The work of the apply method is called injection, because this method takes some arguments and returns an element of the given set
(part1, part2, ...) -> whole

The work of the unapply method is called extraction, since this method takes an element of the same set and extracts some of its parts
whole -> (part1, part2, ...)

EMail.unapply(EMail.apply(user, domain)) -> it should return -> Some(user, domain)
EMail.unapply(obj) match { case Some(u, d) => EMail.apply(u, d) } -> it should returns the same object -> obj

object Twice {
  def apply(s: String): String = s + s
  def unapply(arg: String): Option[String] = {
    val length =  arg.length / 2
    val half = arg.substring(0, length)
    if (half == arg.substring(length)) Some(half) else None
  }
}

object UpperCase {
  def unapply(arg: String): Boolean = arg.toUpperCase == arg
}

def userTwiceUpper(s: String) = s match {
  // here we use two extractors (UpperCase takes empty initializers list otherwise it would by comparision with type)
  case EMail(Twice(x @ UpperCase()), domain) => x + " in domain " + domain
  case _ => "no match"
}

userTwiceUpper("test@test.com")     // no match
userTwiceUpper("testtest@test.com") // no match
userTwiceUpper("QE@test.com")       // no match
userTwiceUpper("QEQE@test.com")     // QE in domain test.com
userTwiceUpper("QE")                // no match
userTwiceUpper("QEQE")              // no match

dom match {
    case Domain("org", "abc") => println("abc.org")
    case Domain("com", "sun", "java") => println("java.sun.com")
    case Domain("net", _*) => println(".net domain")
}

object Domain {
  def apply(parts: String *): String = parts.reverse.mkString(".")

  def unapplySeq(arg: String): Option[Seq[String]] = Some(arg.split("\\.").reverse)
}

def isBobInDotCom(s: String): Boolean = s match {
  case EMail("bob", Domain("com", _*)) => true
  case _ => false
}

isBobInDotCom("bob@sun.org")  // false
isBobInDotCom("bob@sun.com")  // true

// Extractors and Sequence Templates

List()
List(x, y, _*)
Array(x, 0, 0, _)

package scala
object List {
    def apply[T](elems: T*) = elems.toList
    def unapplySeq[T](x: List[T]): Option[Seq[T]] = Some(x)
    ...
}

/*
View independence is a property that breaks the link between data presentation and templates, thanks to the use of templates,
which have nothing to do with the data type of the selected object

View independence allows you to change the implementation type used in a set of components without affecting those who use
these components

Comparing case-classes with extractors
    case-classes:
        - brevity
        - speed
        - the ability to statically check the code
    extractors:
        - independence of views
*/



// Future

/*
Future is a wrapper object for a value that may not yet exist.
Typically, the Future value is evaluated concurrently
and can be used later. Composing parallel tasks
this way results in faster asynchronous non-blocking parallel code

Pros:
    - you can create chains of parallel computing
    - the code is more compact, allows you to adhere to the declarative
      style, easier to read and maintain
    - no need to explicitly use the thread pool
Cons:
    - an additional layer of abstraction makes it difficult to control
      what happens at a lower level
    - the syntax is unusual and difficult for a programmer,
      accustomed to using thread pool
*/

future.map(x => x + 1)

fot { // it will be waiting for execution of futures (ordering is not guaranteed)
    x <- future1
    y <- future2
} yield(x + y) // the result also is presented as a future


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val f = Future { // val f: scala.concurrent.Future[Int] = Future(<not completed>)
  Thread.sleep(2000)
  2 * 55
}

f.isCompleted
f.value // val res1: Option[scala.util.Try[Int]] = Some(Success(110)) or None

/*
scala.util.Try[+T] Sealed abstract
  ^
  |
  +-- scala.util.Success[T] final case
  |
  +-- scala.util.Failure[T] final case
*/

val f2 = f.map(x => x + 1) // val f2: scala.concurrent.Future[Int] = Future(<not completed>)
f2.value  // val res2: Option[scala.util.Try[Int]] = Some(Success(111))

val f3: Future[Int] = for {
  x <- f
  y <- f2
} yield x + y

f3.value

val f4 = for { // here the future executions will be ordered!!!
  x <- Future { Thread.sleep(1000); 21 + 23 }
  y <- Future { Thread.sleep(1000); 21 + 23 }
} yield x + y

Thread.sleep(3000)
f4.value // val res5: Option[scala.util.Try[Int]] = Some(Success(88))

Future.successful(1 + 1) // val res4: scala.concurrent.Future[Int] = Future(Success(2))

Future.failed(new Exception("Stop!")) // val res5: scala.concurrent.Future[Nothing] = Future(Failure(java.lang.Exception: Stop!))

Future.fromTry(Success(2 + 2)) // val res6: scala.concurrent.Future[Int] = Future(Success(4))
Future.fromTry(Failure(new Exception("Stop!"))) // val res7: scala.concurrent.Future[Nothing] = Future(Failure(java.lang.Exception: Stop!))

val pro = Promise[Int] // val pro: scala.concurrent.Promise[Int] = Future(<not completed>)
val f5: Future[Int] = pro.future // val f5: scala.concurrent.Future[Int] = Future(<not completed>)
f5.value // val res8: Option[scala.util.Try[Int]] = None

pro.success(5) // val res9: pro.type = Future(Success(5))
f5.value // val res10: Option[scala.util.Try[Int]] = Some(Success(5))

// Future offers two methods, filter and collect, to ensure that a property is true relative to the value of Future.

val f6 = Future(7)
val valid = f6.filter(res => res > 0)
val invalid = f6.filter(res => res < 0)
valid.value // val res11: Option[scala.util.Try[Int]] = Some(Success(7))
invalid.value // val res12: Option[scala.util.Try[Int]] = Some(Failure(scala.concurrent.Future$$anon$2: Future.filter predicate is not satisfied))

val valid1 = for (res <- f6 if res > 0) yield res
val invalid1 = for (res <- f6 if res < 0) yield res
valid1.value // Some(Success(7))
invalid1.value // Some(Failure(scala.concurrent.Future$$anon$2: Future.filter predicate is not satisfied))

val valid2 = f6.collect { case res if res > 0 => res + 10 }
val invalid2 = f6.collect { case res if res < 0 => res + 10 }
valid2.value   // Some(Success(17))
invalid2.value // Some(Failure(scala.concurrent.Future$$anon$1: Future.collect partial function is not defined at: 7))

// Using the failed, fallBackTo, recover and recoverWith methods, you can work with Future that failed

val f7 = Future(1/0)
f7.value // Some(Failure(java.lang.ArithmeticException: / by zero))
val f8 = f7.failed
f8.value // Some(Success(java.lang.ArithmeticException: / by zero))
f6.failed // Future(Failure(scala.concurrent.Future$$anon$3: Future.failed not completed with a throwable.))
f7.fallbackTo(f6) // Future(Success(7))
f7.fallbackTo(invalid) // Future(Failure(java.lang.ArithmeticException: / by zero))
val f9 = f7.recover { case ex: ArithmeticException => -1 }
f9.value // Some(Success(-1))
val f10 = f7.recoverWith {
  case ex: ArithmeticException => Future(-1)
}
f10.value // Some(Success(-1))
val t = f6.transform(
  res => res * (-1),
  ex => new Exception("my exception")
)
t.value // Some(Success(-7))

val t1 = f7.transform(
  res => res * (-1),
  ex => new Exception("my exception")
)
t1.value // Some(Failure(java.lang.Exception: my exception))




// Functional programming techniques

// Functional programming is a programming paradigm in which the computation process is interpreted as the calculation of the values of functions
// in the mathematical understanding of the latter (as opposed to functions as subroutines in procedural programming)

/*
Clean functions are functions without side effects

Doing any of the following will cause side effects
- Variable reassignment
- Change data structure in place
- Setting the field in the object
- Throwing an exception or stopping with an error
- Print to console or read user input
- Reading or writing to a file
- Drawing on the screen

An expression is said to be referentially transparent if it can be replaced with an appropriate value without changing the behavior of the program.
As a result, evaluating a referentially transparent function yields the same value for the same arguments.
Such functions are called clean.
*/

val x = "Hello, world!"

val r1 = x.reverse // !dlrow ,olleH
val r2 = x.reverse // !dlrow ,olleH
val r1 = "Hello, world!".reverse // !dlrow ,olleH
val r2 = "Hello, world!".reverse // !dlrow ,olleH
// => x is referentially transparent

val x1 = new StringBuilder("Hello")

val y = x1.append(", world!")

val r1 = y.toString // Hello, world!
val r2 = y.toString // Hello, world!
val r1 = (x1.append(", world!")).toString // Hello, world!, world!
val r2 = (x1.append(", world!")).toString // Hello, world!, world!, world!
// => it's not a referentially transparent. So here we do not use clean functions

// Linked list (immutable)

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]
object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def products(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * products(xs)
  }

  def apply[A](as: A*): List[A] = if (as.isEmpty) Nil else Cons(as.head, apply(as.tail: _*))
  val example = Cons(1, Cons(2, Cons(3, Nil)))
  val example2 = List(1, 2, 3)
  val total = sum(example)
}

def foldRight[A, B](l: List[A], z: B)(f: (A, B) => B): B = l match {
  case Nil => Z
  case Cons(x, xs) => f(x, foldRight(xs, z)(f))
}

def sum2(l: List[Int]) = foldRight(l, 0.0)(_ + _)

def product2(l: List[Double]) = foldRight(l, 1.0)(_ * _)

/*
foldRight(Cons(a, Nil), z)(f) <=> f(a, z)
foldRight(Cons(a, Cons(b, Nil)), z)(f) <=> f(a, f(b, z))

foldRight(Cons(1, Cons(2, Cons(3, Nil))), 0)(_ + _)
1 + foldRight(Cons(2, Cons(3, Nil)), 0)(_ + _)
1 + (2 + foldRight(Cons(3, Nil), 0)(_ + _))
1 + (2 + (3 + (foldRight(Nil, 0)(_ + _))))
1 + (2 + (3 + (0)))
*/

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

// Throwing exception violates teferential transparency
def failingFn(i: Int): Int = {
  val x: Int = throw new Exception("fail!")
  try {
    val y = 42 + 5
    x + y
  }
  catch { case e: Exception => 43 }
}

def mean(xs: Seq[Double]): Double =
  if (xs.isEmpty) throw new ArithmeticException("mean of empty list!")
  else xs.sum / xs.length

def mean_1(xs: IndexedSeq[Double], onEmpty: Double): Double =
  if (xs.isEmpty) onEmpty
  else xs.sum / xs.length

def mean(xs: Seq[Double]): Option[Double] =
  if (xs.isEmpty) None
  else Some(xs.sum / xs.length)

sealed trait Either[+E, +A]
case class Left[+E](value: E) extends Either[E, Nothing]
case class Right[+A](value: A) extends Either[Nothing, A]

def mean(xs: IndexedSeq[Double]): Either[String, Double] =
  if (xs.isEmpty) Left("mean of empty list!")
  else Right(xs.sum / xs.length)

def safeDiv(x: Double, y: Double): Either[Exception, Double] =
  try { Right(x / y) }
  catch { case e: Exception => Left(e) }

// Strictness & lazyness

/*
List(1, 2, 3, 4) map (_ + 10) filter (_ % 2 == 0) map (_ * 3)
List(11, 12, 13, 14) filter (_ % 2 == 0) map (_ * 3)
List(12, 14) map (_ * 3)
List(36, 42)
*/

def square(x: Double): Double = x * x
square(sys.error("failure"))

// Non strict functions

// => parameter - mean call by name (it will be evaluated only once)
def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A = if (cond) onTrue else onFalse
if2(false, sys.error("fail"), 3) // 3

def pair(i: => Int) = {
  // Without adding keyword lazy it will evaluate expression in the pair { ... } and we will see in the output also "Hi".
  // But it will be evaluated when we decide to use j variable (It's kinda COW)
  lazy val j = i

  (1, 2)
}

// Stream example

trait Stream[+A] {
  def uncons: Option[(A, Stream[A])]
  def isEmpty: Boolean = uncons.isEmpty
}
object Stream {
  def empty[A]: Stream[A] = new Stream[A] { def uncons = None }
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = new Stream[A] {
    lazy val uncons = Some((hd, tl))
  }
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}

val rng = new java.util.Random
rng.nextDouble()
rng.nextDouble()

trait Random {
  def nextInt: Int
  def nextDouble: Double
}

trait RNG {
  def nextInt: (Int, RNG) // We generate new value and return RNG with new state without changing status of the current RNG
}

def randomPair(rng: RNG): ((Int, Int), RNG) = {
  val (i1, rng2) = rng.nextInt
  val (i2, rng3) = rng2.nextInt
  ((i1, i2), rng3)
}

// Combinatorial libraries are libraries consisting of one or more data types, along with a collection (often of higher order)
// functions for creating, manipulating and combining values of these types

def sum(as: IndexedSeq[Int]): Int =
  if (as.size <= 1) as.headOption getOrElse 0
  else {
    val (l, r) = as.splitAt(as.length / 2)
    sum(l) + sum(r)
  }

// Par - some container for parallel computation
def unit[A](a: => A): Par[A]
def get[A](a: Par[A]): A

// There is no parallel computation inside this function, since Par.get is waiting for computation,
// which essentially means sequential computation
def sum(as: IndexedSeq[Int]): Int =
  if (as.size <= 1) as.headOption getOrElse 0
  else {
    val (l, r) = as.splitAt(as.length / 2)
    val sumL: Par[Int] = Par.unit(sum(l))
    val sumR: Par[Int] = Par.unit(sum(r))
    Par.get(sumL) + Par.get(sumR)
  }

// Now it should be ok with parallel computations
def sum(as: IndexedSeq[Int]): Par[Int] =
  if (as.size <= 1) Par.unit(as.headOption getOrElse 0)
  else {
    val (l, r) = as.splitAt(as.length / 2)
    Par.map2(sum(l), sim(r))(_ + _)
  }




// Monoids and Monads

/*
A semigroup is a set with a given associative binary operation.
For example, a set of integers with a given addition operation over it.

A monoid is a semigroup with a neutral element.

An example of a neutral number, for example zero in a set of numbers, or if we talk about a set of lines with an operation
concatenation, then it is an empty string.
*/

// Associativity is a property of operations that allows us to restore the sequence of their execution in the absence
// explicit indications of priority at equal priority.
(1 + 2) + 3 = 1 + (2 + 3) = 1 + 2 + 3

// Monoid is a type with a given associative operation and a neutral element.

trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

val stringMonoid = new Monoid[String] {
  def op(a1: String, a2: String) = a1 + a2
  def zero = ""
}

val intAdditionMonoid = new Monoid[Int] {
  def op(a1: Int, a2: Int) = a1 + a2
  def zero = 0
}

stringMonoid.op("Hello", " world") // Hello world

val words = List("one", "two", "three")


// Application of monoids

def foldRight[B](z: B)(f: (A, B) => B): B
def foldLeft[B](Z: B)(f: (B, A) => B): B

// If A and B are of the same type, then we get a monoid

def foldRight(z: A)(f: (A, A) => A): A
def foldLeft(Z: A)(f: (A, A) => A): A

words.foldLeft("")(_ + _) == (("" + "Hic") + "Est") + "Index"
words.foldRight("")(_ + _) == "Hic" + ("Est" + ("Index" + ""))

// The value of monoids is that composition can be applied to them.


val s1 = words.foldRight(stringMonoid.zero)(stringMonoid.op) // onetwothree
val s2 = words.foldLeft(stringMonoid.zero)(stringMonoid.op) // onetwothree

def mapMergeMonoid[K, V](V: Monoid[V]): Monoid[Map[K, V]] =
  new Monoid[Map[K, V]] {
    override def op(a1: Map[K, V], a2: Map[K, V]): Map[K, V] = {
      a1.map {
        case (k, v) => (k, V.op(v, a2.get(k) getOrElse V.zero))
      }
    }
    override def zero: Map[K, V] = Map()
  }

def productMonoid[A, B](A: Monoid[A], B: Monoid[B]): Monoid[(A, B)] =
  new Monoid[(A, B)] {
    override def op(a1: (A, B), a2: (A, B)) = {
      val (b1, c1) = a1
      val (b2, c2) = a2
      ((A.op(b1, b2)), B.op(c1, c2))
    }

    override def zero = (A.zero, B.zero)
  }

val m: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAdditionMonoid))

val m1 = Map("1" -> Map("i1" -> 1, "i2" -> 2))
val m2 = Map("1" -> Map("i2" -> 2))

m.op(m1, m2) // Map(1 -> Map(i1 -> 1, i2 -> 4))

trait Foldable[F[_]] {
  def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B
  def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B
}

val listFoldable = new Foldable[List] {
  override def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    as.foldRight(z)(f)
  override def foldMap[A, B](as: List[A])(f: A => B)(mb: Monoid[B]): B = {
    foldRight(as)(mb.zero) { (a, b) =>
      mb.op(b, f(a))
    }
  }
}

val mProduct: Monoid[(Int, Int)] = productMonoid(intAdditionMonoid, intAdditionMonoid)

val p = listFoldable.foldMap(List(1, 2, 3, 4))(a => (1, a))(mProduct) // (4,10) 4 is a count of list's elements and
                                                                      // 10 is sum of list's elements
val mean = p._2 / p._1.toDouble  // 2.5

// A monad is an abstraction of a linear chain of related computations
// Monads allow sequential computation
// It can be called a container that stores a value of an arbitrary type.

// Most of Scala's standard collections are monads - List, Option or - like monads - Either, Future
// Monad saves data structures and acts only on members of this structure

trait Monad[T] {
  def flatMap[U](f: T => M[U]): M[U]
  def unit[T](x: T): M[T]
}

// flatMap - to transform the container (in fact, we are referring to the elements of the container)
// unit - to create a container

flatMap and unit must satisfy three laws:
1. Left unit law: unit(x) flatMap f == f(x)
2. Right unit law: m flatMap unit == m
3. Associativity law: (m flatMap f) flatMap g = f flatMap (x => f(x) flatMap g)

def square(x: Int): Option[Int] = Some(x * x)
def increment(x: Int): Option[Int] = Some(x + 1)

// left unit law: unit(x) flatMap f == f(x)
def leftUnit(): Boolean = {
  val x = 5
  val m: Option[Int] = Some(x)
  println(m.flatMap(square))
  println(square(x))
  m.flatMap(square) == square(x)
}

leftUnit() // Some(25)
           // Some(25)
           // true

// right unit law: monad flatMap unit == monad
def rightUnit() = {
  val x = 5
  val m: Option[Int] = Some(x)
  println(m.flatMap(x => Some(x)))
  println(m)
  m.flatMap(x => Some(x)) == m
}

rightUnit() // Some(5)
            // Some(5)
            // true

// associativity law: (monad flatMap f) flatMap g = monad flatMap (x => f(x) flatMap g)
def associativity() = {
  val x = 5
  val m: Option[Int] = Some(x)
  val left = m flatMap square flatMap increment
  println(left)
  val right = m flatMap(x => square(x) flatMap increment)
  println(right)
  left == right
}

associativity() // Some(26)
                // Some(26)
                // true

// It's very hard to read
for (square <- for (x <- Some(5); sq <- square(x)) yield sq; result <- increment(square)) yield result

// So we can use for comprehension (now it's more readable)
for  {
  x <- Some(5)                // we take value from monad
  square <- square(x)         // then apply square function
  result <- increment(square) // then apply increment function
} yield result                // and finally yield the result


// That is, we are chaining functions. That is, all these laws give us the opportunity to encapsulate
// logic of the chain of calculations, in fact, for this monads are needed.



// Functors

// A functor in Scala is a data type that implements the map method

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

val listFunctor extends Functor[List] {
  def map[A, B](as: List[A])(f: A => B): List[B] = as map f
}

def distribute[A, B](fab: F[(A, B)]): (F[A], F[B]) = (map(fab)(_._1), map(fab)(_._2)) // <=> unzip

// General law for map
map(v)(x => x) == v

// Applicative functor - data type that implements the functions map2 (takes two arguments) and unit

trait Applicative[F[_]] extends Functor[F] {
  def map2[A, B](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B]
  def unit[A](a: A): F[A]
}

def map[A, B](a: F[A])(f: A => B): F[B] = apply(unit(f))(a)

def map[A, B](a: F[A])(f: A => B): F[B] = map2(a, unit(f))(_(_))


def apply[A, B](oab: Option[A => B])(oa: Option[A]): Option[B] =
  (oab, oa) match {
    case (Some(f), Some(a)) => Some(f(a))
    case _ => None
  }

apply[Int, String](Some(_.toString))(Some(5)) // val res0: Option[String] = Some(5)
apply[Int, String](Some(_.toString))(None) // val res1: Option[String] = None


// Lifting

// In Scala, lifting means converting a function of the form A => B to
// a function of the form F [A] => F [B], which can be applied to a functor or monad
// (i.e. we put A and B in contexts or containers)

def double(x: Int): Int = 2 * x
val xs = Seq(1, 2, 3)
// double(xs)

def liftToSeq[A, B](f: A => B): (Seq[A] => Seq[B]) = (seq: Seq[A]) => seq.map(f)
liftToSeq(double)(xs) // val res2: Seq[Int] = List(2, 4, 6)

Let's consider map3

val f: (Int, Int, Int) => Int = (_ + _ + _)

// If we apply currying, we get another type

val g: Int => Int => Int => Int => f.curried

// Let's pass it to unit

val h: Option[Int => Int => Int => Int] = unit(g)

def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
  apply(apply(apply(unit(f.curried))(fa))(fb))(fc)

// Difference between monad and applicator functor

def apply[A, B](fab: F[A => B])(fa: F[A]): F[B]
def flatMap[A, B](f: A => F[B])(fa: F[A]): F[B]

// All monads are applicating functors, but the converse is not true, not all applicating functors are monads

def apply[A, B](oa: Option[A], oab: Option[A => B]): Option[B] =
  (oa, oab) match {
    case (Some(a), Some(f)) => Some(f(a))
    case _ => None
  }

def map2[A, B](x: Option[A], y: Option[B])(f: (A, B) => C): Option[C] =
  (x, y) match {
    case (Some(a), Some(b)) => Some(f(a, b))
    case _ => None
  }

def flatMap[A, B](oa: Option[A])(f: A => Option[B]): Option[B] =
  oa match {
    case Some(a) => f(a)
    case _ => None
  }

// Identity rule:
apply(unit(x => x))(v) == v

// Composition rule:
apply(apply(apply(unit(a => b => c => a(b(c)))))(g))(x) == apply(f)(apply(g)(x))
apply(map2(f, g)(_compose _))(x) == apply(f)(apply(g)(x))
map3(f, g, x)(_(_(_)))

// Homomorphism rule:
apply(unit(f))(unit(x)) == unit(f(x))
map(unit(x))(f) == unit(f(x))

// Exchange rule:
apply(u)(unit(y)) == apply(unit(_(y)))(u)
map2(u, unit(y))(_(_)) == map(u)(_(y))

// Traversable functors

def traverse[F[_], A, B](as: List[A], f: A => F[B]): F[List[B]]
def sequence[F[_], A](fas: List[F[A]]): F[List[A]]


trait Applicative[F[_]] {
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A,  B) => C): F[C]
  def unit[A](a: => A): F[A]
  def isEmpty[A](a: F[A]): Boolean
}

class ApplicateImpl[F[_]](f: Applicative[F]) {
  def sequenceMap[K, V](ofa: Map[K, F[V]]): F[Map[K, V]] =
    ofa.foldLeft(f.unit(Map.empty[K, V])) {
      case (facc, (k, fv)) =>
        if (f.isEmpty(fv)) facc else {
          f.map2(facc, fv) { (acc, v) =>
            acc + (k -> v)
          }
        }
    }
}

class MyOption extends Applicative[Option] {
  def isEmpty[A](a: Option[A]): Boolean = a.isEmpty

  override def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] =
    (fa, fb) match {
      case (Some(fa), Some(fb)) => Some(f(fa, fb))
      case _ => Option.empty[C]
    }

  override def unit[A](a: => A): Option[A] = if (a == null) None else Some(a)
}

val app = new ApplicateImpl[Option](new MyOption)
app.sequenceMap(Map(1 -> Some(5), 2 -> None, 3 -> Some(10))) // Some(Map(1 -> 5, 3 -> 10))

// Testing

// Service Level Agreement (SLA)

// Integration testing is a type of testing in which the integration of modules and their interaction is checked for compliance with the requirements
// among themselves, as well as the integration of subsystems into one common system.

/*
Classification of test types
- knowledge of the system
- according to the degree of automation
- by the time of testing
- according to the degree of isolation of components

Types of tests "by the degree of isolation of components"
- unit testing
- Integration testing
- System testing (acceptance)

Unit tests > Integration tests > Acceptance tests > Manual tests

If the architecture is unstable, it is preferable to test at the highest level (the console utility grep was given as an example),
since frequent changes in the code will generate multiple rewriting of unit tests.
When the architecture is stabilized, it will be possible to add tests at a lower level - unit tests.

Test-driven development
Test-Driven Development (TDD) is a software development technique that relies on
on repetition of very short development cycles: first, a test is written to cover the desired change,
then the code is written that will allow the test to pass, and at the end the new code is refactored to
relevant standards.

Acceptance testing is a comprehensive test required to determine the level of readiness of a system for
subsequent operation. Testing is carried out on the basis of a set of test scenarios covering the main
business operations system.

Unit testing is a programming process that allows you to check for correctness of individual modules of a program's source code.
The idea is to write tests for every non-trivial function or method.

ScalaTest dependancy
*/

