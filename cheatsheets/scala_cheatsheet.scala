// object on nagu klass, mille sees kõik asjad on staatilised
object Basics {
  // var - muteeritav, val - mittemuteeritav
  var x: Int = 5*(9 - 8) + 1
  x += 1 // See on lubatud, sest x on var
  val y: String = "Hello " + "World!"
  // y = "" ei ole lubatud, sest y on val

  // Muutujat saab väärtustada ka koodiploki abil
  val blockExpression: Int = {
    val a = 5*(9 - 8) + 1
    val b = 3 + 7 - 8*(7 - 3)
    // Ploki viimasel real olev avaldis on tagastusäärtus, mis 
    // muutujasse salvestatakse.
    a + b
  }

  /* 
   * Argumendi b vaikeväärtus on 6.
   * Seega func(5) on sama mis func(5, 6).
   * 
   * Võrdusmärgi järele tuleb avaldis - s.t lihtne üherealine avaldis
   * või koodiplokk
   * 
   * Int tüüpi varargs parameeter - a: Int*
   * esimese arvu saab sellest kätte kui a(0)
   */
  def func(a: Int, b: Int = 6): Int = a + b


  // Funktsiooni saab salvestada ka muutujasse
  //   nimi  |    muutuja tüüp   | väärtus
  val funcVal: (Int, Int) => Int = func

  // 2 viisi, kuidas defineerida anonüümset funktsiooni.
  //    1) Nimedega parameetrid
  val anonFunc1 = (a: Int, b: Int) => a + b

  //    2) _ tähistab parameetrit, mis esinevab funktsioonis vaid korra.
  val anonFunc2 = (_:String) + (_:String) // anonFunc2: (String, String) => String

  
  /* 
   * Funktsioon, mis võtab argumentideks kaks funktsiooni:
   * 1) Esimene funktsioon võtab kaks Int'i ja tagastab Int'i
   * 2) Teine funktsioon võtab ühe Stringi ja tagastab Stringi
   * 
   * Analoogselt võib ka funktsiooni tagastusväärtus olla funktsioon.
   */
  def funcAsParam(
    func1: (Int, Int) => Int, 
    func2: String => String // (String) => String
  ): Unit = { // Unit on sama mis Java void

    func1(5, 6)
    func2("Hello World!")
  }


  // Funktsiooni parameetrid võib ka jaotada mitmesse gruppi.
  def multiArgsList(x1: Int, x2: Int)(x3: Int, x4: Int): Int = x1 + x2 + x3 + x4
  // Rakendab funktsiooni func osaliselt, ei anna argumente x1 ja x4.
  // Tulemuseks on kahe argumendiga funktsioon
  val partiallyApplied: (Int, Int) => Int = multiArgsList(_: Int, 5)(7, _: Int)
  val result: Int = partiallyApplied(3, 4) // Sama mis multiArgsList(3, 5)(7, 4)


  def main(args: Array[String]) {
    // if lause on avaldis, mis tagastab mingi väärtuse
    val x = 
    if (2 == 2) 1
    else if (4 % 2 == 0) 2
    else 3

    // match avaldis on sarnane switch lausega, kuid see tagastab samuti väärtuse
    val y: String = x match {
      case 1 | 2 | 3 => "123"
      case 4 => "4"
      case _ => "-1"
    }


    for (i <- 1 to 10) {
      // i = 1, 2, ... , 10
      for (j <- 1 until 10) {
        // j = 1, 2, ... , 9
        for (k <- 1 to 10 if (k%2 == 0)) {
          // k = 2, 4, 6, 8, 10
        }
      }
    }
    // Eelmine on samaväärne sellega:
    for (i <- 1 to 10; j <- 1 until 10; k <- 1 to 10 if (k%2 == 0)) {}
    // Teine võimalus:
    for {
      i <- 1 to 10
      j <- 1 until 10
      k <- 1 to 10
      if (k%2 == 0)
    } {

    }

    // Parameeter i väärtustatakse iga kord, kui seda kasutatakse
    def callByName(i: =>Int): Unit = {
      println(i)
      println(i) // Siin ei pruugi i väärtus enam sama olla
    }
    var i = 0;
    // Parameetriks tuleb anda koodiplokk, mille tagastusväärtus
    // ehk ploki viimane rida läheb parameetri väärtuseks
    callByName(
      {i += 5; i}
    );
    // väljund:
    // 5
    // 10
  }
}


