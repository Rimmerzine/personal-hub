package repositories.utils

import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class MongoComponent @Inject()(configuration: Configuration, lifecycle: ApplicationLifecycle) {

  private val uriConfigPath: String = "mongodb.uri"
  private val databaseNameConfigPath: String = "mongodb.name"

  private lazy val uri: String = {
    configuration.get[String](uriConfigPath)
  }

  private lazy val name: String = {
    configuration.get[String](databaseNameConfigPath)
  }

  private lazy val mongoClient: MongoClient = MongoClient(uri)

  lazy val database: MongoDatabase = mongoClient.getDatabase(name)

  // $COVERAGE-OFF$
  lifecycle.addStopHook { () =>
    mongoClient.close()
    Future.successful(())
  }
  // $COVERAGE-ON$

}
