/**
 * Copyright (C) 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.finatra

import java.io._
import org.apache.commons.fileupload.ParameterParser

class MultipartItem(val rawHeaders: String, val data: ByteArrayOutputStream) {

  lazy val paramParser         = new ParameterParser
  lazy val parsedHeaders       = MultipartHeaderParser(rawHeaders)

  lazy val contentDispositionMap = {
    parsedHeaders.get("Content-Disposition") map { cp =>
      paramParser.parse(cp, ';').asInstanceOf[java.util.Map[String,String]]
    }
  }

  def name(): Option[String] = {
    contentDispositionMap map (_.get("name"))
  }

  def contentType: Option[String] = {
    parsedHeaders.get("Content-Type")
  }

  def filename: Option[String] = {
    contentDispositionMap map (_.get("filename"))
  }

  def writeToFile(path: String) = {
    val outStream = new FileOutputStream(path)
    data.writeTo(outStream)
    outStream.close
  }
}
