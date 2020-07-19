program pascal_cheatsheet;
uses crt; // Kõik unit'id, mida see programm kasutab (import laused).

(* Mitmerealine
   kommentaar *)

{ Mitmerealine
   kommentaar }

// Üherealine kommentaar

// Siin tuleb deklareerida goto lausete sihtpunktid. NB! Enne globaalseid const deklaratsioone.
label 1;


// Globaalsed konstantide deklaratsioonid
const
    int_type = 5;
    real_type = 2.7182818;
    char_type = '+';
    str_type = 'Hello World';
    set_type = set of (A, E, I, O, U);
    ptr_type = NIL;


// Funktsioon. Parameetriteks on: num1, num2 - pass by value, str - pass by reference. Tagastab integeri.
function func(num1, num2: integer, var str: string): integer;
begin
    // ...
    func := int_type; // return käsk. Alternatiiv oleks result := 5.
end;


// Protseduur on sama mis funktsioon, kuid ei tagasta midagi. Sellel protseduuril puuduvad
// ka parameetrid, mistõttu võib siin tühjad sulud ära jätta (kuid võib ka kirjutada).
procedure basicsDemo;
// Kõik protseduuri/funktsiooni lokaalsed deklaratsioonid asuvad peale rida
// function .../procedure ... ja enne võtmesõna 'begin'.
// Siin on lokaalsed muutujad, kuid võib lisada ka konstante ja tüübidefinitsioone.
var
    x, i : integer;
    str : string;
    // Stringi pointer
    ptr : ^string; 
    // 4-kohaline stringide massiiv (deklaratsioon + algväärtustamine), indekseeritakse
    // alates 1-st. Kui on vaja 0-st indekseerida, siis võib kirjutada sarr : array[0..3] of string.
    sarr : array[1..4] of string = ('one', 'two', 'three', 'four');
    n : 3..8 = 5; // Arv vahemikust 1 - 8. Kui kirjutada n : 3..8 = 10, siis annab vea.
    c : 'a'..'z'; // Väiketäht vahemikust 'a' kuni 'z'. 
begin
    writeln('Sisesta arv'); // Print käsk
    readln(x); // Loeb käsurealt sisendi ja salvestab tulemuse muutujasse x.
    
    x := func(num1 - 3, num2 + 7, str);
    // x := func(num - 3, num + 7, 'Hello!') annab vea, sest func kolmas argument on deklareeritud kui var!

    // Massiivi elementide kättesaamine ja muutmine.
    str := sarr[1];
    sarr[3] := 'THREE';

    ptr := @str; // Stringi aadressi võtmine ja pointerisse salvestamine.
    ptr^ := 'New Value'; // Pointeri järgi väärtustamine.
    str := ptr^; // Pointeri järgi väärtuse võtmine.
    ptr := NIL; // Null pointer.


    // if lause.
    if (x = 10) then // = on sama mis javas ==
    begin
        // ... Kui koodiplokis on ainult üks rida, ei ole sõnu begin ja end vaja.
    end;
    else if (x <> 15) // <> on sama mis javas !=
        // ...
    else
        // ...


    // Switch lause. Sõna break ei ole vaja.
    case (x) of
        1..17: { Kui i >= 1 ja i <= 17 ...}
        18: {...}
        19..21: {...}
        else {...}
    end;


    // while tsükkel.
    while i < 10 do
    begin
        // ...
    end;


    // for tsükkel.
    for i := 1 to 10 do
    begin
        // ...
    end;


    // do tsükkel. NB! Tsükkel jätkub siis, kui tsükli tingimus on FALSE!
    repeat
        // ...
    until (i = 0);


    // Erindihaldus
    try
        // ...
    except
        on EConvertError do
            // ...
        on EZeroDivideError do
            // ...
        on e : Exception do // saame salvestada erindi muutujasse e.
            writeln(e.message);
    end;
end;


