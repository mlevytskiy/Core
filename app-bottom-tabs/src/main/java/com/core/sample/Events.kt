package com.core.sample

import com.core.sample.util.HomeTitle
import com.core.sample.viewmodel.HomeViewModel
import com.library.core.event.BaseEvent

class ShowPickOfAppsDialog(val type: HomeTitle.Type): BaseEvent()

class ShowPickedApps(val appPackages: String, val likes: Map<String, List<Int>>): BaseEvent()

class ShowCountriesDialog: BaseEvent()

class ShowDetectedPhoneNumberEvent : BaseEvent()

class ShowNextButtonInProgressState: BaseEvent()

class ShowEnterCodeState: BaseEvent()

class ShowSuccess: BaseEvent()

class MoveToHomeScreen : BaseEvent()




