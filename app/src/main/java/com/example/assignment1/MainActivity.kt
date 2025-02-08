package com.example.assignment1
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    // Variable Declaration
    private val maxHoldState = 9
    private val maxPoint = 18
    private var currentValue = 0
    private var holdState = 0
    private var colorZone: String = "Ground"
    private lateinit var result: TextView
    private lateinit var zoneText: TextView
    private lateinit var displayHoldState: TextView
    private var vnState:Boolean = false
    private var engState:Boolean = false

    // One-Time Click
    private var vietnamToast:Boolean = false
    private var englishToast:Boolean = false
    private var resetButtonClicked: Boolean = true

    // Check has fallen ?
    private var hasFallen:Boolean = false

    // Button interaction
    private var climbAlphaSet:Float = 1f
    private var fallAlphaSet:Float = 1f



    // Update color back ground match with zone
    private fun getBackGroundColorZone(tempValue: Int) {
        val root = findViewById<ViewGroup>(
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                R.id.landscapeTemp
            else
                R.id.portraitTemp
        )

        val backgroundResource = when(tempValue) {
            0 -> R.drawable.gradient_ground
            in 1..3 -> R.drawable.gradient_blue
            in 4..8 -> R.drawable.gradient_green
            else -> R.drawable.gradient_red
        }
        root.setBackgroundResource(backgroundResource)
    }
    // Change the button alpha
    private fun setAlpha(climbAlpha:Button, fallAlpha:Button){
        climbAlpha.alpha = climbAlphaSet
        fallAlpha.alpha = fallAlphaSet
    }

    private fun updateZoneDisplay() {
        val currentPointDisplay:String = getString(R.string.score)
        getZone(this,currentValue,zoneText)
        colorZone = getColour(currentValue,vnState,engState)
        if(engState){
            zoneText.text = "$colorZone Zone"
            displayHoldState.text = "Current hold: $holdState"
        }else{
            zoneText.text = "Bạn đang ở vùng $colorZone"
            displayHoldState.text = "Bạn đang ở bậc $holdState"
        }
        result.text = "$currentPointDisplay: $currentValue"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("CurrentValue", currentValue)
        outState.putInt("HoldState", holdState)
        outState.putBoolean("vn",vnState)
        outState.putBoolean("en",engState)
        outState.putBoolean("hasFallen",hasFallen)
        outState.putFloat("climbAlpha",climbAlphaSet)
        outState.putFloat("fallAlpha",fallAlphaSet)
        outState.putBoolean("resetButtonClicked",resetButtonClicked)
        super.onSaveInstanceState(outState)
    }
                        //// Start ////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_portraits)
        // check the saved data
        Log.i("SaveInstance:", savedInstanceState.toString())

        // setOnClick declaration
        result = findViewById(R.id.resultText)
        displayHoldState = findViewById(R.id.holdState)
        zoneText = findViewById(R.id.Zone)

        val climbFunction = findViewById<Button>(R.id.climbButton)
        val resetFunction = findViewById<Button>(R.id.resetButton)
        val fallFunction = findViewById<Button>(R.id.fallButton)
        val vnButton = findViewById<RadioButton>(R.id.vn)
        val engButton = findViewById<RadioButton>(R.id.eng)



        fun updateString(){
            climbFunction.text = getText(R.string.climb)
            resetFunction.text = getText(R.string.reset)
            fallFunction.text = getText(R.string.fall)
        }


       if (savedInstanceState != null) {
            currentValue = savedInstanceState.getInt("CurrentValue", 0)
            holdState = savedInstanceState.getInt("HoldState", 0)
            vnState = savedInstanceState.getBoolean("vn", false)
            engState = savedInstanceState.getBoolean("en", false)
           hasFallen = savedInstanceState.getBoolean("hasFallen",false)
           climbAlphaSet = savedInstanceState.getFloat("climbAlpha",0f)
           fallAlphaSet = savedInstanceState.getFloat("fallAlpha",0f)
           resetButtonClicked = savedInstanceState.getBoolean("resetButtonClicked",false)
           val currentLocale:String = if(engState) "en" else "vi"
           setLocale(this,currentLocale, ::updateString)
           getBackGroundColorZone(currentValue)
           updateZoneDisplay()
           setAlpha(climbFunction,fallFunction)
        }
                            // Languages function ///
        vnButton.setOnClickListener {
                vnState = true
                engState = false
                setLocale(this,"vi",::updateString)
            if(!vietnamToast){
                    Toast.makeText(this,"Language: Vietnamese",Toast.LENGTH_SHORT).show()
                    vietnamToast = true
                    englishToast = false
                }
            updateZoneDisplay()
        }
        engButton.setOnClickListener {
                vnState = false
                engState = true
                setLocale(this,"en",::updateString)
            if(!englishToast){
                Toast.makeText(this,"Language: English",Toast.LENGTH_SHORT).show()
                vietnamToast = false
                englishToast = true
            }
                updateZoneDisplay()
        }


                    //  CLIMB FUNCTION //
        climbFunction.setOnClickListener {
            /// Value have changed ///
            val toastTextReachTop = getString(R.string.toastReachTop)
            resetButtonClicked = false
            if (hasFallen && currentValue < 18) {
                val toastTextHaveFallen = getString(R.string.toastHaveFallen)
                Toast.makeText(this,toastTextHaveFallen,Toast.LENGTH_SHORT).show()
            } else{
                when (currentValue) {
                    in 0..3 -> currentValue++
                    in 4..8 -> currentValue += 2
                    in 9..18 -> currentValue += 3
                }
                getBackGroundColorZone(currentValue)
                holdState++
                // Prevent passing max value
                if (currentValue >= maxPoint) {
                    holdState = maxHoldState
                    currentValue = maxPoint
                    Toast.makeText(this,toastTextReachTop,Toast.LENGTH_LONG).show()
                    climbAlphaSet = 0.5f
                    fallAlphaSet = 0.5f
                    setAlpha(climbFunction,fallFunction)
                }
                updateZoneDisplay()
                Log.d("Current Value: ", currentValue.toString())
                Log.d("Button Function: ", "Climb Button Clicked")
                Log.d("Color Zone: ", "Current color zone: " + getColour(currentValue, vnState, engState)
                )
            }
        }
                //  RESET FUNCTION //
        resetFunction.setOnClickListener {
            if(!resetButtonClicked){
                currentValue = 0
                holdState = 0
                updateZoneDisplay()
                Toast.makeText(this, getString(R.string.toastResetPoint), Toast.LENGTH_SHORT).show()
                resetButtonClicked = true
                hasFallen = false
                climbAlphaSet = 1f
                fallAlphaSet = 1f
                getBackGroundColorZone(currentValue)
            }
            Log.d("Button Function: ", "Reset Button Clicked")
            Log.d("Color Zone: ", "Current color zone: " + getColour(currentValue,vnState,engState))
            setAlpha(climbFunction,fallFunction)
        }

        fallFunction.setOnClickListener {
            val toastTextHaveLanded = getString(R.string.toastHaveLanded)
            // Check if reached top or has fallen?
            val toastTextReachTop = getString(R.string.toastNeedReset)
            if(!hasFallen && currentValue != 0) {
                when (currentValue) {
                    in 1..3 -> {
                        currentValue = 0
                        Toast.makeText(this, toastTextHaveLanded, Toast.LENGTH_SHORT).show()
                    }
                    in 4..17 -> {
                        currentValue -= 3
                    }
                    18 -> {
                        Toast.makeText(this, toastTextReachTop, Toast.LENGTH_SHORT).show()
                    }
                }
                hasFallen = true
            // adjust the color to make user reset after fell
                climbAlphaSet = 0.5f
                fallAlphaSet = 0.5f
                setAlpha(climbFunction,fallFunction)
            }else if(!resetButtonClicked){
                Toast.makeText(this, toastTextReachTop, Toast.LENGTH_SHORT).show()
            }
            getBackGroundColorZone(currentValue)
            updateZoneDisplay()
            Log.d("Current Value: ",currentValue.toString())
            Log.d("Button Function: ", "Fall Button Clicked")
            Log.d("Color Zone: ", "Current color zone: " + getColour(currentValue,vnState,engState))
        }
    }

}