// Võtmesõna 'type' järele tulevad programmeerija loodud tüübidefinitsioonid.
// Antud juhul globaalsed, kuna need ei asu ühegi funktsiooni ega protseduuri
// deklaratsiooni järel.
type
    // C typedef stiilis tüübidefinitsioon.
    str30 = string[30]; // String max pikkusega 30.

    // Klass ilma meetoditeta (nagu C struct).
    movie = record
        title : str30;
        year : integer;
        runtimeHrs : double;
    end;


procedure createMovieRecord(movie_title : str30, production_year : integer, movie_runtimeHrs : double) : movie;
var
    movie_var : movie;
begin
    movie_var.title := movie_title;
    movie_var.year := production_year;
    movie_var.runtimeHrs := movie_runtimeHrs;

    createMovieRecord := movie_var;
end;


procedure writeMovieRecordToFile(var movie_var : movie, file_path : string);
var
    file_var : file of movie;
begin
    AssignFile(file_var, 'file_path');
    
    if FileExists('file_path') then
        Reset(file_var); // Avab olemasoleva faili.
    else
        Rewrite(file_var); // Loob ja avab uue faili.

    // Määrab, mitmendale kohale failis kirjutada. FileSize(file_var)
    // annab recordite koguarvu failis, seega tuleb kirjutada viimase salvestatud
    // recordi järele. Määratud positsiooni on võimalik kätte saada nii: FilePos(file_var).
    Seek(file_var, FileSize(file_var));

    // Faili kirjutamine. Kui oleks vaja lugeda, tuleb kirjutada
    // Read(file_var, movie_var);
    Write(file_var, movie_var);

    CloseFile(file_var);
end;


function readMovieFromFile(position: integer, file_path : string) : movie;
var
    file_var : file of movie;
    movie_var : movie;
begin
    AssignFile(file_var, 'file_path');
    // Kuna siin toimub failist lugemine, peame avama olemasoleva faili.
    Reset(file_var);
    Seek(file_var, position);
    Read(file_var, movie_var);
    CloseFile(file_var);
    readMovieFromFile := movie_var;
end;


procedure textFileIOdemo;
const
    file_path : string = 'path/to/file';
var
    file_var : TextFile;
    str : string;
    chr : char;
begin
    AssignFile(file_var, 'file_path');
    Rewrite(file_var); // Reset(file_var)

    if not Eof(file_var) then
    begin
        // Üksiku sümboli kirjutamine/lugemine.
        Write(file_var, 'X');
        Read(file_var, chr);

        // Rea kirjutamine/lugemine.
        Writeln(file_var, 'New line written');
        Readln(file_var, str);
    end

    CloseFile(file_var);
end;


procedure fileStreamDemo;
const
    file_path : string = 'path/to/file';
var
    stream : TFileStream;
    str : string = 'Hello World!';
    n : integer = '23';
    e : double = 2.72;
begin
    // Tekitab uue faili.
    stream = TFileStream.Create(file_path, fmCreate);

    // Kirjutab muutujate str, n ja e väärtused faili.
    stream.WriteBuffer(str, sizeof(str));
    stream.WriteBuffer(n, sizeof(n));
    stream.WriteBuffer(e, sizeof(e));

    stream.Free;

    // Avab olemasoleva faili.
    stream = TFileStream.Create(file_path, fmOpenRead);

    // Loeb failist väärtused ja salvestab muutujatesse str, n ja e.
    // NB! Lugemine peab toimuma samas järjekorras mis kirjutamine!
    stream.ReadBuffer(str, sizeof(str));
    stream.ReadBuffer(n, sizeof(n));
    stream.ReadBuffer(e, sizeof(e));

    stream.Free;
end;


