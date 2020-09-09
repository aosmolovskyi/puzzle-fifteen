package com.puzzle.model.error

import com.puzzle.model.{Cell, Coordinates}

sealed trait PuzzleException extends Exception

case object RandomNumberLoverThenZero extends PuzzleException {
  override def getMessage: String = "Random number range should be greater then 0"
}

case object CellValueLoverThenZero extends PuzzleException {
  override def getMessage: String = "Cell value should be greater then 0"
}

case object CoordinatesLoverThenZero extends PuzzleException {
  override def getMessage: String = "Coordinates should be greater then 0"
}

case object MissedEmptyCell extends PuzzleException {
  override def getMessage: String = "Empty cell should exist on the board"
}

case class MissedCells(index1: Int, index2: Int) extends PuzzleException {
  override def getMessage: String = s"Cells with indexes $index1 and/or $index2 is not exist on the board"
}

case class MissedCellView(cell: Cell) extends PuzzleException {
  override def getMessage: String = s"View is missing for cell $cell"
}

case class MissedCellWithCoordinates(coordinates: Coordinates) extends PuzzleException {
  override def getMessage: String = s"Cells with coordinates $coordinates is not exist on the board"
}

case class DuplicatedCoordinates(coordinates: Coordinates) extends PuzzleException {
  override def getMessage: String = s"Coordinates $coordinates are duplicated"
}

case class DefaultBoardValidation(isGridSizeCorrect: Boolean,
                                  isCellsCountCorrect: Boolean,
                                  isEmptyCellExists: Boolean) extends PuzzleException {
  override def getMessage: String =
    s"Default board validation failed. isGridSizeCorrect == $isGridSizeCorrect, " +
      s"isCellsCountCorrect == $isCellsCountCorrect, isEmptyCellExists == $isEmptyCellExists"
}