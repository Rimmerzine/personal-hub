package controllers

import play.api.mvc._
import views.html.Index

import javax.inject._

@Singleton
class HomeController @Inject()(index: Index, val controllerComponents: ControllerComponents) extends BaseController {

  def home(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(index())
  }

}
