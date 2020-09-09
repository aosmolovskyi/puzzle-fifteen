package com.puzzle.model

import com.puzzle.model.error.{CellValueLoverThenZero, CoordinatesLoverThenZero}
import org.specs2.matcher.Matchers
import org.specs2.mutable.Spec


class CellSpec extends Spec with Matchers {

  "Cell" should {
    "correctly creates" in {
      DefaultCell(Coordinates(1, 1), 1)
      success
    }

    "return exception on negative value" in {
      DefaultCell(Coordinates(1, 1), -1) must throwA[CellValueLoverThenZero.type]
    }

    "return exception on negative line coordinates" in {
      DefaultCell(Coordinates(-1, 1), 1) must throwA[CoordinatesLoverThenZero.type]
    }

    "return exception on negative column coordinates" in {
      DefaultCell(Coordinates(1, -1), 1) must throwA[CoordinatesLoverThenZero.type]
    }

    "correctly switch" in {
      val cell1 = DefaultCell(Coordinates(1, 1), 1)
      val cell2 = DefaultCell(Coordinates(1, 2), 2)

      val correctCell1 = DefaultCell(Coordinates(1, 2), 1)
      val correctCell2 = DefaultCell(Coordinates(1, 1), 2)

      cell1.switch(cell2) mustEqual Set(correctCell1, correctCell2)
    }

    "correctly switch with empty cell" in {
      val cell1 = DefaultCell(Coordinates(1, 1), 1)
      val cell2 = EmptyCell(Coordinates(1, 2))

      val correctCell1 = DefaultCell(Coordinates(1, 2), 1)
      val correctCell2 = EmptyCell(Coordinates(1, 1))

      cell1.switch(cell2) mustEqual Set(correctCell1, correctCell2)
    }


    "correctly found close cell" in {
      val cell1 = DefaultCell(Coordinates(1, 1), 1)
      val cell2 = DefaultCell(Coordinates(1, 2), 2)
      val cell3 = DefaultCell(Coordinates(1, 3), 2)
      cell1.isClose(cell2) mustEqual true
      cell2.isClose(cell3) mustEqual true
      cell1.isClose(cell3) mustEqual false


    }
  }

}
