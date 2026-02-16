package views

import forms.NoteDetailForm
import models.NoteDetail
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import play.api.data.{Form, FormError}

class CreateNoteViewSpec extends BaseViewSpec {

  val createNote: CreateNote = inject[CreateNote]

  def render(form: Form[NoteDetail] = NoteDetailForm.form): Document = {
    Jsoup.parse(createNote(testCall, form).render)
  }

  val fakeNoteTitleError: FormError = FormError(NoteDetailForm.titleKey, "Fake title error")
  val fakeNoteBodyError: FormError = FormError(NoteDetailForm.bodyKey, "Fake body error")

  val defaultDocument: Document = render()
  val valueDocument: Document = render(NoteDetailForm.form.fill(NoteDetail("test-title", "test-body")))
  val errorDocument: Document = render(NoteDetailForm.form.withError(fakeNoteTitleError).withError(fakeNoteBodyError))

  "CreateNote" must {
    "have a title" in {
      defaultDocument.title mustBe CreateNoteMessages.title
    }

    "have a h1" in {
      defaultDocument.selectHead("h1").text mustBe CreateNoteMessages.heading
    }

    "have a form" which {
      "has the correct attributes" in {
        val form: Element = defaultDocument.selectHead("form")

        form.attr("method") mustBe testCall.method
        form.attr("action") mustBe testCall.url
      }

      "has a note title text field" when {
        "the field has no value or error" in {
          val form: Element = defaultDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteDetailForm.titleKey,
            fieldLabel = CreateNoteMessages.noteTitleLabel,
            maybeValue = None,
            maybePlaceholderText = Some(CreateNoteMessages.noteTitlePlaceholder),
            maybeErrorText = None
          )
        }
        "the field has a value available in the form" in {
          val form: Element = valueDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteDetailForm.titleKey,
            fieldLabel = CreateNoteMessages.noteTitleLabel,
            maybeValue = Some("test-title"),
            maybePlaceholderText = Some(CreateNoteMessages.noteTitlePlaceholder),
            maybeErrorText = None
          )
        }
        "the field has an error in the form" in {
          val form: Element = errorDocument.selectHead("form")

          form.mustHaveTextInput("div:nth-of-type(1)")(
            name = NoteDetailForm.titleKey,
            fieldLabel = CreateNoteMessages.noteTitleLabel,
            maybeValue = None,
            maybePlaceholderText = Some(CreateNoteMessages.noteTitlePlaceholder),
            maybeErrorText = Some("Fake title error")
          )
        }
      }

      "has a note body text area" when {
        "the field has no value or error" in {
          val form: Element = defaultDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteDetailForm.bodyKey,
            fieldLabel = CreateNoteMessages.noteBodyLabel,
            maybeValue = None,
            maybeErrorText = None
          )
        }
        "the field has a value available in the form" in {
          val form: Element = valueDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteDetailForm.bodyKey,
            fieldLabel = CreateNoteMessages.noteBodyLabel,
            maybeValue = Some("test-body"),
            maybeErrorText = None
          )
        }
        "the field has an error in the form" in {
          val form: Element = errorDocument.selectHead("form")

          form.mustHaveTextArea("div:nth-of-type(2)")(
            name = NoteDetailForm.bodyKey,
            fieldLabel = CreateNoteMessages.noteBodyLabel,
            maybeValue = None,
            maybeErrorText = Some("Fake body error")
          )
        }
      }

      "has a save button" in {
        val button = defaultDocument.selectHead("form").selectHead("button.btn-primary")
        button.attr("type") mustBe "submit"
        button.text mustBe CreateNoteMessages.save
      }

      "has a cancel button" in {
        val button = defaultDocument.selectHead("form").selectHead("a.btn-secondary")
        button.attr("role") mustBe "button"
        button.attr("href") mustBe controllers.routes.NotesHubController.show().url
      }
    }
  }

  object CreateNoteMessages {
    val title: String = "Create a new note"
    val heading: String = "Create a new note"

    val noteTitleLabel: String = "Note title"
    val noteTitlePlaceholder: String = "Enter a title for your note"

    val noteBodyLabel: String = "Note body"

    val save: String = "Save"
    val cancel: String = "Cancel"
  }

}
