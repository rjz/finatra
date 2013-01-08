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

  val paramParser         = new ParameterParser
  val parsedHeaders       = HeaderParser.getParsedHeaders(rawHeaders)
  val contentDisposition  = parsedHeaders.get("Content-Disposition")
  val headers             = paramParser.parse(contentDisposition, ';').
                              asInstanceOf[java.util.Map[String,String]]

  headers.put("Content-Type", parsedHeaders.get("Content-Type"))

  def name():String = {
    headers.get("name")
  }

  def contentType = {
    headers.get("Content-Type")
  }

  def filename = {
    headers.get("filename")
  }

  def writeToFile(path: String) = {
    val outStream = new FileOutputStream(path)
    data.writeTo(outStream)
    outStream.close
  }
}
