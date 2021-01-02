module MazeGenerator where
import Data.Bits


{--------------}
{- DATA TYPES -}
{--------------}


data Direction = North | East | South | West

data Cell = Cell {
    posX :: Int,
    posY :: Int,
    visited :: Bool,
    arrow :: Direction,
    northOpen :: Bool,
    westOpen :: Bool
}

data State = State {
    width :: Int,
    height :: Int,
    grid :: [[Cell]],
    rngState :: Integer
}

{--------------------}
{- RANDOM SELECTION -}
{--------------------}
    
a, m :: Integer
a = 25214903917
m = (shiftL 1 48) - 1

initRandom :: Integer -> Integer
initRandom seed = seed `xor` a .&. m

nextRandom :: Integer -> Integer
nextRandom prevState = let 
            newState = (prevState*a + 11) .&. m
        in
            shiftR newState 16

pickRandomItem :: State -> [a] -> (State, a)
pickRandomItem state items = (newState, choice)
    where
    randInt = fromIntegral (rngState state)

    newState = state { 
        rngState = nextRandom (rngState state) 
    }
    choice = items!!(mod randInt (length items))

{-----------------------}
{- MAZE INITIALIZATION -}
{-----------------------}

newCell :: Int -> Int -> Cell
newCell x y = Cell {
    posX = x,
    posY = y,
    visited = False,
    arrow = North,
    northOpen = False,
    westOpen = False
}

newMaze :: Int -> Int -> Integer -> State
newMaze w h seed = State {
    width = w,
    height = h,
    grid = cellGrid 0,
    rngState = initRandom seed
}
    where        
    cellRow :: Int -> Int -> [Cell]
    cellRow x y | x == w = []
                | otherwise = newCell x y : cellRow (x+1) y
        
    cellGrid :: Int -> [[Cell]]
    cellGrid y | y == h = []
               | otherwise = cellRow 0 y : cellGrid (y+1)

{-------------------------}
{- MAZE GENERATION UTILS -}
{-------------------------}


getCell :: State -> Int -> Int -> Cell
getCell state x y = (grid state)!!y!!x


getAllCells :: State -> [Cell]
getAllCells state = flatten (grid state)
    where
    flatten :: [[Cell]] -> [Cell]
    flatten [] = []
    flatten (x:xs) = x ++ flatten xs


getUnvisitedCells :: State -> [Cell]
getUnvisitedCells state = filterUnvisited (grid state)
    where
    notVisited :: Cell -> Bool
    notVisited cell = not (visited cell)

    filterUnvisited :: [[Cell]] -> [Cell]
    filterUnvisited [] = []
    filterUnvisited (x:xs) = (filter notVisited x) ++ filterUnvisited xs


updateCell :: State -> Cell -> State
updateCell state cell = state { grid = updateGrid (grid state) }
    where
    updateGrid :: [[Cell]] -> [[Cell]]
    updateGrid [] = []
    updateGrid (r:rs) =
        if (posY (head r)) == posY cell then
            updateRow r : rs
        else
            r : updateGrid rs

    updateRow :: [Cell] -> [Cell]
    updateRow [] = []
    updateRow (c:cs) = 
        if posX c == posX cell then
            cell:cs
        else
            c : updateRow cs


getNeighbor :: State -> Cell -> Direction -> Cell
getNeighbor state cell dir = 
    case dir of
        North -> getCell state (posX cell) (posY cell - 1)
        East -> getCell state (posX cell + 1) (posY cell)
        South -> getCell state (posX cell) (posY cell + 1)
        West -> getCell state (posX cell - 1) (posY cell)


breakWall :: State -> Cell -> Direction -> State
breakWall state cell dir = 
    let
        neighbor = getNeighbor state cell dir
    in 
        case dir of
            North -> updateCell state cell{ northOpen = True }
            East -> updateCell state neighbor{ westOpen = True }
            South -> updateCell state neighbor{ northOpen = True }
            West -> updateCell state cell{ westOpen = True }


