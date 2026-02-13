package controllers

import forms.NoteForm
import models.Note
import play.api.mvc.Result
import play.api.test.Helpers._
import repositories.mocks.MockNotesRepository
import views.mocks.MockNotesHub

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NotesHubControllerSpec extends BaseControllerSpec
  with MockNotesHub
  with MockNotesRepository {

  object TestNotesHubController extends NotesHubController(
    mockView, mockRepository
  )

  "NotesHubController.show" must {
    "return OK with HTML content" when {
      "there was a note available in the database to pre-fill the form" in {
        val note: Note = Note("test-title", "test-body")

        mockFindFirstNote(Some(note))
        mockNotesHub(NoteForm.form.fill(note))

        val result: Future[Result] = TestNotesHubController.show()(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
      "there was not a note available in session" in {
        mockFindFirstNote(None)
        mockNotesHub(NoteForm.form)

        val result: Future[Result] = TestNotesHubController.show()(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
    }
  }

  "NotesHubController.submit" when {
    "valid form data is submitted" must {
      "save the note to the database, return SEE_OTHER and redirect to the notes hub url" in {
        val note: Note = Note("test-title", "test-body")

        mockSaveNote(note)

        val result = TestNotesHubController.submit()(
          request.withFormUrlEncodedBody(
            NoteForm.titleKey -> note.title,
            NoteForm.bodyKey -> note.body
          )
        )

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.NotesHubController.show().url)
      }
    }
    "invalid form data is submitted" must {
      "return a BAD_REQUEST with HTML content" in {
        mockNotesHub(
          form = NoteForm.form.bind(Map.empty[String, String])
        )

        val result = TestNotesHubController.submit()(request)

        status(result) mustBe BAD_REQUEST
        contentType(result) mustBe Some(HTML)
      }
    }
  }

}
