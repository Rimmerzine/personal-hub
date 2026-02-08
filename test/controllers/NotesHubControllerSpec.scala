package controllers

import forms.NoteForm
import models.Note
import play.api.mvc.Result
import play.api.test.Helpers._
import views.mocks.MockNotesHub

import scala.concurrent.Future

class NotesHubControllerSpec extends BaseControllerSpec
  with MockNotesHub {

  object TestNotesHubController extends NotesHubController(
    mockView
  )

  "NotesHubController.show" must {
    "return OK with HTML content" when {
      "there was a note available in session to pre-fill the form" in {
        val note: Note = Note("test-title", "test-body")

        mockNotesHub(
          form = NoteForm.form.fill(note)
        )

        val result: Future[Result] = TestNotesHubController.show()(
          request.withSession("note-title" -> note.title, "note-body" -> note.body)
        )

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
      "there was not a note available in session" in {
        mockNotesHub(
          form = NoteForm.form
        )

        val result: Future[Result] = TestNotesHubController.show()(request)

        status(result) mustBe OK
        contentType(result) mustBe Some(HTML)
      }
    }
  }

  "NotesHubController.submit" when {
    "valid form data is submitted" must {
      "add the note to session, return SEE_OTHER and redirect to the notes hub url" in {
        val result = TestNotesHubController.submit()(
          request.withFormUrlEncodedBody(
            NoteForm.titleKey -> "test-title",
            NoteForm.bodyKey -> "test-body"
          )
        )

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.NotesHubController.show().url)
        session(result).data mustBe Map(
          "note-title" -> "test-title",
          "note-body" -> "test-body",
        )
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
