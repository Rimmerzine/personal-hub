package controllers

import forms.FormUtils.FormExtensions
import forms.NoteForm
import play.api.mvc._
import repositories.NotesRepository
import views.NotesHub

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NotesHubController @Inject()(notesHub: NotesHub,
                                   notesRepository: NotesRepository)
                                  (implicit mcc: MessagesControllerComponents,
                                   executionContext: ExecutionContext) extends BaseFrontendController {

  def show(): Action[AnyContent] = Action.async { implicit request =>
    notesRepository.findFirstNote map { maybeNote =>
      Ok(notesHub(
        noteForm = NoteForm.form.fill(maybeNote)
      ))
    }
  }

  def submit(): Action[AnyContent] = Action.async { implicit request =>
    NoteForm.form.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(notesHub(
          noteForm = formWithErrors
        )))
      },
      successfulNote => {
        notesRepository.saveNote(successfulNote) map { _ =>
          Redirect(routes.NotesHubController.show())
        }
      }
    )
  }

}
