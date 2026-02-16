package views

import models.Note
import play.api.i18n.Messages
import play.api.mvc.RequestHeader
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class NotesHub @Inject()(main: Main) {

  def apply(notes: Seq[Note])(implicit request: RequestHeader, messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("notes-hub.heading"))(
      h1(messages("notes-hub.heading")),
      notes.map(note => p(
        span(`class` := "bold")(note.detail.title),
        span(" - "),
        span(note.detail.bodySummary)
      ))
    )
  }

}
