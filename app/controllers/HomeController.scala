package controllers

import play.api.mvc._
import views.Home

import javax.inject._

@Singleton
class HomeController @Inject()(home: Home,
                               mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc)
  with ScalaTagWritable {

  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(home())
  }

}
