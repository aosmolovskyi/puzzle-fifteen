package com.puzzle.model

import com.puzzle.model.Cell.Value
import com.puzzle.model.error.CellValueLoverThenZero

/**
 * Interface for Cell
 */
sealed trait Cell {

  /**
   * Coordinates of that Cell
   */
  val coordinates: Coordinates
  /**
   * Value of that Cell, if empty - should have value 0
   */
  val value: Value

  /**
   * Change current coordinates of cell to specific
   *
   * @param coordinates new coordinates
   * @return new cell with updated coordinates
   */
  def withCoordinates(coordinates: Coordinates): Cell

  /**
   * Checks whatever cell is close to another cell
   *
   * @param cell target cell
   * @return true if cell is close and false otherwise
   */
  def isClose(cell: Cell): Boolean = cell.coordinates.isClose(coordinates)

  /**
   * Switch cells
   *
   * @param cell target cell
   * @return Set of 2 new cells with updated coordinates
   */
  def switch(cell: Cell): Set[Cell] =
    Set(cell.withCoordinates(this.coordinates), withCoordinates(cell.coordinates))

}

object Cell {
  type Value = Int

  def unapply(cell: Cell): Option[(Coordinates, Value)] = Some(cell.coordinates, cell.value)
}

case class DefaultCell(coordinates: Coordinates, value: Value) extends Cell {

  def withCoordinates(coordinates: Coordinates): Cell = copy(coordinates)

}

object DefaultCell {
  def apply(coordinates: Coordinates, value: Value): DefaultCell =
    if (value <= 0)
      throw CellValueLoverThenZero
    else
      new DefaultCell(coordinates, value)

}

case class EmptyCell(coordinates: Coordinates) extends Cell {

  val value: Value = 0

  def withCoordinates(coordinates: Coordinates): Cell = copy(coordinates)

}

