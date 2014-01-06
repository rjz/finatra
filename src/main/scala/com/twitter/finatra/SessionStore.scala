package com.twitter.finatra

import com.twitter.storehaus.Store
import collection.JavaConverters._
import com.twitter.util.Future

//Represents the global session store available to the framework
object SessionStore {

  val map = scala.collection.mutable.Map[String, Option[Map[String, String]]]()
  val store = Store.fromJMap(map.asJava)

  def get(k: String): Future[Option[Map[String, String]]] = store.get(k)
  def put(kv: (String, Option[Map[String, String]])): Future[Unit] = store.put(kv)

}
