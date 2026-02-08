package controllers

import play.api.mvc._
import views.NotesHub

import javax.inject._

@Singleton
class NotesHubController @Inject()(notesHub: NotesHub)
                                  (implicit mcc: MessagesControllerComponents) extends BaseFrontendController {

  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(notesHub())
  }

  def submit(): Action[AnyContent] = Action { _ =>
    Redirect(routes.NotesHubController.show())
  }

}
