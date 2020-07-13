#!/bin/bash

################
# SISSEJUHATUS #
################

# Aritmeetilised operaatorid:
# +, -, *, /, %, ++, --
# Loogilised operaatorid:
# &&, ||, !
# Bitioperaatorid
# &, |, ^, ~, <<, >>


# Muutujad. NB! võrdusmärgi ümber tühikuid ei ole!
a=5
b=6
# Aritmeetiline avaldis. Võib ka kirjutada c=`expr $a + $b`
c=$((a+b))
# Prindib reavahetusega.
echo $a ja $b summa on $c
# Prindib ilma reavahetuseta.
echo -n $a ja $b summa on $c


# Loeb käsurealt kasutaja sisendi ja salvestab muutujasse a.
read a
# Sama asi, aga enne kuvab teate "Sisesta a : ".
read - p "Sisesta a : " a


# Kui if lauses või while tsüklis on vaja võrrelda
# arve a ja b, saab seda teha kahel erineval viisil:

# Võrdne:             [ $a -eq $b ] - (($a == $b))
# Mittevõrdne:        [ $a -ne $b ] - (($a != $b))
# Väiksem kui:        [ $a -lt $b ] - (($a < $b))
# Väiksem või võrdne: [ $a -le $b ] - (($a <= $b))
# Suurem kui:         [ $a -gt $b ] - (($a > $b))
# Suurem või võrdne:  [ $a -ge $b ] - (($a >= $b))


# While tsükkel
i=0
while [ $i lt 10 ] 
do
    echo Tsükkel nr. $i
    ((++i)) # i=`expr $i + 1`
done


# For tsükkel
for i in 1 2 3 4 5 6 7 8 9 10 
do
    if [ $i eq 5 ]
    then
        continue
    elif (($i == 8))
    then
        break
    else
        echo Tsükkel nr. $i
    fi
done


# Switch lause
CARS="bmw"
case $CARS in
    "mercedes")
        echo "Headquarters - Affalterbach, Germany" ;; 

    "audi")
        echo "Headquarters - Ingolstadt, Germany" ;;

    "bmw")
        echo "Headquarters - Chennai, Tamil Nadu, India" ;; 
esac




#########
# SÕNED #
#########

str="yippiekayyeah"

