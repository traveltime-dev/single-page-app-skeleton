package backend.core.module

import com.google.inject.{AbstractModule, Inject, Provider}
import backend.core.model.AppConfig
import play.api.Configuration

class ConfigModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AppConfig])
      .toProvider(classOf[ConfigProvider])
      .asEagerSingleton()
  }
}

class ConfigProvider @Inject()(config: Configuration) extends Provider[AppConfig] {
  override def get(): AppConfig = {
    AppConfig()
  }
}
