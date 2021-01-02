-- Moodul M peab asuma failis M.hs
-- Teistes moodulites: import HaskellCheatsheet
module HaskellCheatsheet where 


{---------------------------}
{- STANDARDSED ANDMETÜÜBID -}
{---------------------------}


-- Tüübideklaratsioon ei ole kohustuslik
a :: Int
a = 5

b :: Char
b = 'A'

list1 :: [Double]
list1 = [0.1, 0.2, 0.3]

list2 :: [Double]
list2 = 0.4 : 0.5 : 0.6 : []

-- Tüübisünonüüm: [Double] asemel võib kirjutada DoubleList
type DoubleList = [Double]

-- Listide konkatenatsioon
list3 :: DoubleList
list3 = list1 ++ list2

-- Väärtuse lisamine listi algusesse
list4 :: DoubleList
list4 = 0.0 : list3

-- Indeksi järgi väärtuse võtmine
byIndex :: Double
byIndex = list3!!3

-- Haskelli standardteegis:
-- type String = [Char]
hello :: String
hello = "Hello World!"

-- Ennikud on fikseeritud suurusega järjendid, mida võib
-- paigutada üksteise sisse kuitahes keeruliselt.
tuple :: (String, Int, (Double, Bool))
tuple = ("Jack", 20, (0.5, True))

{-
Käsureal mooduli M kasutamine:

-- ghci käivitamine

> ghci

-- Liigub sinna kataloogi, kus fail M.hs asub

Prelude> :cd C:\...
Prelude> :load M
Compiling M ( M.hs, interpreted )
Ok, modules loaded: M.

-- Eeldusel, et moodulis M asub muutuja hello

*M> hello
"Hello World!"

-- Saab luua muutujaid, väärtustada avaldisi, kutsuda funktsioone jne.
-- Kui failis tehti vahepeal muudatusi

*M> :reload

-- ghci sulgemine

*M> :quit
-}


{----------------}
{- FUNKTSIOONID -}
{----------------}


-- Funktsioon, mis tagastab True, kui listis leidub mingi arv.
-- Esimene argument on Int tüüpi, teine argument on [Int] tüüpi
-- ja tagastustüüp on Bool
contains :: Int -> [Int] -> Bool
-- Juhul, kui list on tühi, tagastab False. Alakriips _ tähendab, et 
-- esimese argumendi väärtus ei ole oluline
contains _ [] = False
-- Ülejäänud juhtudel: (x:xs) tähistab listi, milles x on esimene
-- element ja xs on ülejäänud list
contains n (x:xs) | n == x = True               -- Juhul, kui esimene argument on sama mis x
                  | otherwise = contains n xs   -- Kui ei ole, siis otsi ülejäänud listist


-- Funktsioon, mis võtab argumentideks sõne ja arvu
-- ning tagastab sõne. Tulemus on sõne konkatenatsioon
-- mingi arv korda
repeatString :: String -> Int -> String
repeatString str copyNumber | copyNumber < 0 = error "n must be >= 0"
                            | copyNumber == 0 = ""
                            | otherwise = helper str str copyNumber
    where -- Siin on lokaalsed deklaratsioonid
        helper :: String -> String -> Int -> String
        helper _ result 1 = result
        helper unit result n = helper unit (unit ++ result) (n-1)


-- Võtmesõna let
letDemo :: Int -> Int -> Int
letDemo x y = 
    let r = 3 
        s = 6
        in  r*x + s*y

-- case of avaldis
listSum :: [Int] -> Int
listSum list = case list of
    [] -> 0
    (x:xs) -> x + listSum xs

-- if lause. NB! else peab kindlasti olema!
grade :: Int -> Char
grade x = 
    if x > 90 then 
        'A'
    else 
        if 80 < x && x < 90 then 
            'B'
        else 
            if 70 < x && x < 80 then 
                'C'
            else 
                if 60 < x && x < 70 then 
                    'D'
                else 
                    'F'

