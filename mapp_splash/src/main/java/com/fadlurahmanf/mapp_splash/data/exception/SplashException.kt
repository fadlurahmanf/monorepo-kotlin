package com.fadlurahmanf.mapp_splash.data.exception

import java.lang.Exception

class SplashException(
    override val message: String?
) : Exception(message) {

}
