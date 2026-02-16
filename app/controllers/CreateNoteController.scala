package controllers

import forms.FormUtils.FormExtensions
import forms.NoteDetailForm
import models.Note
import play.api.data.Form
import play.api.mvc._
import repositories.NotesRepository
import utils.UUIDProvider
import views.CreateNote

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreateNoteController @Inject()(createNote: CreateNote,
                                     notesRepository: NotesRepository,
                                     uuidProvider: UUIDProvider)
                                    (implicit mcc: MessagesControllerComponents,
                                     executionContext: ExecutionContext) extends BaseFrontendController {

  def show(id: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    id match {
      case Some(actualId) =>
        notesRepository.findNote(actualId) map { maybeNote =>
          Ok(view(
            id = actualId,
            form = NoteDetailForm.form.fill(maybeNote.map(_.detail))
          ))
        }
      case None =>
        val actualId: String = uuidProvider.getUUIDString
        Future.successful(Ok(view(
          id = actualId,
          form = NoteDetailForm.form
        )))
    }
  }

  def submit(id: String): Action[AnyContent] = Action.async { implicit request =>
    NoteDetailForm.form.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(view(
          id = id,
          form = formWithErrors
        )))
      },
      successfulNoteDetail => {
        notesRepository.saveNote(Note(id, successfulNoteDetail)) map { _ =>
          Redirect(routes.NotesHubController.show())
        }
      }
    )
  }

  private def view(id: String, form: Form[_])
                  (implicit messagesRequestHeader: MessagesRequestHeader) = {
    createNote(
      postAction = routes.CreateNoteController.submit(id),
      noteForm = form
    )
  }

}
