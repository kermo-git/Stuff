/* * * * * * * * *
 * SISSEJUHATUS  *
 * * * * * * * * */


// Seda teksti näeb siis, kui brauseris valida developer tools -> console.
// Node.js'i käsureal töötab kui tavaline print käsk.
console.log("Hello world!");
// Kuvab teate väikses tekstiaknas. Võib ka kirjutada alert("Hello world!");
window.alert("Hello world!");

// Muutujad
var x = "string" + "string" // "stringstring"
x = 16 + 4 + "string" // "20string"
x = "string" + 16 + 4 // "string164"

x = true;
x = false;
x = null;
x = undefined;

// Tüübi kontrollimine
typeof x // undefined

// tsüklid ja if lause
while (x < 10) {
    break;
}
do {
    break;
} while (x < 10)

for (x = 0; x < 10; x++) {

}
if (x < 3) {

}
else if (x < 6) {

}
else {
    
}
// switch lause. x võib olla kas arv või string
switch (x) {
    case 1:
        break;
    case 2:
        break;
    case 3:
        break;
    default:
}


/* * * * * * * * * 
 * FUNKTSIOONID  *
 * * * * * * * * */


// Tavaline funktsioon
function add(x, y) {
    return x + y;
} // Siia pole semikoolonit vaja.
x = add(y, z);

// Funktsioon, mis on salvestatud muutujasse f
var f = function(x, y) {
    return x + y;
}; // NB! siin on semikoolon vajalik!
x = f(y, z);

/**
 * self-invoking function - anonüümne funktsioon, mis jookseb
 * ainult ühe korra. Süntaks:
 * (function(x_1, ... , x_N){...})(x_1, ... , x_N);
 * Antud juhul kahe argumendiga.
 */
(function(x, y){
	alert(x + " " + y);
})("Hello" +  "World!");

// self-invoking function, mis tagastab uue funktsiooni ja salvestab selle muutujasse count.
var count = (
    function () {
        var counter = 0;

        return function () {
            counter += 1;
            return counter
        }
    } 
)();
var count_1 = count(); // 1
var count_2 = count(); // 2
var count_3 = count(); // 3


/* * * * * * * 
 * OBJEKTID  *
 * * * * * * */


/**
 * JavaScripti objekt on võti - väärtus paaride kogumik, kus võtmeteks
 * (ingl. property) on nimed ning väärtused võivad olla arvud, sõned,
 * funktsioonid või isegi teised objektid. On kolm moodust objektide
 * loomiseks: objekti literaal, konstruktor funktsioon ja "traditsiooniline klass".
 */

/** 
 * Objekti literaal
 * var name = {key_1:value_1, ... , key_N:value_N};
 */
var object = {
    var1: 5,
    var2: 6,

    method: function(x, y) {
        return object.var1 + x + y;
    }
};

// Konstruktor funktsioon
function function_class(var1, var2) {
    this.var1 = var1;
    this.var2 = var2;

    this.method = function(x, y) {
        return this.var1 + x + y;
    }
}
object = new function_class(5, 6);
object instanceof function_class; // true

// "Traditsiooniline" klass ja selle alamklass.
class real_class {
    constructor(var1, var2) {
        this.var1 = var1;
        this.var2 = var2;
    }
    method(x, y) {
        return this.var1 + x + y;
    } 
}
class extended_class extends real_class {
    constructor(var1, var2, var3) {
        super(var1, var2);
        this.var3 = var3;
    }
    method(x, y) {
        return super.method(x, y) + this.var3;
    }
}
object = new extended_class(5, 6, 7);

// Objekti kopeerimine
var object2 = Object.create(object);
// Tühi objekt
object2 = new Object();

// Isendimeetodi rakendamine
var m = object.method(z, y);
// Olemasoleva property kättesaamine
var p = object.var1;
var p = object["var1"];
// Uue property lisamine (Olemasoleva property ülekirjutamine)
object.var4 = 23;
object["var4"] = 23;
// Property kustutamine
delete object.var4;

// For tsükliga üle property'te itereerimine
for (x in object) {
    var p = object.x;
}

// Ühe objekti isendimeetodit saavad kasutada ka teised
// objektid meetodi call() abil:
var person = {
    fullName: function(city, country) {
      return this.firstName + " " + this.lastName + "," + city + "," + country;
    }
}
var person1 = {
    firstName:"John",
    lastName: "Doe"
}
var person2 = {
    firstName:"Jack",
    lastName: "Lemon"
}
// Meetodi fullName uus "omanik" on meetodi call() esimene argument.
var name1 = person.fullName.call(person1, "Oslo", "Norway"); // "John Doe,Oslo,Norway"
var name2 = person.fullName.call(person2, "LA", "USA"); // "Jack Lemon,LA,USA"


/* * * * * * * * * * * *
 * ARROW FUNKTSIOONID  *
 * * * * * * * * * * * */