type
    // -------------------- OOP klassid --------------------
    //
    // Iga klass on TObject alamklass. class(TObject) asemel võib kirjutada ka
    // lihtalt class, tähendus jääb samaks. Klassile ObjectDef saab luua alamklassi nii:
    // Derived = class(ObjectDef).
    //
    // -------------------- Piiritlejad --------------------
    //
    // public: members have no access restrictions.
    // protected: members are accessible within the unit where its class is.
    // declared and by any descendent class in any unit.
    // private: visible only in the unit where it is declared.
    // strict private: members are visible only to the class itself.
    // strict protected: members are visible only to the class and its descendants.

    ObjectDef = class(TObject)

    private // Kõik privaatsed väljad ja meetodid.

        _field1 : integer;
        _field2 : string;

        // Setter meetod, mis eelnevalt kontrollib sisendit.
        procedure setfield2(f2 : string);
        begin
            if (f2 <> 'Unallowed value') then
                _field2 := f2;
        end;

        function getfield2: string;
        begin
            result := _field2 + '!!';
        end;

        // Kui alamklassis on vaja meetod üle katta, tuleb ülemklassis
        // märkida meetodi deklaratsiooni järele ... virtual;
        // Alamklassis tuleb ülekaetud meetodi deklaratsiooni järele märkida ... override;
        function virtual_method(n: integer, str : string): string; virtual;
        begin
            // ...
        end;

        // Protseduuri deklaratsioon ilma definitsioonita.
        procedure proc(n: integer, str : string);

    public // Kõik avalikud väljad ja meetodid.

        field3 : double;

        constructor Create(f1 : integer, f2 : string, f3 : double);
        begin
            // Kutsub ülemklassi konstruktorit (antud juhul tühi konstruktor).
            // See peab olema konstruktori kõige esimene rida.
            inherited Create;
            _field1 := f1;
            _field2 := f2;
            field3 := f3;
        end;

        // Ülemklassi destruktor TOcject.Destroy on virtual, sellepärast on
        // omaloodud destruktor vaja märkida ülekaetuks.
        destructor Destroy; override;
        begin
            // ...
            // Kutsub ülemklassi destruktorit.
            // See peab olema destruktori kõige viimane rida.
            inherited Destroy
        end;

        // getter + setter välja _field1 jaoks. Kui write _field1 välja kommenteerida,
        // saab küll välja _field1 lugeda, kuid mitte muuta.
        property Field1 : integer read _field1 write _field1;
        // Sama asi _field2 jaoks, kuid kasutab
        // meetodeid getfield2 ja setfield2.
        property Field2 : string read getfield2 write setfield2;
    end;

// Siin saab defineerida eelnevalt deklareeritud klassi ObjectDef meetodi.
// Sama saab teha ka konstruktorite ja destruktoritega.
procedure ObjectDef.proc(n: integer, str : string);
begin
    // ...
end;


type
    day = (Mon, Tue, Wed, Thu, Fri, Sat, Sun) // enum
var
    dayvar : day = Wed;
    // Ilma eelneva tüübidefinitsioonita võib kirjutada ka
    // dayvar : (Mon, Tue, Wed, Thu, Fri, Sat, Sun) = Wed;
    obj : ObjectDef;


begin // Põhiprogrammi algus.
    goto 1;
    writeln('Seda rida ei täideta, sest goto lause ütleb, et mine sellele reale, kus on 1: ...');
    1 : writeln('Tänu goto lausele jõuab programm selle reani');

    // Objektide loomine ja kasutamine.
    obj := ObjectDef.Create(5, 'Hello World!', 3.14);
    // Kasutab propertyt Field1, et kätte saada obj._field1
    x := obj.Field1;
    // Kasutab propertyt Field2, mis omakorda kutsub välja obj.setfield2('New value')
    obj.Field2 := 'New value';
    // Property Field2 kutsub välja obj.getfield2, mis lisab stringile 'Hello World!' lõppu '!!'
    str := obj.Field2;
    // Muudab avalikku välja.
    obj.field3 := 2.78;
    obj.Free; // Rakendab destruktorit.
    obj := NIL; // nil on null pointer.
        
    readkey; // Ootab, kuni kasutaja vajutab mingit klahvi.
end. // Põhiprogrammi lõpp. NB! lõpus peab olema punkt!
