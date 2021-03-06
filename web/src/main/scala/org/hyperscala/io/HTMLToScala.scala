package org.hyperscala.io

import org.hyperscala.html._
import org.jdom2.input.SAXBuilder
import java.io.{FileWriter, File, StringReader}
import io.Source
import org.htmlcleaner.{PrettyXmlSerializer, HtmlCleaner}
import org.hyperscala.{Markup, Unique}
import org.hyperscala.web.site.Webpage
import org.powerscala.reflect.DynamicCompiler
import swing.FileChooser
import java.util.prefs.Preferences

/**
 * @author Matt Hicks <mhicks@powerscala.org>
 */
object HTMLToScala {
  lazy val WebpageTemplate = Source.fromURL(getClass.getClassLoader.getResource("webpage.template")).mkString
  lazy val TagTemplate = Source.fromURL(getClass.getClassLoader.getResource("tag.template")).mkString

  val builder = new SAXBuilder()

  def toScala(page: Webpage, packageName: String, className: String) = {
    val b = new ScalaWebpageBuffer(packageName, className, page)
    b.code
  }

  def toScala(tag: HTMLTag, packageName: String, className: String) = {
    val b = new ScalaTagBuffer(packageName, className, tag)
    b.code
  }

  def toInstantiator[T <: HTMLTag](tag: HTMLTag, className: String) = {
    val source = toScala(tag, null, className)
    val file = File.createTempFile("hyperscala", ".scala")
    try {
      val writer = new FileWriter(file)
      try {
        writer.write(source)
      } finally {
        writer.flush()
        writer.close()
      }
      DynamicCompiler[T](className, file.toURI.toURL)
    } finally {
      file.delete()
    }
  }

  def toInstantiator[T <: HTMLTag](source: Source, clean: Boolean) = {
    val tag = toHTML(source, clean)
    val className = "Custom%s".format(Unique())
    toInstantiator[T](tag, className)
  }

  def toPage(source: Source, clean: Boolean = true) = {
    val page = new Webpage()
    page.html.read(toXML(source, clean))
    page
  }

  def toHTML(source: Source, clean: Boolean) = {
    val xml = toXML(source, clean)
    val root = HTMLTag.create(xml.getName)
    root.read(xml)
    root
  }

  def toXML(source: Source, clean: Boolean) = {
    val content = if (clean) {
      val html = source.mkString
      val cleaner = new HtmlCleaner()
      val props = cleaner.getProperties
      val cleaned = cleaner.clean(html)
      new PrettyXmlSerializer(props).getAsString(cleaned)
    } else {
      source.mkString
    }
    builder.build(new StringReader(content)).getRootElement
  }

  def main(args: Array[String]): Unit = {
    Markup.UnsupportedAttributeException = false
    val preferences = Preferences.userNodeForPackage(getClass)
    val filePath = preferences.get("path", ".")
    val chooser = new FileChooser(new File(filePath))
    chooser.title = "Select the HTML file to process"
    chooser.showOpenDialog(null) match {
      case FileChooser.Result.Approve => {
        val file = chooser.selectedFile
        preferences.put("path", file.getParentFile.getAbsolutePath)

        val webpage = toPage(Source.fromFile(file), clean = true)

        val savePath = preferences.get("savePath", preferences.get("path", "."))
        val saver = new FileChooser(new File(savePath))
        saver.title = "Select the location to save the generate code"
        saver.showSaveDialog(null) match {
          case FileChooser.Result.Approve => {
            val file = saver.selectedFile
            preferences.put("savePath", file.getParentFile.getAbsolutePath)

            val scala = toScala(webpage, file.getParentFile.getName, file.getName.substring(0, file.getName.indexOf('.')))
            val writer = new FileWriter(file)
            try {
              writer.write(scala)
            } finally {
              writer.flush()
              writer.close()
            }
          }
          case _ => // Cancelled save
        }
      }
      case _ => // Cancelled open
    }
  }
}