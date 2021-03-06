package im.mange.driveby.pool

import im.mange.driveby.browser.Browser
import im.mange.driveby.{ExampleAware, BrowserAware, DriveBy, Example}

trait PooledBrowsers extends ExampleAware with BrowserAware {
  var pooledBrowser: Option[Browser] = None

  override def beforeExample = takeBrowser _ :: super.beforeExample
  override def afterExample = writeBrowser _ :: super.afterExample

  private def takeBrowser(example: Example) { pooledBrowser = DriveBy.browserPool.take(example) }
  private def writeBrowser(example: Example) { DriveBy.browserPool.write(pooledBrowser, example) }
}