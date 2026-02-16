package views

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.title

import javax.inject.{Inject, Singleton}

@Singleton
class Main @Inject() {

  def apply(pageTitle: String)(content: Modifier*): TypedTag[String] = {
    html(
      head(
        title(pageTitle),
        link(
          href := "https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css",
          rel := "stylesheet",
          integrity := "sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB",
          crossorigin := "anonymous"
        ),
        link(
          rel := "stylesheet",
          media := "screen",
          href := controllers.routes.Assets.versioned("stylesheets/main.css").url
        ),
        link(
          rel := "shortcut icon",
          `type` := "image/png",
          href := controllers.routes.Assets.versioned("images/favicon.png").url
        )
      ),
      body(
        content,
        script(
          src := "https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js",
          integrity := "sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI",
          crossorigin := "anonymous"
        ),
        script(
          src := controllers.routes.Assets.versioned("javascripts/main.js").url,
          `type` := "text/javascript"
        )
      )
    )
  }

}
