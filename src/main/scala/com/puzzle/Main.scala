package com.puzzle

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, Timer}
import com.puzzle.helper.{DefaultRandomizeAlgorithm, DefaultShuffleAlgorithm, ShuffleAlgorithm}
import com.puzzle.manager.{BoardStateManager, DefaultBoardStateManager, JFXGameManagerImpl}
import com.puzzle.model._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application._
import scalafx.scene._

import scala.concurrent.ExecutionContext

object Main extends JFXApp {
  private val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())
  private implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)
  private implicit val timer: Timer[IO] = IO.timer(ec)

  private val board = DefaultBoard.fifteenBoard
  private val bsmF: IO[BoardStateManager[IO]] = DefaultBoardStateManager.apply[IO](board)
  private val shuffleAlgorithm: ShuffleAlgorithm = DefaultShuffleAlgorithm.apply(DefaultRandomizeAlgorithm.apply)

  (for {
    bsm <- bsmF
    gameManager = JFXGameManagerImpl(board, bsm, shuffleAlgorithm)
    boardView <- gameManager.run
  } yield {
    stage = new PrimaryStage {
      title = "Fifteen Puzzle"
      scene = new Scene {
        root = boardView.root
        resizable = false
      }
    }
  }).unsafeRunSync()
}