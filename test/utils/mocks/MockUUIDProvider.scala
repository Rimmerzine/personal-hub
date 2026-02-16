package utils.mocks

import org.mockito.Mockito.{reset, when}
import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.mockito.MockitoSugar
import utils.UUIDProvider

trait MockUUIDProvider extends MockitoSugar with BeforeAndAfterEach {
  suite: Suite =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockUUIDProvider)
  }

  val mockUUIDProvider: UUIDProvider = mock[UUIDProvider]

  def mockGetUUIDString(uuid: String): Unit = {
    when(mockUUIDProvider.getUUIDString)
      .thenReturn(uuid)
  }

}