-- lambda funktsioon ehk anonüümne funktsioon:
lambdaDemo :: Int
lambdaDemo = (\x y -> x + y) 3 5 -- 8

{-
Funktsioonide kasutamine käsureal:

-- Kahe argumendiga funktsiooni kasutamine prefiks-kujul:

ghci> repeatString "Hello" 5
"HelloHelloHelloHelloHello"

-- Kahe argumendiga funktsiooni kasutamine infiks-kujul:

ghci> "Hello" `repeatString` 5
"HelloHelloHelloHelloHello"

-- Operaatoreid +, -, *, /, % jne saab kasutada nii:
prefix-kujul - 5 + 3
infiks-kujul - (+) 5 3

Funktsiooni saab ka osaliselt rakendada. Siin kutsutakse funkstiooni
repeatString, kuid jäetakse teine argument ära. Tulemuseks on uus funktsioon,
mis on samasugune, kuid esimene argument on fikseeritud "Hello".

ghci> repeatHello = repeatString "Hello"

Kontrollime muutuja repeatHello tüüpi:

ghci> :t repeatHello
repeatHello :: Int -> String

Kasutame uut funktsiooni:

ghci> repeatHello 3
"HelloHelloHello"
ghci> repeatHello 4
"HelloHelloHelloHello"
-}


{-----------------------------}
{- UUTE ANDMETÜÜPIDE LOOMINE -}
{-----------------------------}


-- Kahendpuu, mille tipud sisaldavad tüüpi a väärtuseid.
-- a  on tüübiparameeter. Tree on tüübikonstruktor.
-- Leaf ja Node on andmekonstruktorid.
data Tree a = Leaf | Node (Tree a) a (Tree a)

leaf :: Tree String
leaf = Leaf

node1 :: Tree String
node1 = Node leaf "lemon" leaf

node2 :: Tree String
node2 = Node leaf "lime" leaf

root :: Tree String
root = Node node1 "banana" node2

-- Funktsioon, mis võtab argumendiks puu ja tagastab listi kõikide 
-- tippude väärtustega. a on siin geneeriline tüüp.
getValueList :: Tree a -> [a]
getValueList Leaf = []
getValueList (Node left x right) = (getValueList left) ++ [x] ++ (getValueList right)

-- Haskelli standardteegis:
--
-- data Bool = False | True
-- data Maybe a = Nothing | Just a
-- data Either a b = Left a | Right b

-- Alternatiivne süntaks.
data Email = Email {
    sender :: String,
    receivers :: [String],
    subject :: String,
    content :: String
}

spamLetter :: Email
spamLetter = Email {
    sender = "mrgold@spammail.com",
    receivers = ["jack.lemon@hotmail.com", "devon.orange@gmail.com,"],
    subject = "CONGRATULATIONS!",
    content = "YOU HAVE WON 1,000,000$! CLICK HERE TO RECEIVE YOUR PRIZE."
}

spammer :: String
spammer = sender spamLetter -- "mrgold@spammail.com"


{----------------}
{- TÜÜBIKLASSID -}
{----------------}


-- Tüübiklassi CanBeEqual kuuluvaid tüüpe saab võrrelda
-- funktsioonidega isEqual ja isNotEqual. Mõlemal on olemas
-- vaikimisi implementatsioon (ei pea olema), kuid kuna need 
-- kasutavad üksteist, on vaja üks neist üle defineerida.
class CanBeEqual a where
    isEqual :: a -> a -> Bool
    isEqual x y = not (isNotEqual x y)

    isNotEqual :: a -> a -> Bool
    isNotEqual x y = not (isEqual x y)

-- Implementeerime selle tüübiklassi funktsiooni isEqual Email tüübi jaoks.
instance CanBeEqual Email where
    isEqual 
        Email {sender=s1, receivers=_, subject=sb1, content=c1} 
        Email {sender=s2, receivers=_, subject=sb2, content=c2} = 
            (s1 == s2) && (sb1 == sb2) && (c1 == c2)

