package im.yagni.driveby.report

import im.yagni.driveby.browser.Browser
import im.yagni.driveby.{Specification, Example}
import org.joda.time.DateTime
import org.apache.commons.io.FileUtils
import java.io.File
import im.yagni.driveby.tracking.{Event, BrowserCommandExecuted, Tracker}
import collection.mutable.ListBuffer
import im.yagni.driveby.commands.{Screenshot, Html}
import im.yagni.driveby.DriveByConfig._
import org.joda.time.format.DateTimeFormat

object Reporter {
  val fmt = DateTimeFormat.forPattern("kk:mm:ss:SS");

  def tidyUp() { FileUtils.deleteDirectory(new File(outputDir)) }

  def report(message: String, browser: Browser, example: Example, specification: Specification, failed: Boolean = true) {
    val actualPath = outputDir + (if (failed) "failed/" else "all/")
    val uniqueName = example.id + "_" + specification.name
    val screenshotFilename = new File(actualPath + "screenshot/" + uniqueName + ".png")
    browser.screenshot(screenshotFilename)
    val content = report(message, screenshotFilename, example, browser.html)
    FileUtils.writeStringToFile(new File(actualPath + uniqueName + ".html"), content.toString())
  }

  private def report(message: String, screenshot: File, example: Example, html: String) =
    //TODO: include spec name and example desc in report
    <body>
      <h3>{example.description}</h3>
      <p>{fmt.print(new DateTime)}: <b>{message}</b></p><hr/>
      <img src={"screenshot/" + screenshot.getName}/><hr/>
      <p>{scala.xml.Unparsed(renderEvents(example.id))}</p><hr/>
      <p>{scala.xml.Unparsed(html)}</p>
    </body>

  private def renderEvents(exampleId: Long): String = {
    val interesting = Tracker.allEvents(exampleId).foldLeft(new ListBuffer[String]()) {
      (accumulator, event) => {
        event match {
          case BrowserCommandExecuted(Html(_), _) =>
          case BrowserCommandExecuted(Screenshot(_), _) =>
          case e@BrowserCommandExecuted(c, _) => append(accumulator, e, c.toString)
          case e => append(accumulator, e, e.toString)
        }
        accumulator
      }
    }
    interesting.map(e => "<li><b>" + e + "</b></li>").mkString
  }

  private def append(accumulator: ListBuffer[String], e: Event, m: String) {
    accumulator.append(fmt.print(e.at) + " - " + m)
  }
}