findNeighborDirections :: State -> Cell -> [Direction]
findNeighborDirections state cell = filter hasNeighbor [North, East, South, West]
    where
    hasNeighbor :: Direction -> Bool
    hasNeighbor dir =
        case dir of
            North -> posY cell > 0
            East -> posX cell < (width state) - 1
            South -> posY cell < (height state) - 1
            West -> posX cell > 0


getAllNeighbors :: State -> Cell -> [Cell]
getAllNeighbors state cell = 
    map (getNeighbor state cell) 
    (findNeighborDirections state cell)
    


visit :: State -> Cell -> State
visit state cell = updateCell state cell{ visited = True }

{----------------------}
{- WILSON'S ALGORITHM -}
{----------------------}


moveToRandomNeighbor :: State -> Cell -> (State, Cell)
moveToRandomNeighbor state cell = 
    let
        neighborDirections = findNeighborDirections state cell
        (state1, direction) = pickRandomItem state neighborDirections
        state2 = updateCell state1 cell{ arrow = direction }
    in
        (state2, getNeighbor state2 cell direction)


doRandomWalk :: State -> Cell -> State
doRandomWalk state0 startCell = 
    let
        (state1, nextCell) = moveToRandomNeighbor state0 startCell
    in
        if visited nextCell then
            state1
        else
            doRandomWalk state1 nextCell


buildPath :: State -> Cell -> State
buildPath state0 startCell = 
    let
        state1 = updateCell state0 startCell{ visited = True }
        state2 = breakWall state1 startCell (arrow startCell)
        nextCell = getNeighbor state2 startCell (arrow startCell)
    in
        if visited nextCell then
            state2
        else
            buildPath state2 nextCell


doWilsonLoop :: State -> Cell -> State
doWilsonLoop state0 startCell = 
    let
        state1 = doRandomWalk state0 startCell
        updatedStartCell = getCell state1 (posX startCell) (posY startCell)
        state2 = buildPath state1 updatedStartCell
    in
        case getUnvisitedCells state2 of
            [] -> state2
            unvisitedCells -> 
                let
                    (state3, newStartCell) = pickRandomItem state2 unvisitedCells
                in
                    doWilsonLoop state3 newStartCell


generateWilsonMaze :: State -> State
generateWilsonMaze state0 = 
    let
        (state1, firstVisited) = pickRandomItem state0 (getAllCells state0)
        state2 = updateCell state1 firstVisited{ visited = True }
        (state3, startCell) = pickRandomItem state2 (getUnvisitedCells state2)
    in
        doWilsonLoop state3 startCell

{--------------------------}
{- BACKTRACKING ALGORITHM -}
{--------------------------}

{-----------------------------}
{- CONVERTING MAZE TO STRING -}
{-----------------------------}

xSpace, xWall, ySpace, yWall, lineBreak :: String
xSpace = "     "
xWall = " X X "
ySpace = " "
yWall = "X"
lineBreak = "\n"

cellTop :: Cell -> String
cellTop cell = 
    if northOpen cell then 
        yWall ++ xSpace
    else
        yWall ++ xWall

cellMiddle :: Cell -> String
cellMiddle cell = 
    if westOpen cell then 
        ySpace ++ xSpace
    else
        yWall ++ xSpace

rowTop :: [Cell] -> String
rowTop [] = yWall ++ lineBreak
rowTop (cell:cs) = (cellTop cell) ++ (rowTop cs)

rowMiddle :: [Cell] -> String
rowMiddle [] = yWall ++ lineBreak
rowMiddle (cell:cs) = (cellMiddle cell) ++ (rowMiddle cs)

gridStr :: [[Cell]] -> String
gridStr [] = ""
gridStr (row:rs) = (rowTop row) ++ (rowMiddle row) ++ (rowMiddle row) ++ (gridStr rs)

instance Show State where
    show State {width=w, grid=g} = 
        (gridStr g) ++ (bottomWall w)
        where
            bottomWall :: Int -> String
            bottomWall 0 = yWall ++ lineBreak
            bottomWall n = yWall ++ xWall ++ (bottomWall (n-1))