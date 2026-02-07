package controllers

import play.api.mvc._

abstract class BaseFrontendController(implicit mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc)
  with ScalaTagWritable