object Collections {
  // Massiivid
  var arr1: Array[Int] = new Array[Int](3);
  arr1(0) = 1; arr1(1) = 2; arr1(2) = 3;
  var arr2: Array[Int] = Array(1, 2, 3); // NB! mitte new Array(1, 2, 3)


  for (i <- arr1) println(i);
  /* 
   * Kui for tsükli keha ees on sõna "yield", tehakse
   * keha tagastusväärtustest uus kollektsioon, mis
   * on itereeritava (siin arr1) kollektsiooniga sama süüpi
   */
  var newArr: Array[Int] = for (i <- arr1) yield {
    val a = i
    val b = a*5
    a + b
  }


  type IntArray = Array[Int] // nagu C typedef


  // List on mittemuteeritav linked list
  val fruits: List[String] = List("lemon", "lime", "orange", "banana")
  val gems: List[String] = "diamond" :: "emerald" :: "ruby" :: Nil
  

  val lemon: String = fruits.head
  val orange: String = fruits(2) // fruits.apply(2)
  val lime_orange_banana: List[String] = fruits.tail
  val apple_lemon_lime_orange_banana: List[String] = "apple" :: fruits // "apple" +: fruits, fruits.prepended("apple")
  val lemon_lime_orange_banana_apple: List[String] = fruits :+ "apple"// fruits.appended("apple")


  // List("lemon", "lime", "orange", "banana", "diamond", "emerald", "ruby")
  val concat: List[String] = fruits ::: gems // fruits.concat(gems)
  // ::: töötab ainult tüübiga List, teiste kollektsioonide puhul tuleb kasutada operaatorit ++

  // List(List("lemon", "lime", "orange", "banana"), List("diamond", "emerald", "ruby"))
  val twoDimensionalList: List[List[String]] = List(fruits, gems)
  
  // List("lemon", "lime", "orange", "banana", "diamond", "emerald", "ruby")
  val oneDimensionalList: List[String] = twoDimensionalList.flatten


  // Mustrisobitus listidega
  fruits match {
    case "lemon" :: "lime" :: next => {
      /* 
       * See plokk käivitub siis, kui listi esimene element on "lemon" 
       * ja teine element "lime". Muutuja next: List[String] viitab 
       * ülejäänud listile peale esimest elementi. Kui listis peale "lemon" 
       * ja "lime" midagi rohkem pole, siis on selleks tühi list ehk Nil.
       */
    }
    case head :: next => {
      /* 
       * See plokk käivitub siis, kui listis on vähemalt üks element,
       * ükskõik milline. Sellele viitab muutuja head: String.
       */
    }
    case Nil => {
      // Tühi list
    }
  }


  val map: Map[String, Int] = Map("lemon" -> 30, "lime" -> 50, "banana" -> 70)
  // Viskab vea kui sellist võtit ei ole
  val lemonValue: Int = map("lemon") // map.apply("lemon")
  // Kontrollib, kas selline võti on olemas
  val checkedValue: Int = 
    if (map.contains("lime")) 
      map("lime") 
    else 
      0
  
