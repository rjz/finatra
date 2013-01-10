package com.twitter.finatra

object MultipartHeaderParser {

  def apply(rawHeaders: String): Map[String, String] = {
    rawHeaders.split("\r\n").foldLeft(Map[String,String]()) { (headers, line) =>
      val parts = line.split(':')
      if (parts.length == 2) {
        val key = parts(0).trim
        val value = parts(1).trim
        headers ++ Map(key -> value)
      } else {
        headers
      }
    }
  }

}
