package com.dashBoardUniversary.commons.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface TestLogging

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

inline fun <reified T : TestLogging> T.logger(): Logger = getLogger(T::class.java)