  /*
   * Olgu list tüüpi List[A]. Mõned meetodid
   * 
   * Rakendab funktsiooni f iga listi elemendiga.
   *    def foreach(f: (A) => Unit): Unit
   * 
   * Rakendab funktsiooni f iga listi elemendiga ja moodustab listi tagastustväärtustest.
   *    def map[B](f: (A) => B): List[B]
   * 
   * Tagastab uue listi nendest vana listi elementidest, mis rahuldavad tingimust p.
   *    def filter(p: (A) => Boolean): List[A]
   * 
   * Tagastab true, kui kõik listi elemendid rahuldavad tingimust p.
   *    def forall(p: (A) => Boolean): Boolean
   * 
   * Rakendab kahekohalist funktsiooni f kumulatiivselt vasakult paremale ja tagastab viimase tulemuse
   * z on algväärtus, mis ei pea olema listi elementidega sama tüüpi
   * 
   *    def foldLeft[B](z: B)(f: (B, A) => B): B
   * 
   * Nagu foldLeft, kombineerib terve listi mingi kahekohalise operatsiooni abil üheks väärtuseks,
   * kuid see võib toimuda täiesti suvalises järjekorras. Listis peab olema vähemalt kaks elementi.
   * 
   *    def reduce(f: (A, A) => A): A
   * 
   * Jagab elemendid gruppidesse (listidesse) funktsiooni f tagastusväärtuse järgi
   *    groupBy[K](f: (A) => K): Map[K, List[A]]
   */

  // Muteeritavad kollektsioonid tuleb importida
  import scala.collection.mutable

  val mutableDoubles: mutable.ListBuffer[Double] = mutable.ListBuffer(4.0, 5.0, 6.0)
  mutableDoubles += 7.0 // mutableDoubles.addOne(7.0)
  mutableDoubles.insert(2, 9.0) // Sisestab 9.0 indeksile 2
  mutableDoubles.addAll(List(1.0, 2.0, 3.0)) // Lisab mistahes kollektsiooni lõppu
  mutableDoubles.insertAll(3, List(1.0, 2.0, 3.0)) // Sisestab mistahes kollektsiooni indeksite 3 ja 4 vahele

  val immutableDoubles: List[Double] = mutableDoubles.toList
}


class Car(
  var brand: String, 
  var model: String, 
  var year: Int) {
  /*
   * Klassi keha on ka (põhi)konstruktori keha. Siin saab defineerida
   * Väljad ja meetodid, kuid saab ka kirjutada mistahes koodi
   * (näiteks printimine).
   * Konstruktori parameetrid on samuti isendiväljad, neid ei ole
   * vaja eraldi väärtustada (s.t ei ole vaja kirjutada this.brand = brand
   * nagu Javas).
   * Scala klassides ei ole staatilisi välju ega meetodeid, selle asemel
   * on singleton objektid (võtmesõna object), kus kõik on staatiline.
   * 
   * Objekti loomine:
   * var/val muutuja_nimi: Car = new Car("Ford", "Sierra", 1990)
   */
  println("Creating a Car")

  // Defineerib uued väljad, lisaks nendele, mis on konstruktori parameetrites
  var numOfDoors: Int = 4
  val numOfWheels: Int = 4 // Erinevalt teistest väljadest ei saa seda muuta

  def display(numTimes: Int): Unit = {
    for (i <- 1 to numTimes) {
      println(year.toString + " " + brand + " " + model)
    }
  }

  // Lisakonstruktor. Igal konstruktoril peab olema erinev parameetrite nimekiri
  // Lisakonstruktori parameetritel ei ole piiritlejaid (var, val ja private)
  def this(brand: String, model: String, year: Int, numOfDoors: Int) {
    // Põhikonstruktori väljakutsumine. Iga lisakonstruktori esimene rida
    // peab olema mõne teise konstruktori väljakutsumine. Väljakutsutav konstruktor
    // peab koodis asuma eespool.
    this(brand, model, year)
    this.numOfDoors = numOfDoors
  }
}


/*
 * Alamklass. Põhikonstruktor peab välja kutsuma mõne Ülemklassi 
 * konstruktori. Lisakonstrukorid seda teha ei saa.
 * 
 * Parameetritel brand, model ja year ei ole ees sõna "var", kuna
 * need saadetakse ülemklassi konstruktorisse, kus need deklareeritakse
 * kui var väljad. Parameeter color on aga uus ning see deklareeritakse siin kui var
 */
class ThreeWheeledCar(
  brand: String, 
  model: String, 
  year: Int,
  var color: String) extends Car(brand, model, year) {

  // Sõna override saab kasutada ainult val väljadega, et nende
  // väärtust alamklassis muuta.
  override val numOfWheels: Int = 3
  // Ülemklassist pärit var välja muutmine, ei ole vaja kasutada sõnu override ja var
  numOfDoors = 2

  // Ülekaetud meetodil peab olema võtmesõna "override"
  override def display(numTimes: Int): Unit = {
  }
}


