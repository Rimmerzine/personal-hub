package views

import models.{Note, NoteDetail}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class NotesHubViewSpec extends BaseViewSpec {

  val notesHub: NotesHub = inject[NotesHub]

  val noteOne: Note = Note("test-id-one", NoteDetail("test-title-one", "test-body-one"))
  val noteTwo: Note = Note("test-id-two", NoteDetail("test-title-two", "test-body-two"))

  val defaultDocument: Document = Jsoup.parse(notesHub(
    Seq(
      noteOne,
      noteTwo
    )
  ).render)

  "NotesHub" must {
    "have a title" in {
      defaultDocument.title mustBe NotesHubMessages.title
    }

    "have a h1" in {
      defaultDocument.selectHead("h1").text mustBe NotesHubMessages.heading
    }

    "have a first note paragraph" in {
      val paragraph: Element = defaultDocument.selectNth("p", 1)

      paragraph.selectNth("span", 1).text mustBe noteOne.detail.title
      paragraph.selectNth("span", 2).text mustBe "-"
      paragraph.selectNth("span", 3).text mustBe noteOne.detail.bodySummary
    }

    "have a second note paragraph" in {
      val paragraph: Element = defaultDocument.selectNth("p", 2)

      paragraph.selectNth("span", 1).text mustBe noteTwo.detail.title
      paragraph.selectNth("span", 2).text mustBe "-"
      paragraph.selectNth("span", 3).text mustBe noteTwo.detail.bodySummary
    }
  }

  object NotesHubMessages {
    val title: String = "Notes"
    val heading: String = "Notes"
  }

}
