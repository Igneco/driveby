package im.mange.driveby.conditions

import im.mange.driveby.{Condition, Describer}
import im.mange.driveby.browser.UnSafeBrowser

case class TitleEquals(expected: String) extends Condition {
  import Describer._

  def expectation = expect("TitleEquals", List(expected))
  def isSatisfied(browser: UnSafeBrowser) = { browser.title == expected }
  def describeFailure(browser: UnSafeBrowser) = { expectation + butWas(() => browser.title) }
}