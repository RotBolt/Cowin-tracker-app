package io.rotlabs.cowintracker.data

import android.content.Context

class CenterNotifiedPrefs(context: Context) {

    private val sharedPrefs = context.getSharedPreferences("Centers", Context.MODE_PRIVATE)

    private val CENTER_KEY = "centre_key"

    // List of "Centre_Name:District_name:timestamp"
    fun getListCentresTimeStampedList(): List<String> {
        val string = sharedPrefs.getString(CENTER_KEY, "")
        return string?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    // List of "Centre_Name:District_name"
    fun appendList(list: List<String>) {
        val currentTime = System.currentTimeMillis()
        val timeStampedList = list.filter { it.isNotEmpty() }.map {
            "$it:$currentTime"
        }
        val oldList = mutableListOf<String>()
        getListCentresTimeStampedList().forEach { old ->
            list.forEach { new ->
                if (!old.contains(new)) {
                    oldList.add(old)
                }
            }
        }

        val listSting = (oldList + timeStampedList).joinToString(",")
        sharedPrefs.edit().putString(CENTER_KEY, listSting).apply()
    }

}