package com.peasenet.settings

/**
 *
 * @author GT3CH1
 * @version 02-06-2025
 * @since 02-06-2025
 */
open class CallbackSetting<T : Setting>(
    var callback: ((T) -> Unit)? = null,
    settingOptions: SettingOptions = SettingOptions(),
) : Setting(settingOptions)
