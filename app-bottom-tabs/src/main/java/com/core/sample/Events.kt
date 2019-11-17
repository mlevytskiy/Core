package com.core.sample

import com.core.sample.viewmodel.HomeViewModel
import com.library.core.event.BaseEvent

class ShowPickOfAppsDialog(val selected: Int): BaseEvent(HomeViewModel::class)

class ShowPickedApps(val appPackages: String): BaseEvent(HomeViewModel::class)


