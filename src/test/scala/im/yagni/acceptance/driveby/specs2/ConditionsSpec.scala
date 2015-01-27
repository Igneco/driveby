package im.yagni.acceptance.driveby.specs2

import im.yagni.driveby.conditions._
import im.yagni.common.ConditionNotMetException
import im.yagni.driveby.{Id, Class}
import im.yagni.acceptance.driveby.driver.BrowserDriver

class ConditionsSpec extends WebSpecification {
  //TODO: test missing conditions
  //TODO: test all sad paths
  //TODO: test probing (via delays somehow)
  //TODO: find out why some messages have an extra " " before butWas - is it Assert(message)?
  //TODO: find out why some messages have an extra " " before (not met within ...) - is it Assert(message)?
  //TODO: review/improve all messages
  //TODO: split out into one spec per condition

  //TODO: these next two shouldnt be here ...
  //TODO: need to make this work in a nicer way
  //  def `current url must be the page url` {
  //    given.page(<html/>)
  //            .assert(CurrentUrlEquals("foo"))
  //  }

  //  TODO: need to make this work in a nicer way
  //  def `current url must contain the expected` {
  //    given.page(<html/>)
  //      .assert(CurrentUrlContains("foo"))
  //  }

  "ElementCountEquals" should {
    "count by class" in rawBrowser {
      (given: BrowserDriver) => {
        val clazz = Class("thing")
        given.page(<b class={clazz.className}>thing1</b> <b class={clazz.className}>thing2</b> <b class={clazz.className}>thing3</b>)
          .assert(ElementCountEquals(clazz, 3))
      }
    }
  }

  "TextContains" should {
    "work for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("contains")
        given.page(<b id={id.id}>foocontainsbar</b>)
          .assert(TextContains(id, "contains"))
      }
    }
  }

  "ElementVisible" should {
    "work for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("visible")
        given.page(<b id={id.id} style="display:block">visible</b>)
          .assert(ElementVisible(id))
      }
    }
  }

  "ElementHidden" should {
    "work for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hidden")
        given.page(<b id={id.id} style="display:none">hidden</b>)
          .assert(ElementHidden(id))
      }
    }
  }

  "ElementClassesContains" should {
    "pass for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hasClassWith")
        given.page(<b id={id.id} class="wobbleClass hasClass wibbleClass">hasClassWith</b>)
          .assert(ElementClassesContains(id, "hasClass"))
      }
    }

    "fail for id without" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hasClassWithout")
        given.page(<b id={id.id} class="nothasClass">hasClassWithout</b>)
          .assert(ElementClassesContains(id, "hasClass")) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementClassesContains("Id(hasClassWithout)", "hasClass") but was "nothasClass"  (not met within 2000 millis)"""))
      }
    }
  }

  "ElementClassesEquals" should {
    "pass for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hasClassWith")
        given.page(<b id={id.id} class="wobbleClass hasClass wibbleClass">hasClassWith</b>)
          .assert(ElementClassesEquals(id, Set("hasClass", "wibbleClass", "wobbleClass")))
      }
    }

    "fail for id with missing" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hasClassWithMissing")
        given.page(<b id={id.id} class="nothasClass">hasClassWithMissing</b>)
          .assert(ElementClassesEquals(id, Set("hasClass"))) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementClassesEqual("Id(hasClassWithMissing)", "hasClass") but was "nothasClass"  (not met within 2000 millis)"""))
      }
    }

    "fail for id with extra" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("hasClassWithExtra")
        given.page(<b id={id.id} class="hasClass extraClass">hasClassWithExtra</b>)
          .assert(ElementClassesEquals(id, Set("hasClass"))) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementClassesEqual("Id(hasClassWithExtra)", "hasClass") but was "hasClass extraClass"  (not met within 2000 millis)"""))
      }
    }
  }

  //TODO: have a spec ...
//  "SelectOptionsEquals" should {
//    "pass happy path" in rawBrowser {
//      (given: BrowserDriver) => {
//        val id = Id("TextEmpty")
//        given.page(<b id={id.id}></b>)
//          .assert(TextEmpty(id))
//      }
//    }
//  }

  "TextEmpty" should {
    "pass for id" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("TextEmpty")
        given.page(<b id={id.id}></b>)
          .assert(TextEmpty(id))
      }
    }

    "pass for id with children" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("TextEmptyWithChild")
        given.page(<b id={id.id}><div></div></b>)
          .assert(TextEmpty(id))
      }
    }

    "fail for id with text" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("TextEmptyWithText")
        given.page(<b id={id.id}>text</b>)
          .assert(TextEmpty(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert TextEmpty("Id(TextEmptyWithText)", "true") but was "text"  (not met within 2000 millis)"""))
      }
    }

    "fail for id with children with text" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("TextEmptyWithChildWithText")
        given.page(<b id={id.id}><div>childtext</div></b>)
          .assert(TextEmpty(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert TextEmpty("Id(TextEmptyWithChildWithText)", "true") but was "childtext"  (not met within 2000 millis)"""))
      }
    }
  }

  "ElementEmpty" should {
    "pass for id when no children and no text" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementEmpty")
        given.page(<b id={id.id}></b>)
          .assert(ElementEmpty(id))
       }
    }

    "fail for id with children" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementEmptyWithChild")
        given.page(<b id={id.id}><div></div></b>)
          .assert(ElementEmpty(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementEmpty("Id(ElementEmptyWithChild)", "true") but was "Element has 1 children and "" text"  (not met within 2000 millis)"""))
      }
    }

    "fail for id with text" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementEmptyWithText")
        given.page(<b id={id.id}>textInElement</b>)
          .assert(ElementEmpty(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementEmpty("Id(ElementEmptyWithText)", "true") but was "Element has 0 children and "textInElement" text"  (not met within 2000 millis)"""))
      }
    }
  }

  "ElementDisabled" should {
    "pass for id when disabled" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementDisabledWhenDisabled")
        given.page(<form><input disabled="disabled" id={id.id}/></form>)
          .assert(ElementDisabled(id))
       }
    }

    "fail for id not disabled" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementNotDisabledWhenNotDisabled")
        given.page(<form><input id={id.id}/></form>)
          .assert(ElementDisabled(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementDisabled("Id(ElementNotDisabled)") but was "true"  (not met within 2000 millis)"""))
      }
    }
  }

  "ElementEnabled" should {
    "pass for id when enabled" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementEnabledWhenEnabled")
        given.page(<form><input id={id.id}/></form>)
          .assert(ElementEnabled(id))
       }
    }

    "fail for id not enabled" in rawBrowser {
      (given: BrowserDriver) => {
        val id = Id("ElementEnabledWhenNotEnabled")
        given.page(<form><input disabled="disabled" id={id.id}/></form>)
          .assert(ElementEnabled(id)) must throwA(new ConditionNotMetException(
          """> FAILED: Assert ElementEnabled("Id(ElementNotEnabled)") but was "false"  (not met within 2000 millis)"""))
      }
    }
  }
}