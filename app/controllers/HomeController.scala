package controllers

import play.api.mvc._
import views.html.Index

import javax.inject._

@Singleton
class HomeController @Inject()(index: Index, val controllerComponents: MessagesControllerComponents) extends MessagesBaseController {

  def home(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(index())
  }

}