/*
 * Kui objektil x on meetod f, mis võtab vaid ühe parameetri p,
 * saab rakendada kui x.f(p) või x f p. Nii töötavad kõik binaarsed
 * operaatorit arvudega ja bool'idega. See tähendab, et neid operaatoreid
 * saab ka mistahes klassi jaoks üledefineerida. Näide operaatori + kohta:
 * 
 * class Custom {
 *    def +(other: Custom): Custom
 * }
 * 
 * val c1: Custom = new Custom()
 * val c2: Custom = new Custom()
 * val c3: Custom = c1 + c2 // sama mis c1.+(c2)
 */


/*
 * Isendiväljade, meetodite ja konstruktorite piiritlejad Scalas
 *    1) piiritleja puudub - sama mis Java public (Scalas see võtmesõna puudub). Nähtav igal pool.
 *       NB! Javas tähendab piiritleja puudumine nähtavust ainult paketi sees.
 *    2) protected - nähtav igale objektile sellest klassist ja selle alamklassidest 
 *       NB! Javas on protected nähtav samas paketis ja alamklassides väljaspool paketti
 *    3) private - nähtav igale objektile ainult sellest klassist.
 *    4) private[X] või protected[X] kus X tähistab paketti, klassi või singleton objekti.
 */
package outer {
  
  package inner {

    class InnerBase {
      private val pri = 0
      private[this] val pri_this = 0
      private[inner] var pri_inner = 0 // Nähtav paketis inner

      println(this.pri)
      println((new InnerBase().pri))

      println(this.pri_this)
      // println((new Base()).pri_this) ei kompileeru

      protected val prt = 0
      protected[this] val prt_this = 0
      protected[outer] val prt_outer = 0 // Nähtav paketis outer, väljaspool paketti on sama mis protected

      // println((new Base()).prt_this) ei kompileeru
    }

    class InnerDerived extends InnerBase {
      // println(this.pri) ei kompileeru

      println(this.prt)
      println((new InnerDerived()).prt)
      // println((new Base()).prt) ei kompileeru
      
      println(this.prt_this)
      // println((new InnerDerived()).prt_this) ei kompileeru
    }

    class InnerClass {
      println((new InnerBase()).pri_inner)
    }
  }

  // import laused võivad olla kõikjal
  import inner.InnerBase
  import inner.InnerDerived
  // Alternatiiv: import inner._

  class OuterDerived extends InnerBase {
    // println(this.pri_inner) ei kompileeru
  }

  class OuterClass {
    println((new InnerBase()).prt_outer)
  }
}

import outer.inner.InnerBase

class OutsideDerived extends InnerBase {
  println(this.prt)
  println(this.prt_this)
  println(this.prt_outer)
  println((new OutsideDerived().prt_outer))
  // println((new InnerBase()).prt_outer) ei kompileeru
}


/* 
 * Trait vs Abstraktne klass 
 * 
 * 1) Nii trait'ist kui ka abstraktsest klassist ei saa luua objekte, neil
 *    saavad olla vaid alamklassid/alamtraitid. Neil mõlemal võivad olla 
 *    väärtustamata väljad ja implementeerimata meetodid.
 * 2) Trait'il ei ole konstruktoreid, abstraktsel klassil on
 * 3) Üks klass/trait võib olla alamklass/alamtrait mitmele trait'ile, kuid ainult
 *  	ühele klassile.
 * 4) Kui trait t on klassi c alamtrait, siis trait'i t saavad implementeerida ainult
 *    c alamklassid
 * 4) Objekti loomisel saab sellele traite kaasa anda:
 *    val cbj = new Object() with Trait
 */
trait Trait { // abstract class Class(var x: Int, ...)
  var field: Int = 0 // Mitteabstraktne väli
  var abstractField: Int // Abstraktne väli - see tuleb alamklassis/alamtrait'is väärtustada

  def method() { // Mitte-abstraktne meetod
    println("Hello World!")
  }
  def abstractMethod() // Abstraktne meetod
}


