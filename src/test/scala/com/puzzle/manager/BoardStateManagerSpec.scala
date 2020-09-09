package com.puzzle.manager

import cats.effect.IO
import com.puzzle.model._
import com.puzzle.util.{IOMatchers, IOSpec}
import org.specs2.matcher.Matchers
import org.specs2.mutable.Spec
import org.specs2.specification.core.Env


class BoardStateManagerSpec(val env: Env) extends Spec with IOSpec with IOMatchers with Matchers {

  "ShuffleAlgorithm" should {
    "correctly get state" in {
      val board = DefaultBoard.createBoard(2)
      val boardStateManagerF = DefaultBoardStateManager.apply[IO](board)
      boardStateManagerF.flatMap(_.getState) must equalTo(board).io
    }

    "correctly set state" in {
      val board = DefaultBoard.createBoard(2)
      val newBoard = DefaultBoard.createBoard(3)

      (for {
        boardStateManagerF <- DefaultBoardStateManager.apply[IO](board)
        fistState <- boardStateManagerF.getState
        stateChange <- boardStateManagerF.setState(newBoard)
        secondState <- boardStateManagerF.getState
      } yield {
        (fistState, secondState, stateChange)
      }) must equalTo((board, newBoard, ())).io
    }
  }

}