# Sõne pikkus
str_length=${#str} # 13

# Alamsõne alates 3. indeksist pikkusega 4 sümbolit.
# NB! indekseerimine algab 0-st!
sub_str=${str:3:4} # "piek"

# Tükelda str punkti (-d ".") järgi juppideks ja võta teine (-f 2) jupp.
# NB! Indekseerimine algab 1-st.
str="foo.bar.whatever"
split=$(echo $str | cut -d "." -f 2) # bar

# Sõnede võrdlemine if lauses või while tsüklis:

# str1 ja str2 on võrdsed:  [ $str1 = $str2 ]
# str1 ja str2 on erinevad: [ $str1 != $str2 ]
# str1 on lühem kui str2:   [ $str1 \< $str2 ]
# str1 on pikem kui str2:   [ $str1 \> $str2 ]
# str on mittetühi:         [ -n $str ]
# str on tühi:              [ -z $str ]




#############
# MASSIIVID #
#############

# Massiivi võib deklareerida, aga ei pea.
declare -a ARRAY

# Massiivi indekseerimine algab 0-st. Indeks võib olla kuitahes suur (aga mitte <0), veateadet ei anna.

# Massiivi loomine.
ARRAY=(value1 value2 value3 value4)
# Massiivi loomine etteantud indeksitega. Indeksid, mida ei defineerita, jäävad tühjaks.
ARRAY=([2]=value1 [4]=value2 [7]=value7)
# Indeksil 5 oleva väärtuse muutmine.
ARRAY[5] = new_value5

# Massiivi esimene element.
first=${ARRAY}
# Element indeksil 5.
Nth=${ARRAY[5]} 

# Prindib kõik massiivi elemendid.
echo ${ARRAY[@]}        
echo ${ARRAY[*]}
# Prindib kõik elemendid alates indeksist 5.  
echo ${ARRAY[@]:5}
# Prindib indeksist 5 alates 8 elementi.
echo ${ARRAY[@]:5:8}

# Elementide arv massiivis.
array_size=${#ARRAY[@]}     
array_size=${#ARRAY[*]}
# 5-nda elemendi pikkus.
length_of_5th=${#ARRAY[5]}

# Massiivide ARRAY_1 ja ARRAY_2 liitmine.
NEW_ARRAY=(${ARRAY_1[@]} ${ARRAY_2[@]})
# Ühele massiivile teise massiivi liitmine.
ARRAY += (new_value1 new_value2 new_value3)
# Uute elementide massiivi sisestamine indeksite 1 ja 2 vahele.
# Võib salvestada ka uude muutujasse, sellisel juhul jääb vana
# massiiv ARRAY muutumatuks.
ARRAY=(${ARRAY[@]:0:2} new_value4 new_value5 ${ARRAY[@]:2})

# Kustutab indeksil 5 oleva elemendi. NB! Indeks 5 jääb tühjaks, teised indeksid ei muutu.
unset ARRAY[5]
# Kustutab terve massiivi.
unset ARRAY

# Käi for tsükliga massiiv läbi ja prindi iga element välja.
for i in ${ARRAY[@]}
do
    echo $i
done




################
# FUNKTSIOONID #
################

# Funktsioonile argumentide andmine.
args_demo() {
    echo $1 # Prindib esimese argumendi
    echo $2 # Prindib teise argumendi
}
# Kutsub funktsiooni args_demo välja argumentidega foo ($1) ja bar ($2)
args_demo foo bar




# On kolm võimlust, kuidas funktsioon saab midagi tagastada.


# 1) return käsk
return_demo_1() {
    return 64
}
return_demo_1
# $? sisaldab viimati väljakutsutud funktsiooni return käsu järel olevat väärtust.
echo $?


# 2) Läbi mingi käsu, näiteks echo, cat ja grep.
return_demo_2() {
    echo "Hello World!"
}
# Prindib "Hello World!"
return_demo_2
# Ei prindi, vaid hoopis salvestab väärtuse "Hello World!" muutujasse return_value.
# Töötab ka paljude teiste käskudega, näiteks cat ja grep.
return_value=$(return_demo2)


# 3) Läbi globaalse muutuja
return_demo_3() {
    # Tekitab globaalse muutuja var
    var="Hello World!"
}
# Käivitab funktsiooni ja prindib muutuja var väärtuse "Hello World!".
return_demo_3
echo $var



# Globaalsed ja lokaalsed muutujad.
# var1 ja var2 on globaalsed muutujad.
var1="global 1"
var2="global 2"

# Teine viis, kuidas funktsiooni definitsiooni kirjutada.
# NB! sellisel juhul ei ole funktsiooni nime järel sulge!
function local_global_demo {
    # Seda muutujat saab kasutada ainult funktsiooni sees
    # ja see ei lähe sassi väljaspool funktsiooni defineeritud
    # samanimelise muutujaga var1.
    local var1="local 1"

    var1="changed 1" # Muudab "local 1" -> "changed 1"
    var2="changed 2" # Muudab "global 1" -> "changed 2"
}

# Käivitab local_global_demo
local_global_demo

# Prindib "global 1", sest var1 oli local_global_demo sees lokaalne muutuja.
echo $var1
# Prindib "changed 2", sest local_global_demo muutis seda.
echo $var2




# See funktsioon on sama nimega kui käsk ls,
# mis tavaliselt prindib kõikide failide ja kataloogide
# loetelu. Et need kaks omavahel sassi ei läheks, saab viimase
# jaoks kasutada võtmesõna command.
ls {
    echo "Hello World!"
}

# Prindib Hello World!
ls
# Prindib kõikide failide ja kataloogide loetelu.
command ls




########################
# FAILID JA KATALOOGID #
########################

# Operaatorid
# -b: checks wether a file is a block special file or not.
# -c: checks wether a file is a character special file or not.
# -d: kas selline kataloog eksisteerib?
# -e: kas selline fail eksisteerib?
# -r: checks wether the given file has read access or not.
# -w: checks wether the given file has write access or not.
# -x: checks wether the given file has execute access or not.
# -s: kas faili suurus on > 0?

DIR=/gpfs/hpc/home/area51
FILE=$DIR/topsecret.txt

if [ -d $HOME ] 
then 
    echo Kataloog $HOME on olemas.
else
    echo Kataloogi $HOME ei ole olemas.
fi

if [ -e $FILE ] 
then 
    echo Fail $FILE on olemas.
else
    echo Faili $FILE ei ole olemas.
fi

# Protseduurid failide ja kaustadega

# Prindib kõikide failide ja kataloogide loetelu kataloogis DIR.
ls $DIR
# Sama asi praeguses kataloogis
ls
# Tekita kataloog DIR
mkdir $DIR
# Liigu kataloogi DIR
cd $DIR
# Liigu praegusest kataloogist välja ühe taseme võrra
cd ..
# Kustuta fail FILE
rm $FILE
# Kustuta kataloog DIR ja kogu selle sisu
rm -r $DIR
# Kopeeri fail FILE asukohta DEST
cp $FILE $DEST
# Kopeeri kataloog DIR (koos sisuga) asukohta DEST
cp -r $DIR $DEST
# Liiguta fail/kataloog X asukohta DEST
mv $X $DEST
# Nimeta ümber fail/kataloog X nimega Y
mv $X $Y

# Loe kokku, mitu korda esineb tekst "line" failis FILE
# ja salvesta tulemus muutujasse count.
count=$(grep -c "line" $FILE)

# Olgu kataloogis /path/to/ failid file1.txt ja file2.txt.
# Prindime for tsükliga kõik failinimed välja.
FILES=/path/to/*
for f in $FILES
do
    echo $f
done
# Väljund:
#
# /path/to/file1.txt
# /path/to/file1.txt


# Liigume kataloogi FILES ja siis prindime:
cd $FILES
for f in *
do
    echo $f
done
# Väljund:
#
# file1.txt
# file1.txt


# Ainult kataloogid:
# for d in */
# Ainult failid, mille nime lõpus on out.txt
# for f in *out.txt
