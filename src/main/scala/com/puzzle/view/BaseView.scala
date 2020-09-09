package com.puzzle.view

import scalafx.beans.property.ObjectProperty
import scalafx.beans.value.ObservableValue
import scalafx.event.subscriptions.Subscription
import scalafx.scene.Parent

import scala.collection.mutable


abstract class BaseView[Data, Root <: Parent](value: Data) {


  type ChangeHandler = (Data, Data) => Unit
  val root: Root
  protected val subscriptions: mutable.Set[Subscription] = mutable.Set[Subscription]()
  private val _data: ObjectProperty[Data] = ObjectProperty(value)

  def mapData[T](map: Data => T): ObjectProperty[T] = {
    val property = ObjectProperty(map(element))
    onDataChange({ (_, next) => property() = map(next) })
    property
  }

  def element: Data = _data()

  def onDataChange(handler: ChangeHandler): Unit = listen(_data, handler)

  private def listen(value: ObservableValue[Data, Data], action: ChangeHandler): Unit =
    subscriptions add value.onChange((_, from, to) => action(from, to))

  def render(next: Data): Unit = {
    _data() = next
  }

}