/*
 * Arrow funktsioonid ei sobi objekti meetoditeks, kuna nendes ei saa kasutada.
 * this võtmesõna. Süntaks:
 *
 * (param1, param2, …, paramN) => { laused } 
 * (param1, param2, …, paramN) => avaldis
 * viimane on samaväärne sellega: 
 * (param1, param2, …, paramN) => { return expression; }
 *
 * Kui on ainult üks parameeter pole sulge vaja:
 * (param) => { laused }
 * param => { laused }
 *
 * Ilma parameetriteta funktsioonil peavad olema tühjad sulud.
 * () => { laused }
 */



/* * * * *
 * HTML  *
 * * * * */


// Leiab HTML elemendi mingi atribuudi järgi ja tagastab selle JavaScripti objektina.
var element = document.getElementById("demo"); // <tag id="demo">
element = document.getElementsByTagName("demo"); // <tag name="demo">
element = document.getElementsByClassName("demo"); // <tag class="demo">

// HTML elemendi loomine.
var button = document.createElement("button");// Tekitab elemendi <button>
btn.innerHTML = "CLICK ME";                   // Lisab nupule teksti (sama mis <buton>CLICK ME</button>)
document.body.appendChild(btn);               // Lisab nupu elementi <body> 

// HTML elemendi muutmine
element.innerHTML = "new html content";
element.className = "new_class";
element.id = "new_id";
element.src = "image.gif"
element.style.fontFamily = "Impact";
element.style.fontSize = "25px";
element.style.color = "red";
element.style.backgroundColor = "yellow"; 

// Kui elemendi peal tehakse hiireklõps, käivitub see funktsioon.
element.onclick = function () {
    // Tee midagi
}


/* * * * * * * 
 * MASSIIVID *
 * * * * * * */


/**
 * Massiivi loomiseks on kaks võimalust: konstruktori
 * new Array(x_1, ..., x_2) abil või kandiliste sulgudega
 * [x_1, ..., x_2]. Soovitatav on viimane variant, sest
 * new Array(67, "string", 45) on sama mis [67, "string", 45]
 * ning new Array("string") on sama mis ["string"], kuid
 * new Array(67) tekitab 67-kohalise tühja massiivi!
 */
var arr = [4, 5, 6];
var last = arr[2]; // 6
var l = arr.length; // masiivi pikkus (3)
// Lisab massiivi lõppu elemendi 6 ja tagastab uue massiivi pikkuse (4)
var new_len = arr.push(7);
// Lisab massiivi algusesse elemendi 6 ja tagastab uue massiivi pikkuse (5)
new_len = arr.unshift(3);
// Eemaldab massiivi lõpust elemendi ja tagastab selle (7).
var removed = arr.pop();
// Eemaldab massiivi algusest elemendi ja tagastab selle (3).
removed = arr.shift();

arr.sort() // Sorteerib sõnesid tähestiku järjekorras.

/*
 * Annab sort() meetodile argumendiks funktsiooni f, millega
 * elemente (antud juhul arve) võrrelda. Sorteerib nii, et
 * kui f(a, b) < 0, siis a asub sorteeritud järjendis enne
 * elementi b. Kui f(a, b) > 0, siis asub b enne elementi a.
 * Kui f(a, b) == 0, siis võivad a ja b paikneda üksteise suhtes
 * ükskõik kuidas.
 */
arr.sort(function(a, b){return a - b});

/*
 * Rakendab listi arr iga indeksi i korral funktsiooni 
 * forEachFunc(arr[i], i, arr). Ei tagasta midagi. 
 */
function forEachFunc(value, index, array) {
    console.log(value);
}
arr.forEach(forEachFunc);

/*
 * Rakendab listi arr iga indeksi i korral funktsiooni 
 * mapFunc(arr[i], i, arr). Tagastab uue listi
 * funktsiooni tagastusväärtustest.
 */
function mapFunc(value, index, array) {
    return 2*value;
}
// Kui arr on [4, 5, 6], siis arr2 on [8, 10, 12]
var arr2 = arr.map(mapFunc);


/* * * * * * * * * * * * * * * * * * * *
 * MUUTUJATE TÜÜBID: var, let ja const *
 * * * * * * * * * * * * * * * * * * * */


/*
 * Variables declared by let have their scope in the block for which they are defined,
 * as well as in any contained sub-blocks. In this way, let works very much like var.
 * The main difference is that the scope of a var variable is the entire enclosing function.
 */
function varTest() {
    var x = 1;
    {
        var x = 2;  // same variable!
        console.log(x);  // 2
    }
    console.log(x);  // 2
}
  
function letTest() {
    let x = 1;
    {
        let x = 2;  // different variable
        console.log(x);  // 2
    }
    console.log(x);  // 1
}

/*
 * At the top level of programs and functions, let, unlike var,
 * does not create a property on the global object.
 */
var x = 'global';
let y = 'global';
console.log(this.x); // "global"
console.log(this.y); // undefined

// const muutujad töötavad täpselt samamoodi nagu let muutujad, kuid nende väärtust ei saa muuta.
const z = 5;
z = 6; // TypeError