-- Tüübiklassi Comparable kuuluvad tüübid peavad kuuluma ka klassi CanBeEqual.
class (CanBeEqual a) => Comparable a where
    isLarger :: a -> a -> Bool
    isLarger x y = not (isSmaller x y) && not (isEqual x y)

    isSmaller :: a -> a -> Bool
    isSmaller x y = not (isLarger x y) && not (isEqual x y)

-- Funktsiooni deklaratsioonis saab kirja panna, et teatud
-- argumendid peavad kuuluma mingisse kindlasse tüübiklassi,
-- antud juhul Num ja Show.
-- foo :: (Num a, Show a, Show b) => a -> a -> b -> String


{- Haskelli standardteegis (lihtsustatult):

class  Eq a  where
    (==), (/=) :: a -> a -> Bool
    x /= y     =  not (x == y)
    x == y     =  not (x /= y)

data Ordering = LT | EQ | GT

class  (Eq a) => Ord a  where
    compare              :: a -> a -> Ordering
    (<), (<=), (>=), (>) :: a -> a -> Bool
    max, min             :: a -> a -> a

class Show a where
    show :: a -> String
-}

{------------}
{- MONAADID -}
{------------}


{- 
https://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
Haskelli standardteegis:

(a -> b) tähistab funktsiooni, mille argument on tüüpi a ja tagastustüüp on b.
f on mingi andmetüüp kujul (data f a = ...)

class Functor f where
    fmap :: (a -> b) -> f a -> f b

class Functor f => Applicative f where
    pure :: a -> f a
    (<*>) :: f (a -> b) -> f a -> f b

class Applicative m => Monad m where
    (>>=) :: m a -> (a -> m b) -> m b
    (>>) :: m a -> m b -> m b
    return :: a -> m a


https://wiki.haskell.org/Monads_as_computation
Monaadi funktsioonide jaoks saab kasutada do-notatsiooni:
    
do { x } = x

do { x ; <stmts> }
  = x >> do { <stmts> }

do { v <- x ; <stmts> }
  = x >>= \v -> do { <stmts> }

do { let <decls> ; <stmts> }
  = let <decls> in do { <stmts> }
-}


-- Näide Maybe monaadi kasutamisest - üks ja sama arvutus 
-- >>= operaatoriga ja do notatsiooniga
maybeLambda :: Maybe Int
maybeLambda = 
    Just 5 >>= \x -> 
    Just (x + 3) >>= \y -> 
    Just (x * y) >>= \z -> 
    Just (x + y + z)

maybeDo :: Maybe Int
maybeDo = do {
    x <- Just 5;
    y <- Just (x + 3);
    z <- Just (x * y);
    return (x + y + z)
}


{---------------------}
{- SISEND JA VÄLJUND -}
{---------------------}


{-
Haskelli standardteegi vahendid sisend-väljundi jaoks:

type FilePath = String

Kasutaja sisendi küsimiseks
getLine :: IO String

Faili sisu kätte saamiseks
readFile :: FilePath -> IO String

Print käsk reavahetusega
putStrLn :: String -> IO ()

Print käsk ilma reavahetuseta
putStr :: String -> IO ()
-}


-- programm, mis küsib kasutajalt nime ja prindib selle ekraanile
nameLambda :: IO ()
nameLambda = putStr "What is your first name? " >>
             getLine >>= \ firstName ->
             putStr "And your last name? " >>
             getLine >>= \ lastName ->
             let fullName = firstName ++ " " ++ lastName
             in putStrLn ("Pleased to meet you, " ++ fullName ++ "!")

-- Sama programm, aga do notatsiooniga
nameDo :: IO ()
nameDo = do
    putStr "What is your first name? "
    firstName <- getLine
    putStr "And your last name? "
    lastName <- getLine
    let fullName = firstName ++ " " ++ lastName
    putStrLn ("Pleased to meet you, " ++ fullName ++ "!")