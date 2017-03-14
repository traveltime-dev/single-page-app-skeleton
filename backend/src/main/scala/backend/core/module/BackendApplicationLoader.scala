package backend.core.module

import boopickle.BufferPool
import com.typesafe.config.ConfigFactory
import play.api.{ApplicationLoader, Configuration}
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceApplicationLoader}

class BackendApplicationLoader extends GuiceApplicationLoader {
  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
    BufferPool.disable()

    val configFolder = sys.env("SPA_CONFIG_FOLDER")
    val config       = ConfigFactory.load(s"$configFolder/app.conf")

    val updatedContext = context.copy(initialConfiguration = Configuration(config))
    super.builder(updatedContext)
  }
}
