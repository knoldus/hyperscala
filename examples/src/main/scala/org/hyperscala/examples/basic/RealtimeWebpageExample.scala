package org.hyperscala.examples.basic

import org.hyperscala.web.site.realtime.RealtimeWebpage
import org.hyperscala.html._
import org.powerscala.Color
import org.hyperscala.event.{ChangeEvent, ClickEvent, JavaScriptEvent}
import org.powerscala.property._
import org.powerscala.event.ActionEvent

/**
 * @author Matt Hicks <matt@outr.com>
 */
class RealtimeWebpageExample extends RealtimeWebpage {
  title := "Realtime Example"

  var count = 0
  var reversed = false

  val input = new tag.Input {
    event.change := JavaScriptEvent()

    value.onChange {
      println("Input value changed: %s".format(value()))
    }

    listeners.synchronous {
      case evt: ActionEvent => println("Input event: %s".format(evt))
    }
  }
  body.contents += input

  val select = new tag.Select(id = "realtimeSelect") {
    event.change := JavaScriptEvent()

    contents += new tag.Option(value = "uno", content = "One")
    contents += new tag.Option(value = "dos", content = "Two")
    contents += new tag.Option(value = "tres", content = "Three")

    listeners.synchronous {
      case evt: ChangeEvent => println("Selected: %s".format(selected()))
    }
  }
  body.contents += select

  val textArea = new tag.TextArea {
    event.change := JavaScriptEvent()

    listeners.synchronous {
      case evt: ChangeEvent => println(content())
    }
  }
  body.contents += textArea

  body.contents += new tag.Button(content = "Test Button") {
    event.click := JavaScriptEvent()

    listeners.synchronous {
      case evt: ClickEvent => {
        println("Selected: %s".format(select.selected))
        input.value := "Button clicked %s".format(count)
        contents.replaceWith("Test Button %s".format(count))
        style.color := Color.values.random
        if (count >= 10) {
          reversed = true
        } else if (count <= 0) {
          reversed = false
        }
        if (reversed) {
          count -= 1
          body.contents -= body.contents.last
        } else {
          count += 1
          body.contents += new tag.Div {
            contents += "Testing %s!".format(count)
          }
        }
      }
    }
  }

  override def dispose() {
    super.dispose()
    println("LivePageExample being disposed!")
  }
}