/*
 * Geneerilised tüübid
 * 
 * Kirjutatakse klassi või funktsiooni nime järele:
 * 
 * def method[T1, T2, ...](x: T1, ...): T2
 * class Name[T1, T2, ...](...)
 * 
 * [T] - ükskõik mis tüüp
 * [T <: A] - iga tüüp, mis on tüübi A alamtüüp
 * [T >: A] - iga tüüp, mis on tüübi A ülemtüüp
 * 
 * Kovariantne klass: Class[+T] - kui A <: B, siis Class[A] <: Class[B]
 * Kontravariantne klass: Class[-T] - kui A <: B, siis Class[B] <: Class[A]
 * Invariantne klass Class[T] - ei kovariantne ega kontravariantne.
 */


class Demo[GenericType] private // Põhikonstruktori piiritleja
(
  private var field1: String,
  protected val field2: GenericType
) 
// extends järel olev nimi võib olla klass või trait, ülejäänud on kõik trait'id
extends Trait /* with Trait2 with Trait3 ... */ {

  final def finalMethod() {} // final tähendab, et seda ei saa alamklassis üle katta

  /*
   * Kui abstraktse klassi või trait'i alamklass ei väärtusta kõiki
   * abstraktseid välju või ei implementeeri kõiki abstraktseid meetodeid,
   * siis tuleb ka see klass kuulutada abstraktseks.
   */

  var abstractField = 0 // Abstraktse välja väärtustamine

  def abstractMethod() { // Abstraktse meetodi implementeerimine
    println("Hello World!")
  }
}


/*
 * Singleton objekti, millel on klassiga sama nimi, nimetatakse
 * companion objektiks. See pääseb ligi kõikidele väljadele ja meetoditele
 * (ka nendele mis on privaatsed)
 */
object Demo {
  val demo: Demo[Double] = new Demo("", 0.0) // Privaatne konstruktor
}


object ImplicitAndCase {
  case class Person(name: String, age: Int) // vaikimisi (val name: String, val age: Int)
  val person = Person("Mohammed", 50) // Pole vaja sõna "new"

  // Saab kasutada mustrisobitust
  person match {
    case Person("Mohammed", 50) =>
    case Person("Ibrahim", 23) =>
    case Person(name, age) => // Ükskõik milline Person
  }

  
  // Näide Scala standardteegist: trait Option, millel
  // on kaks alam case klassi - Some(Int) ja None
  val some: Option[Int] = Some(5)
  val none: Option[Int] = None

  // Väärtuse kättesaamine mustrisobituse abil
  val patternMatchingVal = some match {
    case Some(value) => value
    case None => 0
  }

  // Väärtuse kättesaamine if lausega
  val ifVal = if (some.isEmpty) some.get else 0


  /*
   * Kui funktsiooni parameetrite grupi ees on sõna "implicit", 
   * siis on kõik parameetrid selles grupis implicit parameetrid.
   * Selliseid parameetreid ei ole vaja eraldi kaasa anda, kui
   * funktsioon leiab lähedusest implicit muutuja.
   */
  implicit val x: Int = 5
  def func(a: Int)(implicit b: Int): Int = a + b

  val sum: Int = func(5)


  /*
   * implicit funktsioonide abil saab väärtustada muutujat
   * tüübist S avaldisega, mis tagastab tüübi T
   */
  implicit def booleanToString(b: Boolean): String = b match {
    case true => "true"
    case false => "false"
  }
  val boolStr: String = 3 != 5 // Rakendab automaatselt booleanToString(3 != 5)


  /*
   * implicit klasside abil saab teistele klassidele uusi välju ja meetodeid
   * lisada.
   * 
   * 1) implicit klass peab olema defineeritud mõne singleton objekti või klassi sees
   * 2) implicit klassi konstruktor võtab ainult ühe parameetri
   */
  implicit class IntOps(x: Int) {
    def display(numTimes: Int): Unit = {
      for (i <- 1 to numTimes) {
        println(x)
      }
    }
  }
  val i: Int = 5
  i.display(10)
}
