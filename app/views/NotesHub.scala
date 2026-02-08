package views

import forms.NoteForm
import models.Note
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.RequestHeader
import play.filters.csrf.CSRF
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class NotesHub @Inject()(main: Main) {

  def apply(noteForm: Form[Note])(implicit request: RequestHeader, messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("notes-hub.heading"))(
      h1(messages("notes-hub.heading")),
      form(method := "POST", action := controllers.routes.NotesHubController.submit().url)(
        input(`type` := "hidden", name := "csrfToken", value := CSRF.getToken.map(_.value).getOrElse("")),
        div(`class` := "mb-3")(
          label(`class` := "form-label")(messages("notes-hub.note-title.label")),
          input(
            `type` := "text",
            noteForm(NoteForm.titleKey).error.map { error =>
              `class` := "form-control is-invalid"
            }.getOrElse(
              `class` := "form-control"
            ),
            id := noteForm(NoteForm.titleKey).id,
            placeholder := "Note #1",
            name := noteForm(NoteForm.titleKey).name,
            value := noteForm(NoteForm.titleKey).value.getOrElse(""),
            noteForm(NoteForm.titleKey).error.map { error =>
              aria.describedby := s"${error.key}-error"
            }
          ),
          noteForm(NoteForm.titleKey).error.map { error =>
            div(id := s"${error.key}-error", `class` := "invalid-feedback")(
              messages(error.message, error.args: _*)
            )
          }
        ),
        div(`class` := "mb-3")(
          label(`class` := "form-label")(messages("notes-hub.note-body.label")),
          textarea(
            `type` := "text",
            `class` := "form-control",
            id := noteForm(NoteForm.bodyKey).id,
            name := noteForm(NoteForm.bodyKey).name
          )(noteForm(NoteForm.bodyKey).value)
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
