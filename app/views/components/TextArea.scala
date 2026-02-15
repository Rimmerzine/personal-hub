package views.components

import play.api.data.Field
import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class TextArea @Inject() {

  def apply(field: Field, fieldLabel: String)(implicit messages: Messages): TypedTag[String] = {

    div(`class` := "mb-3")(
      label(
        `class` := "form-label",
        `for` := field.id
      )(fieldLabel),
      textarea(
        `type` := "text",
        `class` := "form-control",
        id := field.id,
        name := field.name,
        field.error.map(error =>
          Seq(
            `class` := "is-invalid",
            aria.describedby := s"${error.key}-error"
          )
        )
      )(field.value),
      field.error.map(error =>
        div(id := s"${error.key}-error", `class` := "invalid-feedback")(
          messages(error.message, error.args: _*)
        )
      )
    )

  }

}
