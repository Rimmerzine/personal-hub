package controllers

import forms.FormUtils.FormExtensions
import forms.NoteForm
import models.Note
import play.api.mvc._
import views.NotesHub

import javax.inject._

@Singleton
class NotesHubController @Inject()(notesHub: NotesHub)
                                  (implicit mcc: MessagesControllerComponents) extends BaseFrontendController {

  def show(): Action[AnyContent] = Action { implicit request =>
    Ok(notesHub(
      noteForm = NoteForm.form.fill(getMaybeSessionNote)
    ))
  }

  def submit(): Action[AnyContent] = Action { implicit request =>
    NoteForm.form.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(notesHub(
          noteForm = formWithErrors
        ))
      },
      successfulNote => {
        Redirect(routes.NotesHubController.show())
          .addingToSession(
            "note-title" -> successfulNote.title,
            "note-body" -> successfulNote.body
          )
      }
    )
  }

  private def getMaybeSessionNote(implicit request: Request[_]): Option[Note] = {
    (request.session.get("note-title"), request.session.get("note-body")) match {
      case (Some(title), Some(body)) => Some(Note(title, body))
      case _ => None
    }
  }

}
