package com.example.assignment1
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.util.Locale

fun getZone(context: Context,value: Int, zoneText: TextView, pointText: TextView){
    when (value) {
        0 -> {
            zoneText.setTextColor(ContextCompat.getColor(context,R.color.darkGrey))
            pointText.setTextColor(ContextCompat.getColor(context,R.color.darkGrey))
        }
        in 1..3 -> {
            zoneText.setTextColor(ContextCompat.getColor(context,R.color.blue))
            pointText.setTextColor(ContextCompat.getColor(context,R.color.blue))
        }
        in 4..8 -> {
            zoneText.setTextColor(ContextCompat.getColor(context,R.color.darkGreen))
            pointText.setTextColor(ContextCompat.getColor(context,R.color.darkGreen))
        }
        in 9..18 ->{
            zoneText.setTextColor(ContextCompat.getColor(context,R.color.darkRed))
            pointText.setTextColor(ContextCompat.getColor(context,R.color.darkRed))
        }
    }
}



fun getColour(value: Int, vnState : Boolean, engState: Boolean) : String {
    var colorZone: String = ""
    if(engState || (!engState && !vnState)){
        colorZone = when (value) {
            0 -> "Ground"
            in 1..3 -> "Blue"
            in 4..8 -> "Green"
            in 9..18 -> "Red"
            else -> "Ground"
        }
    }else if(vnState){
        colorZone = when (value) {
            0 -> "Đất"
            in 1..3 -> "Xanh biển"
            in 4..8 -> "Xanh lá cây"
            in 9..18 -> "Đỏ"
            else -> "Đất"
        }
    }
    return colorZone
}
                    // SETTING LANGUAGE (Changing Locale) //
fun setLocale(context: Context,languageName: String, updateFunction : () -> Unit){
    var locale = Locale(languageName)
    var loSource = context.resources
    var config = loSource.configuration
    val metric = loSource.displayMetrics
    config.setLocale(locale)
    loSource.updateConfiguration(config,metric)
    Log.i("Language: ", languageName)
    updateFunction()
}

