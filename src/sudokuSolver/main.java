/*
 * Sudoku solving program
 * 
 * takes text file of 9 by 9 sudoku and solves it
 * must be in format similar to sudokuTest.txt, where empty spaces are denoted by "."
 */

package sudokuSolver;

import java.io.*;
import java.util.Scanner;

public class main {

	private final static int sudokuDimension = 9;
	private final static String emptyMarker = ".";

	public static void main(String[] args) {
		File inputFile;
		try {
			inputFile = new File(args[0]);
		} catch (IndexOutOfBoundsException e) {
			inputFile = new File("sudokuTest.txt");
		}
		try {
			Scanner scn = new Scanner(new FileReader(inputFile));

			String str;

			String[][] inputSudoku = new String[sudokuDimension][sudokuDimension];
			readInputSudoku(inputSudoku, scn);

			System.out.println("Here is the original sudoku:");
			outputSudoku(inputSudoku);

			// solve sudoku
			if (isSolved(inputSudoku)) {
				// double check sudoku
				if (checkSudoku(inputSudoku)) {
					System.out.println("Here is the solved sudoku: ");
					outputSudoku(inputSudoku);
				}
			} else {
				System.out.println("Sudoku can't be solved");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// check if the number is in the given row
	private static boolean isInRow(String[][] sudoku, int rowNumber, String number) {
		for (int i = 0; i < sudokuDimension; i++)
			if (sudoku[rowNumber][i].equals(number))
				return true;
		return false;
	}

	// check if the number is in the given column
	private static boolean isInColumn(String[][] sudoku, int columnNumber, String number) {
		for (int i = 0; i < sudokuDimension; i++)
			if (sudoku[i][columnNumber].equals(number))
				return true;
		return false;
	}

	// check if the number is in the corresponding block of the row and column
	private static boolean isInBlock(String[][] sudoku, int rowNumber, int columnNumber, String number) {
		int r = rowNumber - (rowNumber % 3);
		int c = columnNumber - (columnNumber % 3);

		for (int i = r; i < r + 3; i++)
			for (int j = c; j < c + 3; j++)
				if (sudoku[i][j].equals(number))
					return true;
		return false;
	}

	// check if the number is valid to go in give row and column with regards to the
	// rules of sudoku
	private static boolean isValid(String[][] sudoku, int row, int column, String number) {
		return !isInRow(sudoku, row, number) && !isInColumn(sudoku, column, number)
				&& !isInBlock(sudoku, row, column, number);
	}

	// solve the sudoku using recursive backtracking
	private static boolean isSolved(String[][] sudoku) {
		// iterate through 2d array
		for (int i = 0; i < sudokuDimension; i++) {
			for (int j = 0; j < sudokuDimension; j++) {
				// if element at location (i , j) is empty
				if (sudoku[i][j].equals(emptyMarker)) {
					// try valid numbers
					for (int k = 1; k <= sudokuDimension; k++) {
						// if the number is valid, try it
						if (isValid(sudoku, i, j, Integer.toString(k))) {
							sudoku[i][j] = Integer.toString(k);

							// if the sudoku is solved return true
							if (isSolved(sudoku)) {
								return true;
							} else { // else, put in the empty element marker
								sudoku[i][j] = emptyMarker;
							}
						}
					} // end trying of valid numbers
					return false;
				}
			}
		} // end iteration through 2d array
		return true;
	}

	// checks entire sudoku
	private static boolean checkSudoku(String[][] sudoku) {
		// iterate through 2d array
		for (int i = 0; i < sudokuDimension; i++) {
			for (int j = 0; j < sudokuDimension; j++) {
				// iterates through the elements in front of each element in the row
				// and compares them
				for (int k = j + 1; k < sudokuDimension; k++) {
					if (sudoku[i][j].equals(sudoku[i][k]))
						return false;
					// does the same but for the columns
					if (sudoku[j][i].equals(sudoku[k][i]))
						return false;
				}
			}
		} // end row and column comparison

		// compares each 3 by 3
		for (int n = 0; n < sudokuDimension; n += 3) {
			for (int i = 0; i < sudokuDimension; i += 3) {// 0, 3, 6
				String[] tempArray = new String[sudokuDimension];
				int tempArrayIndex = 0;
				for (int j = 0; j < 3; j++) {// 0, 1, 2
					for (int k = 0; k < 3; k++) {
						tempArray[tempArrayIndex] = sudoku[j + n][k + i];
						tempArrayIndex++;
						// System.out.println("Checking Location: " + "(" + (j+n) + ", " + (k+i) + ")");
					}
				}
				// compares elements in tempArray
				for (int l = 0; l < tempArray.length; l++) {
					for (int m = l + 1; m < tempArray.length; m++) {
						if (tempArray[l].equals(tempArray[m]))
							return false;
					}
				}
			}
		} // end 3 by 3 comparison
		return true;
	}
	
	// turns text file into 2d array
	private static void readInputSudoku(String[][] sudoku, Scanner scn) {
		while (scn.hasNextLine()) {
			for (int i = 0; i < sudokuDimension; i++) {
				for (int j = 0; j < sudokuDimension; j++)
					sudoku[i][j] = scn.next();
				scn.nextLine();
			}
		}
	}
	
	// outputs the sudoku
	private static void outputSudoku(String[][] sudoku) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				System.out.print(sudoku[i][j] + " ");
			System.out.println();
		}
	}

}
