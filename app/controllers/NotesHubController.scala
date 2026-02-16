package controllers

import play.api.mvc._
import repositories.NotesRepository
import views.NotesHub

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class NotesHubController @Inject()(notesHub: NotesHub,
                                   notesRepository: NotesRepository)
                                  (implicit mcc: MessagesControllerComponents,
                                   executionContext: ExecutionContext) extends BaseFrontendController {

  def show(): Action[AnyContent] = Action.async { implicit request =>
    notesRepository.findNotes map { notes =>
      Ok(notesHub(
        notes = notes
      ))
    }
  }

}
