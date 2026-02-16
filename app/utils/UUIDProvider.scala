package utils

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class UUIDProvider @Inject() {

  def getUUIDString: String = UUID.randomUUID().toString

}
