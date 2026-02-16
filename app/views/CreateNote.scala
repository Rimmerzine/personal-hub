package views

import forms.NoteDetailForm
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.{Call, RequestHeader}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.components.{FormWithCSRF, TextArea, TextInput}

import javax.inject.{Inject, Singleton}

@Singleton
class CreateNote @Inject()(main: Main, formWithCSRF: FormWithCSRF, textInput: TextInput, textArea: TextArea) {

  def apply(postAction: Call, noteForm: Form[_])(implicit request: RequestHeader, messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("create-note.heading"))(
      h1(messages("create-note.heading")),
      formWithCSRF(postAction)(
        textInput(
          field = noteForm(NoteDetailForm.titleKey),
          fieldLabel = messages("create-note.note-title.label"),
          placeholderText = Some(messages("create-note.note-title.placeholder"))
        ),
        textArea(
          field = noteForm(NoteDetailForm.bodyKey),
          fieldLabel = messages("create-note.note-body.label")
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