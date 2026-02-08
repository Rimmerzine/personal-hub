package forms

import play.api.data.Form

object FormUtils {

  implicit class FormExtensions[T](form: Form[T]) {

    def fill(maybeValue: Option[T]): Form[T] = maybeValue match {
      case Some(value) => form.fill(value)
      case None => form
    }

  }

}

