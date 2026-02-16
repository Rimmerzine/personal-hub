package controllers

import forms.NoteDetailForm
import models.{Note, NoteDetail}
import play.api.mvc.Result
import play.api.test.Helpers._
import repositories.mocks.MockNotesRepository
import utils.mocks.MockUUIDProvider
import views.mocks.MockCreateNote

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateNoteControllerSpec extends BaseControllerSpec
  with MockCreateNote
  with MockNotesRepository
  with MockUUIDProvider {

  object TestCreateNoteController extends CreateNoteController(
    mockView, mockRepository, mockUUIDProvider
  )

  "show" must {
    "return OK with HTML content" when {
      "there was a note available in the database to pre-fill the form which matched the id provided" in {
        val note: Note = Note(
          id = "test-id",
          detail = NoteDetail(
            title = "test-title",
            body = "test-body"
          )
        )

        mockFindNote(note.id)(Some(note))
        mockCreateNote(
          postAction = routes.CreateNoteController.submit(note.id),
          form = NoteDetailForm.form.fill(note.detail)
        )

        val result: Future[Result] = TestCreateNoteController.show(id = Some("test-id"))(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }

      "there was not a note available in the database to match the id provided" in {
        mockFindNote("test-id")(None)
        mockCreateNote(
          postAction = routes.CreateNoteController.submit("test-id"),
          form = NoteDetailForm.form
        )

        val result: Future[Result] = TestCreateNoteController.show(id = Some("test-id"))(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }

      "there was no id provided" in {
        mockGetUUIDString("test-new-uuid")
        mockCreateNote(
          postAction = routes.CreateNoteController.submit("test-new-uuid"),
          form = NoteDetailForm.form
        )

        val result: Future[Result] = TestCreateNoteController.show(id = None)(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
    }
  }

  "submit" when {
    "valid form data is submitted" must {
      "save the note to the database, return SEE_OTHER and redirect to the notes hub url" in {
        val note: Note = Note(
          id = "test-id",
          detail = NoteDetail(
            title = "test-title",
            body = "test-body"
          )
        )

        mockSaveNote(note)

        val result = TestCreateNoteController.submit("test-id")(
          request.withFormUrlEncodedBody(
            NoteDetailForm.titleKey -> note.detail.title,
            NoteDetailForm.bodyKey -> note.detail.body
          )
        )

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.NotesHubController.show().url)
      }
    }

    "invalid form data is submitted" must {
      "return a BAD_REQUEST with HTML content" in {
        mockCreateNote(
          postAction = routes.CreateNoteController.submit("test-id"),
          form = NoteDetailForm.form.bind(Map.empty[String, String])
        )

        val result = TestCreateNoteController.submit("test-id")(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some(HTML)
      }
    }
  }

}
