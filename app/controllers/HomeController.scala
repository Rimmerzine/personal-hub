package controllers

import play.api.mvc._
import views.Home

import javax.inject._

@Singleton
class HomeController @Inject()(home: Home)
                              (implicit mcc: MessagesControllerComponents) extends BaseFrontendController {

  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(home())
  }

}
