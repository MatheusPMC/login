package com.login.core.commons.handler

import java.lang.RuntimeException

class MongoException(msg: String) : RuntimeException(msg)
class KeycloakException(msg: String) : RuntimeException(msg)
class AwsConnectionException(msg: String) : RuntimeException(msg)