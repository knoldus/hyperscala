package org.hyperscala.html.tag

import org.hyperscala._
import css.StyleSheet
import html.HTMLTag
import org.hyperscala.html.attributes._
import org.hyperscala.html.constraints._
import javascript.{JavaScriptString, JavaScriptContent}

/**
 * NOTE: This file has been generated. Do not modify directly!
 * @author Matt Hicks <mhicks@hyperscala.org>
 */
class Script extends Container[JavaScriptContent] with BodyChild with HeadChild with HTMLTag {
  lazy val xmlLabel = "script"
  override def xmlExpanded = true

  def this(name: String = null,
           accessKey: java.lang.Character = null,
           clazz: List[String] = null,
           contentEditable: ContentEditable = null,
           contextMenu: String = null,
           dir: Direction = null,
           draggable: Draggable = null,
           dropZone: DropZone = null,
           hidden: java.lang.Boolean = null,
           id: String = null,
           lang: String = null,
           spellCheck: java.lang.Boolean = null,
           style: StyleSheet = null,
           tabIndex: java.lang.Integer = null,
           titleText: String = null,
           async: String = null,
           charSet: String = null,
           defer: String = null,
           mimeType: String = null,
           src: String = null,
           content: JavaScriptContent = null) = {
    this()
    up(this.name, name)
    up(this.accessKey, accessKey)
    up(this.clazz, clazz)
    up(this.contentEditable, contentEditable)
    up(this.contextMenu, contextMenu)
    up(this.dir, dir)
    up(this.draggable, draggable)
    up(this.dropZone, dropZone)
    up(this.hidden, hidden)
    up(this.id, id)
    up(this.lang, lang)
    up(this.spellCheck, spellCheck)
    up(this.style, style)
    up(this.tabIndex, tabIndex)
    up(this.titleText, titleText)
    up(this.async, async)
    up(this.charSet, charSet)
    up(this.defer, defer)
    up(this.mimeType, mimeType)
    up(this.src, src)
    if (content != null) contents += content
  }

  val async = PropertyAttribute[String]("async", null)
  val charSet = PropertyAttribute[String]("charset", null)
  val defer = PropertyAttribute[String]("defer", null)
  val mimeType = PropertyAttribute[String]("type", null)
  val src = PropertyAttribute[String]("src", null)

  override protected def processText(text: String) = {
    contents += new JavaScriptString(text)
  }
}