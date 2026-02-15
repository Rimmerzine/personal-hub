package views.components

import play.api.mvc.{Call, RequestHeader}
import play.filters.csrf.CSRF
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class FormWithCSRF @Inject() {

  def apply(call: Call)
           (content: TypedTag[String]*)
           (implicit request: RequestHeader): TypedTag[String] = {

    form(method := call.method, action := call.url)(
      input(`type` := "hidden", name := "csrfToken", value := CSRF.getToken.map(_.value).getOrElse("")),
      content
    )

  }

}
