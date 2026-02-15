package views

import forms.NoteForm
import models.Note
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import play.api.data.{Form, FormError}

class NotesHubViewSpec extends BaseViewSpec {

  val notesHub: NotesHub = inject[NotesHub]

  def render(form: Form[Note] = NoteForm.form): Document = {
    Jsoup.parse(notesHub(form).render)
  }

  val fakeNoteTitleError: FormError = FormError(NoteForm.titleKey, "Fake title error")
  val fakeNoteBodyError: FormError = FormError(NoteForm.bodyKey, "Fake body error")

  val defaultDocument: Document = render()
  val valueDocument: Document = render(NoteForm.form.fill(Note("test-title", "test-body")))
  val errorDocument: Document = render(NoteForm.form.withError(fakeNoteTitleError).withError(fakeNoteBodyError))

  "NotesHub" must {
    "have a title" in {
      defaultDocument.title mustBe NotesHubMessages.title
    }

    "have a h1" in {
      defaultDocument.selectHead("h1").text mustBe NotesHubMessages.heading
    }

    "have a form" which {
      "has the correct attributes" in {
        val form: Element = defaultDocument.selectHead("form")

        form.attr("method") mustBe "POST"
        form.attr("action") mustBe controllers.routes.NotesHubController.submit().url
      }

      "has a note title text field" when {
        "the field has no value or error" in {
          val form: Element = defaultDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteForm.titleKey,
            fieldLabel = NotesHubMessages.noteTitleLabel,
            maybeValue = None,
            maybePlaceholderText = Some(NotesHubMessages.noteTitlePlaceholder),
            maybeErrorText = None
          )
        }
        "the field has a value available in the form" in {
          val form: Element = valueDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteForm.titleKey,
            fieldLabel = NotesHubMessages.noteTitleLabel,
            maybeValue = Some("test-title"),
            maybePlaceholderText = Some(NotesHubMessages.noteTitlePlaceholder),
            maybeErrorText = None
          )
        }
        "the field has an error in the form" in {
          val form: Element = errorDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteForm.titleKey,
            fieldLabel = NotesHubMessages.noteTitleLabel,
            maybeValue = None,
            maybePlaceholderText = Some(NotesHubMessages.noteTitlePlaceholder),
            maybeErrorText = Some("Fake title error")
          )
        }
      }

      "has a note body text area" when {
        "the field has no value or error" in {
          val form: Element = defaultDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteForm.bodyKey,
            fieldLabel = NotesHubMessages.noteBodyLabel,
            maybeValue = None,
            maybeErrorText = None
          )
        }
        "the field has a value available in the form" in {
          val form: Element = valueDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteForm.bodyKey,
            fieldLabel = NotesHubMessages.noteBodyLabel,
            maybeValue = Some("test-body"),
            maybeErrorText = None
          )
        }
        "the field has an error in the form" in {
          val form: Element = errorDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteForm.bodyKey,
            fieldLabel = NotesHubMessages.noteBodyLabel,
            maybeValue = None,
            maybeErrorText = Some("Fake body error")
          )
        }
      }

      "has a note body text area" which {
        def bodyField: Element = defaultDocument.selectHead("form").selectNth("div", 2)

        "has a label" in {
          bodyField.selectHead("label").text mustBe NotesHubMessages.noteBodyLabel
        }
        "has a text area" in {
          val input: Element = bodyField.selectHead("textarea")

          input.attr("id") mustBe "note-body"
          input.attr("name") mustBe "note-body"
          input.attr("type") mustBe "text"
        }
      }

      "has a save button" in {
        val button = defaultDocument.selectHead("form").selectHead("button.btn-primary")
        button.attr("type") mustBe "submit"
        button.text mustBe NotesHubMessages.save
      }

      "has a cancel button" in {
        val button = defaultDocument.selectHead("form").selectHead("a.btn-secondary")
        button.attr("role") mustBe "button"
        button.attr("href") mustBe controllers.routes.NotesHubController.show().url
      }
    }
  }

  object NotesHubMessages {
    val title: String = "Notes"
    val heading: String = "Notes"

    val noteTitleLabel: String = "Note title"
    val noteTitlePlaceholder: String = "Enter a title for your note"

    val noteBodyLabel: String = "Note body"

    val save: String = "Save"
    val cancel: String = "Cancel"
  }

}
