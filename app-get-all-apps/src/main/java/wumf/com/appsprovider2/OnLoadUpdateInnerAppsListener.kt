package wumf.com.appsprovider2

import wumf.com.appsprovider.App

const val DEFAULT_STEP = 6

abstract class OnLoadUpdateInnerAppsListener(val step: Int = DEFAULT_STEP) {

    abstract fun onUpdate(apps: List<App>)

    abstract fun onFinish()

}