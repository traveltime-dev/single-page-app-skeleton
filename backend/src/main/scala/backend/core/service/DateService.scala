package backend.core.service

import org.joda.time.DateTime
import com.google.inject.Singleton

@Singleton
class DateService {

  def date(): String = {
    new DateTime().toString
  }
}
