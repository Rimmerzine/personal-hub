package controllers

import play.api.mvc._
import views.html.Home

import javax.inject._

@Singleton
class HomeController @Inject()(home: Home, val controllerComponents: MessagesControllerComponents) extends MessagesBaseController {

  def show(): Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(home())
  }

}
