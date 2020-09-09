package com.puzzle.model

import com.puzzle.model.error.{CoordinatesLoverThenZero, DuplicatedCoordinates}

import scala.math.abs

case class Coordinates(line: Int, column: Int) {
  def isClose(target: Coordinates): Boolean = target match {
    case coordinates: Coordinates if coordinates == this => throw DuplicatedCoordinates(coordinates)
    case Coordinates(targetLine, targetColumn) =>
      column == targetColumn && abs(line - targetLine) == 1 ||
        line == targetLine && abs(column - targetColumn) == 1
  }
}

object Coordinates {
  def apply(line: Int, column: Int): Coordinates =
    if (line < 0 || column < 0)
      throw CoordinatesLoverThenZero
    else
      new Coordinates(line, column)

  implicit val ordering: Ordering[Coordinates] = (x: Coordinates, y: Coordinates) => {
    x match {
      case _ if x == y => 0
      case _ if x.line > y.line => 1
      case _ if x.line == y.line && x.column > y.column => 1
      case _ => -1
    }
  }
}
