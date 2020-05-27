var grid;

function createEmptyGrid() {
    grid = [[0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0],
            [0,0,0,0,0,0,0,0,0]];
}


function isValid(value, row, column) {
    for (var index = 0; index < 9; index++) {
        if (grid[row][index] == value)
            return false;
        if (grid[index][column] == value)
            return false;
    }
    var upperRow = 3 * Math.floor(row/3);
    var leftColumn = 3 * Math.floor(column/3);

    for (var r = upperRow; r < upperRow + 3; r++) {
        for (var c = leftColumn; c < leftColumn + 3; c++) {
            if (grid[r][c] == value)
                return false;
        }
    }
    return true;
}


function isFull() {
    for(row = 0; row < 9; row++) {
        for (column = 0; column < 9; column++) {
            if (grid[row][column] == 0)
                return false;
        }
    }
    return true;
}


/**
 * A recursive algoritm for solving a sudoku taken from here:
 * 
 * https://www.101computing.net/backtracking-algorithm-sudoku-solver/
 */
function solve() {
    var row, column, value;

    for(row = 0; row < 9; row++) {
        for (column = 0; column < 9; column++) {
    
            if (grid[row][column] == 0) {
                for (value = 1; value <= 9; value++) {
                    if (isValid(value, row, column)) {
                        grid[row][column] = value;

                        if (isFull() || solve())
                            return true;

                        grid[row][column] = 0;
                    }
                }
                return false;
            }
        }
    }
}


function hasUniqueSolution() {
    var counter = 0;
    
    function countSolutions() {
        if (isFull()) {
            counter++;
            return;
        }
        var row, column, value;
    
        for(row = 0; row < 9; row++) {
            for (column = 0; column < 9; column++) {
        
                if (grid[row][column] == 0) {
                    for (value = 1; value <= 9; value++) {
                        if (isValid(value, row, column)) {
                            grid[row][column] = value;
                            countSolutions();
                            grid[row][column] = 0;
                            
                            if (counter > 1)
                                return;
                        }
                    }
                    return;
                }
            }
        }
    }
    countSolutions();
    return counter == 1;
}


function shuffle(array) {
    var randomIndex, tempValue;

    for (var index = array.length - 1; index > 0; index--) {
        randomIndex = Math.floor(Math.random()*index)
        tempValue = array[index]
        array[index] = array[randomIndex]
        array[randomIndex] = tempValue
    }
}


function fill3X3Box(upperRow, leftColumn) {
    var numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9];
    shuffle(numbers);
    var index = 0;

    for (var row = upperRow; row < upperRow + 3; row++) {
        for (var column = leftColumn; column < leftColumn + 3; column++) {
            grid[row][column] = numbers[index];
            index++;
        }
    }
}


/**
 * First generates a random solution and then removes some
 * numbers in random order so that the sudoku has only one
 * possible solution.
 */
function generate(numberOfEmptyCells) {
    createEmptyGrid();
    /*
     * Fills 3X3 boxes on the diagonal first because they are
     * independent from each other.
     */
    fill3X3Box(0, 0);
    fill3X3Box(3, 3);
    fill3X3Box(6, 6);

    solve();

    var row, column, cells = [];

    for(row = 0; row < 9; row++) {
        for (column = 0; column < 9; column++)
            cells.push([row, column]);
    }

    shuffle(cells);
    var index, cell, value, emptyCellCounter = 0;
    
    for (index = 0; index < cells.length; index++) {
        cell = cells[index];

        value = grid[cell[0]][cell[1]];
        grid[cell[0]][cell[1]] = 0;

        if (hasUniqueSolution()) {
            emptyCellCounter++;
            if (emptyCellCounter >= numberOfEmptyCells)
                break;
        }
        else {
            grid[cell[0]][cell[1]] = value;
        }
    }
}
