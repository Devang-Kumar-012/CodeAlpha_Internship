#include <iostream>
using namespace std;

const int SIZE = 9;

// Check if num can be placed at (row, col)
bool isSafe(int grid[SIZE][SIZE], int row, int col, int num) {
    // Check row and column
    for (int x = 0; x < SIZE; x++) {
        if (grid[row][x] == num || grid[x][col] == num)
            return false;
    }

    // Check 3x3 box
    int startRow = row - row % 3;
    int startCol = col - col % 3;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (grid[startRow + i][startCol + j] == num)
                return false;
        }
    }

    return true;
}

// Solve using backtracking
bool solveSudoku(int grid[SIZE][SIZE]) {
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            if (grid[row][col] == 0) {
                for (int num = 1; num <= 9; num++) {
                    if (isSafe(grid, row, col, num)) {
                        grid[row][col] = num;
                        if (solveSudoku(grid))
                            return true;
                        grid[row][col] = 0; // Backtrack
                    }
                }
                return false;
            }
        }
    }
    return true;
}

// Display Sudoku grid with box layout
void printGrid(int grid[SIZE][SIZE]) {
    cout << "+-------+-------+-------+\n";
    for (int i = 0; i < SIZE; i++) {
        cout << "| ";
        for (int j = 0; j < SIZE; j++) {
            if (grid[i][j] == 0)
                cout << ". ";
            else
                cout << grid[i][j] << " ";
            if ((j + 1) % 3 == 0)
                cout << "| ";
        }
        cout << endl;
        if ((i + 1) % 3 == 0)
            cout << "+-------+-------+-------+\n";
    }
}

// Safely read one valid integer (0–9) from user
int getSafeInput() {
    int num;
    while (true) {
        cin >> num;
        if (cin.fail() || num < 0 || num > 9) {
            cin.clear();                 // Clear input error
            cin.ignore(1000, '\n');      // Remove bad input
            cout << "Invalid! Enter a number (0 to 9): ";
        } else {
            return num;
        }
    }
}

// Take Sudoku input from user row by row
void inputGrid(int grid[SIZE][SIZE]) {
    cout << "Enter your Sudoku puzzle row by row.\n";
    cout << "Use 0 for empty cells.\n\n";
    for (int i = 0; i < SIZE; i++) {
        cout << "Row " << i + 1 << " (9 numbers, 0–9): ";
        for (int j = 0; j < SIZE; j++) {
            grid[i][j] = getSafeInput();
        }
    }
}

int main() {
    int grid[SIZE][SIZE];

    cout << "===== SUDOKU SOLVER =====\n\n";
    inputGrid(grid);

    cout << "\nYour Puzzle:\n";
    printGrid(grid);

    if (solveSudoku(grid)) {
        cout << "\nSolved Sudoku:\n";
        printGrid(grid);
    } else {
        cout << "\nNo solution exists for the given puzzle.\n";
    }

    return 0;
}