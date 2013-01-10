package com.twitter.finatra

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


class MutlipartSpec extends FlatSpec with ShouldMatchers  {

  val rawHeaders1 = "Content-Disposition: form-data; name=\"avatar\"; filename=\"a5.JPG\"\r\nContent-Type: image/jpeg\r\n\r\n"
  val rawHeaders2 = "  Content-Disposition: form-data; name=\"avatar\"; filename=\"a5.JPG\"\r\n\tContent-Type: image/jpeg\r\n\r\n"
  val rawHeaders3 = "Content-Disposition: form-data; name=\"avatar\"; filename=\"a5.JPG\"\r\nContent-Typ"


  val referenceMap = Map("Content-Type" -> "image/jpeg",
    "Content-Disposition" -> "form-data; name=\"avatar\"; filename=\"a5.JPG\"")

  val referenceMap3 = Map("Content-Disposition" -> "form-data; name=\"avatar\"; filename=\"a5.JPG\"")

  "getParsedHeaders" should "work the same" in {
    MultipartHeaderParser(rawHeaders1) should equal(referenceMap)
    MultipartHeaderParser(rawHeaders2) should equal(referenceMap)
    MultipartHeaderParser(rawHeaders3) should equal(referenceMap3)
  }



}
