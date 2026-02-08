package views

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class NotesHubViewSpec extends BaseViewSpec {

  val notesHub: NotesHub = inject[NotesHub]
  val document: Document = Jsoup.parse(notesHub().render)

  "NotesHub" must {
    "have a title" in {
      document.title mustBe NotesHubMessages.title
    }

    "have a h1" in {
      document.selectHead("h1").text mustBe NotesHubMessages.heading
    }

    "have a form" which {
      def form: Element = document.selectHead("form")

      "has the correct attributes" in {
        form.attr("method") mustBe "POST"
        form.attr("action") mustBe controllers.routes.NotesHubController.submit().url
      }

      "has a note title text field" which {
        def titleField: Element = form.selectNth("div", 1)

        "has a label" in {
          titleField.selectHead("label").text mustBe NotesHubMessages.noteTitleLabel
        }
        "has a text input" in {
          val input: Element = titleField.selectHead("input")

          input.attr("id") mustBe "note-title"
          input.attr("name") mustBe "note-title"
          input.attr("type") mustBe "text"
          input.attr("placeholder") mustBe NotesHubMessages.noteTitlePlaceholder
        }
      }

      "has a note body text area" which {
        def bodyField: Element = form.selectNth("div", 2)

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
        val button = form.selectHead("button.btn-primary")
        button.attr("type") mustBe "submit"
        button.text mustBe NotesHubMessages.save
      }

      "has a cancel button" in {
        val button = form.selectHead("a.btn-secondary")
        button.attr("role") mustBe "button"
        button.attr("href") mustBe controllers.routes.NotesHubController.show().url
      }
    }
  }

  object NotesHubMessages {
    val title: String = "Notes"
    val heading: String = "Notes"
    val noteTitleLabel: String = "Note title"
    val noteTitlePlaceholder: String = "Note #1"
    val noteBodyLabel: String = "Note body"

    val save: String = "Save"
    val cancel: String = "Cancel"
  }

}
