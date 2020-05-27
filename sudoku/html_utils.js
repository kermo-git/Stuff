function buildTable() {
    var table = document.createElement("table");
    table.className = "sudoku";
    
    for (var i = 0; i < 9; i ++) {
        var row = document.createElement("tr");
        for (var j = 0;  j < 9; j ++) {
            var cell = document.createElement("td");
            cell.className = "sudoku";
            cell.id = i + "_" + j;
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
    document.body.appendChild(table);
}


var errorMessage;


function setEstonian() {
    document.getElementById("description").textContent =
    `Sudoku on loogikamõistatus, milles tuleb 9×9 ruudustik täita numbritega 1–9 nii, et üheski
    reas, veerus ega paksu joonega piiratud 3×3 alas ükski number ei kordu. Uue sudoku
    genereerimiseks vali sobiv raskusaste (kerge, keskmine või raske). Numbri kirjutamiseks vajuta
    tühjale ruudule ning vali rippmenüüst number.`;

    errorMessage = " on selles reas, veerus või 3×3 kastis juba olemas!";

    document.getElementById("easy").textContent = "Kerge";
    document.getElementById("medium").textContent = "Keskmine";
    document.getElementById("hard").textContent = "Raske";
    document.getElementById("solution").textContent = "Lahendus";
}


function setEnglish() {
    document.getElementById("description").textContent =
    `Sudoku is a logic puzzle where the objective is to fill a 9×9 grid with digits 1-9 so
    that each column, row, and 3×3 subgrid surrounded by bold line contain all of the digits
    only once. To generate a new sudoku, choose a difficulty level (easy, medium or hard).
    To write a number, click on an empty cell and choose the number from the dropdown menu.`;

    errorMessage = " already exists in this row, column or the 3×3 subgrid!";

    document.getElementById("easy").textContent = "Easy";
    document.getElementById("medium").textContent = "Medium";
    document.getElementById("hard").textContent = "Hard";
    document.getElementById("solution").textContent = "Solution";
}


var onchangeFunc = function () {
    var value = this.options[this.selectedIndex].value;
    if (value == "") {
        return;
    }
    if (isValid(value, this.row, this.col)) {
        grid[this.row][this.col] = value;

        if (isFull()) {
            alert("Lahendatud!")
            clearTable();
        }
        return;
    } else {
        alert(value + errorMessage);
    }
    this.selectedIndex = 0;
}


function getCellInput(i, j) {
    var select = document.createElement("select");
    select.className = "sudoku";

    select.row = i;
    select.col = j;

    var option = document.createElement("option");
    option.value = ""; option.text = "";
    select.appendChild(option);

    for(var i = 1; i <= 9; i++) {
        option = document.createElement("option");
        option.value = i; option.text = i;
        select.appendChild(option);
    }
    select.onchange = onchangeFunc;
    return select;
}


function fillTable() {
    for (var i = 0; i < 9; i ++) {
        for (var j = 0;  j < 9; j ++) {
            var cell = document.getElementById(i + "_" + j);
            cell.innerHTML = "";

            if (grid[i][j] == 0) {
                cell.appendChild(getCellInput(i, j));
            } else {
                cell.innerHTML = grid[i][j];
            }
        }
    }
}


function clearTable() {
    for (var i = 0; i < 9; i ++) {
        for (var j = 0;  j < 9; j ++) {
            var cell = document.getElementById(i + "_" + j);
            cell.innerHTML = "";
        }
    }
}
