package views

import forms.NoteForm
import models.Note
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.RequestHeader
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.components.{FormWithCSRF, TextArea, TextInput}

import javax.inject.{Inject, Singleton}

@Singleton
class NotesHub @Inject()(main: Main, formWithCSRF: FormWithCSRF, textInput: TextInput, textArea: TextArea) {

  def apply(noteForm: Form[Note])(implicit request: RequestHeader, messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("notes-hub.heading"))(
      h1(messages("notes-hub.heading")),
      formWithCSRF(controllers.routes.NotesHubController.submit())(
        textInput(
          field = noteForm(NoteForm.titleKey),
          fieldLabel = messages("notes-hub.note-title.label"),
          placeholderText = Some(messages("notes-hub.note-title.placeholder"))
        ),
        textArea(
          field = noteForm(NoteForm.bodyKey),
          fieldLabel = messages("notes-hub.note-body.label")
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
