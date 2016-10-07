import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.OFF

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] [%-5level] [%logger{6}] : %msg%n"
  }
}
root(INFO, ["STDOUT"])

//logger("ivan.dev.web", DEBUG, ["STDOUT"])
//logger("ivan.dev.web", WARN, ["STDOUT"])

//Append to file
//def bySecond = timestamp("yyyyMMdd'T'HHmmss")
//appender("FILE", FileAppender) {
//  file = "./log/log-${bySecond}.log"
//  append = true
//  encoder(PatternLayoutEncoder) {
//    pattern = "%level %logger - %msg%n"
//  }
//}