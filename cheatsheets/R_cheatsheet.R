# Elementaarsed andmetüübid:
#      5, 3.7 - numeric
#         56L - integer
#    "banana" - character
# TRUE, FALSE - logical
#      3 + 2i - complex
#
# Kaks võimalust, kuidas muutujaid väärtustada
i <- 56L
i = 23L
print(class(i)) # "integer"

# Vektor on sama tüüpi elementide kogum. Kui vektorisse kirjutaada
# erinevalt tüüpi elemente, siis R teisendab need kõik ühte tüüpi.
vec <- c("lemon", "lime", "banana")
print(class(x)) # "character"
print(length(x)) # 3
print(x[1]) # "lemon" (Vektori indekseerimine algab 1-st)

vec <- seq(1, 3, by=0.2)
# c(1.0, 1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 2.8, 3.0)
vec <- seq(1, 5, length.out = 4)
# c(1.000000, 2.333333, 3.666667, 5.000000)

# Listis võib olla mitu erinevat tüüpi elementi.
lst <- list(64, "hello", TRUE)
print(lst[1]) # 64
# Listi elementidele saab anda nimed
names(lst) <- c("red", "green", "blue")
print(lst$green) # "hello"

# Maatriks on 2-mõõtmeline andmestruktuur
mat = matrix(
  c(4, 5, 6, 7, 8, 9), 
  nrow = 2, 
  ncol = 3, 
  byrow=TRUE,
  dimnames = list(
    c("row1", "row2"),
    c("col1", "col2", "col3")
  )
)
print(mat)
#      col1 col2 col3
# row1    4    5    6
# row2    7    8    9
print(mat[1, 3]) # 6

# Vektoritega ja maatriksitega saab punktiviisilisi tehteid teha,
# kasutades aritmeetilisi operaatoreid z, -, *, / ning
# loogilisi operaatoreid &, |, !.
# Punktiviisiliste tehete jaoks peavad mõlemad maatriksid/vektorid
# Olema samade mõõtmetega. 
# Samu tehteid saab teha ka maatriksi/vektori ning arvu vahel,
# Sellisel juhul käsitletakse arvu kui maatriksit/vektorit, mis
# on selle arvuga täidetud ja samade mõõtmetega.

# Array on n-mõõtmeline andmestruktuur
arr = array(c("green", "yellow"), dim = c(3, 3, 2))
print(arr)
# , , 1
#
#      [,1]     [,2]     [,3]    
# [1,] "green"  "yellow" "green" 
# [2,] "yellow" "green"  "yellow"
# [3,] "green"  "yellow" "green" 
#
# , , 2
#
#      [,1]     [,2]     [,3]    
# [1,] "yellow" "green"  "yellow"
# [2,] "green"  "yellow" "green" 
# [3,] "yellow" "green"  "yellow" 

# data.frame() tekitab andmetega tabeli. Tegelikult on see
# list, kus kõik elemendid on sama pikkusega vektorid,
# mis võivad olla eri tüüpi ja igal elemendil on nimi.
cars = data.frame(
  car = c("Ford Scorpio", "Audi A6", "Mazda 6"),
  year = c(1997L, 2005L, 2010L),
  rwd = c(TRUE, FALSE, TRUE),
  city = c("London", "Tallinn", "Moscow")
)
print(cars)
#   car           year  rwd    city
# 1 Ford Scorpio  1997  TRUE   London
# 2 Audi A6       2005  FALSE  Tallinn
# 3 Mazda 6       2010  TRUE   Moscow

# typeof(cars) == list
# Tulpade arv
length(cars) # 4

# Tulba saamine tabelist ühetulbalise tabeli kujul
yearColumn <- cars[2] 
# lenght(yearColumn) == 1, typeof(yearColumn) == list

# Tulba saamine tabelist vektori kujul
yearVec <- cars$year 
# length(yearVec) == 3, typeof(yearVec) == integer

# Eraldab tabelist 2. ja 3. rea ning 1. ja 2. veeru
fordAndAudi = cars[c(2,3), c(1,2)]

# Tulba lisamine tabelisse
cars$color <- c("black", "gray", "red")

# Tabeli filtreerimine
newCars <- subset(cars, year > 2000L)
# Tabeli lugemine csv failist
cars2 <- read.csv("fail.csv")
# Tabeli kirjutamine csv faili.
write.csv(cars, "cars.csv")
# Kahe andmetabeli ühendamine uueks tabeliks.
# Mõlemal tabelil peavad olema sama tüüpi veerud.
merged <- rbind(cars, cars2)

if (is.integer(i)) {
  # Kui i on integer tüüpi, siis ...
} else {
  
}
# while tsükkel
while (i < 10) {
  # ... 
}
# do tsükkel
repeat { 
  # ... 
  if(i < 10) {
    break
  }
}
# for tsükkel
for (str in c("lemon", "lime", "banana")) {
  if (str == "lime") {
    next # sama mis teistes keeltes continue
  }
}
# Funktsioon
custom <- function(arg1, arg2, arg3 = TRUE) {
  # Viimasel real olev avaldis on tagastusväärtus
  5 + 5
}
custom(34L, "", c(5, 6, 7))
# Argumente saab ka nimepidi funktsioonile anda suvalises järjekorras.
custom(arg3 = c(5, 6, 7), arg2 = "", arg1 = 34L)

# Liigub kataloogi.
setwd("/Users/kermosaarse/Documents")
# NB! Windowsis tuleb failiteed kirjutada nii:
# c:\\foo\\bar või c:/foo/bar
# Kuid linux'is ja macOS'is:
# /foo/bar

getwd() # Tagastab praeguse kataloogi

# Määrab selle, kuhu suunatakse programmi tekstiline väljund.
# Vaikimisi on selleks terminal.
#   append - kas kijutada faili lõppu või kirjutada fail üle?
#   split - kas kirjutada väljund ka terminali?
sink("fail.txt", append=TRUE, split=TRUE)
# Suunab tekstiväljundi ainult terminali.
sink()
# Prindib teksti sinna, kuhu sink() käsuga väljund suunati.
print("Hello World!")

# Määrab graafilise väljundi asukoha. Vaikimisi on terminal.
pdf("fail.pdf")
jpg("fail.jpg")
bmp("fail.bmp")

# Graafiku joonistamine sinna, kuhu pdf(), jpg() või bmp()
# käsuga väljund suunati.
plot(
  c(2, 5, 3, 7, 9, 6, 7), # andmepunktide y koordinaatide väärtused
  type = "l", # "p" - punktid, "l" - jooned, "o" - mõlemad
  col = "red",
  xlab = "x-telje pealkiri",
  ylab = "y-telje pealkiri",
  main = "graafiku pealkiri"
)
# Teise graafiku joonestamine samale joonisele.
lines(
  c(1, 2, 3, 4, 5, 6, 7),
  type = "p",
  col = "blue"
)
# Salvestab graafilise väljundi faili ning edasine väljund suunatakse terminali.
dev.off()

# Hajuvusdiagramm (scatterplot)
plot(
  x = c(7, 6, 5, 4, 3, 2, 1), # x koordinaatide väärtused
  y = c(1, 2, 3, 4, 5, 6, 7), # y koordinaatide väärtused
  col = "green",
  xlim = c(2, 5), # Minimaalne ja maksimaalne x-koordinaat
  ylim = c(2, 5),
  xlab = "x-telje pealkiri",
  ylab = "y-telje pealkiri",
  main = "graafiku pealkiri"
)
