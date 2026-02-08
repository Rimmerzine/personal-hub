package views

import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class NotesHub @Inject()(main: Main) {

  def apply()(implicit messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("notes-hub.heading"))(
      h1(messages("notes-hub.heading")),
      form(method := "POST", action := controllers.routes.NotesHubController.submit().url)(
        div(`class` := "mb-3")(
          label(`class` := "form-label")(messages("notes-hub.note-title.label")),
          input(
            `type` := "text",
            `class` := "form-control",
            id := "note-title",
            placeholder := "Note #1",
            name := "note-title"
          )
        ),
        div(`class` := "mb-3")(
          label(`class` := "form-label")(messages("notes-hub.note-body.label")),
          textarea(
            `type` := "text",
            `class` := "form-control",
            id := "note-body",
            name := "note-body"
          )
        ),
        button(
          `type` := "submit",
          `class` := "btn btn-primary me-1"
        )(messages("base.save")),
        a(
          `class` := "btn btn-secondary",
          role := "button",
          href := controllers.routes.NotesHubController.show().url
        )(messages("base.cancel"))
      )
    )
  }

}
