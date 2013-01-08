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

import org.apache.commons.fileupload._
import java.io._
import scala.collection.mutable.Map
import com.twitter.finagle.http.{Request => FinagleRequest}
import org.jboss.netty.handler.codec.http.HttpRequestDecoder

object MultipartParsing {

  def loadMultiParams(request: FinagleRequest) = {
    var multiParams = Map[String, MultipartItem]()

    request.getContent.markReaderIndex()
    val contentType = request.headers.get("Content-Type").getOrElse(null)

    if(contentType != null){
      val boundaryIndex = contentType.indexOf("boundary=")
      val boundary      = contentType.substring(boundaryIndex + 9).getBytes
      val input         = request.getInputStream()
      val multistream   = new MultipartStream(input, boundary)
      var nextPart      = multistream.skipPreamble

      while(nextPart){

        val hdec = new HttpRequestDecoder()
        val msg = hdec
        val paramParser = new ParameterParser
        val rawHeaders = multistream.readHeaders()
        val nheaders = HeaderParser.getParsedHeaders(rawHeaders)
        val headers     = paramParser.parse(nheaders.get("Content-Disposition"), ';').asInstanceOf[java.util.Map[String,String]]
        headers.put("Content-Type", nheaders.get("Content-Type"))
        val out         = new ByteArrayOutputStream
        val name        = headers.get("name").toString

        multistream.readBodyData(out)

        val fileobj = new MultipartItem(Tuple2(headers, out))
        multiParams = multiParams + Tuple2(name, fileobj)
        nextPart    = multistream.readBoundary
      }
    }

    request.getContent.resetReaderIndex

    multiParams
  }

}
