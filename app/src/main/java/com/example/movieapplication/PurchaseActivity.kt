package com.example.movieapplication

import android.animation.*
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.movieapplication.*

const val TICKET_BASE_PRICE = 39
const val EXTRA_LEGROOM_PRICE = 12
const val FULL_PACKAGE_PRICE = 65

class PurchaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val refreshButton = findViewById<ImageButton>(R.id.pRefreshButton)
        refreshButton.setOnClickListener{
            recreate()
        }

        //Ticket number Spinner
        val purchaseSpinner = findViewById<Spinner>(R.id.pTicketSpinner)
        val numOfTicketList : MutableList<String> = ArrayList()
        var numOfTickets = 1

        numOfTicketList.add("1 Ticket")
        for (i in 2..12)
            numOfTicketList.add("$i Tickets")

        val adapter = ArrayAdapter(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, numOfTicketList)
        purchaseSpinner.adapter = adapter

        purchaseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                val ticketCountStr = numOfTicketList[position]
                numOfTickets = position + 1
                Toast.makeText(this@PurchaseActivity,
                    "$ticketCountStr selected", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        //Calender
        val myCalender = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalender)
        }
        
        //Button click to show DatePickerDialog
        val pPickDateButton = findViewById<Button>(R.id.pPickDateButton)
        pPickDateButton.setOnClickListener{
            DatePickerDialog(this, datePickerDialog,
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        //Full package info
        val pFullPackageInfoButton = findViewById<ImageButton>(R.id.pFullPackageInfoButton)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        pFullPackageInfoButton.setOnClickListener{
            builder.apply {
                setTitle(R.string.p_full_package_check_box)
                setMessage(R.string.p_full_package_info)
                setIcon(R.drawable.baseline_movie_filter_24)
            }
            val dialog =builder.create()
            dialog.show()
        }

        //Purchase button
        val pPurchaseButton = findViewById<Button>(R.id.pPurchaseButton)
        val pForestGumpFace = findViewById<ImageView>(R.id.pForestGumpFace)
        val pPurchaseTextView = findViewById<TextView>(R.id.pPurchaseTextView)

        pPurchaseButton.setOnClickListener {
            pPurchaseTextView.text = resources.getString(R.string.p_purchase_text_view,
                numOfTickets,
                getExtraLegroomBoolean().second,
                getFullPackageBoolean().second,
                calculateTotalPrice(numOfTickets,
                    getFullPackageBoolean().first,
                    getExtraLegroomBoolean().first))
            pForestGumpFace.animate().apply {
                scaleX(0.6f)
                scaleY(0.6f)
                duration = 1000
                rotationYBy(360f)
            }.withEndAction{
                pForestGumpFace.animate().apply {
                    scaleX(1f)
                    scaleY(1f)
                    pForestGumpFace.alpha = 0.5f
                    duration = 500
                    pPurchaseTextView.visibility = View.VISIBLE
                }
            }
        }
    }



    //  Back button overridden with a dialog shown
    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val goToMainActivity = Intent(this, MainActivity::class.java)
        builder.apply {
            setTitle(R.string.p_back_button_dialog_title)
            setMessage(R.string.p_back_button_dialog_message)
            setCancelable(false)
            setIcon(R.drawable.baseline_exit_to_app_24)

            setPositiveButton(R.string.p_back_button_dialog_positive) { _, _ ->
                startActivity(goToMainActivity)
            }
//
            setNegativeButton(R.string.p_back_button_dialog_negative){ _, _ ->
                //do nothing
            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    //Calculate total price
    private fun calculateTotalPrice(numOfTickets : Int,
                            pExtraLegroomBoolean : Boolean,
                            pFullPackageBoolean : Boolean): Int{
        var ticketPrice = TICKET_BASE_PRICE
        if (pFullPackageBoolean) ticketPrice = FULL_PACKAGE_PRICE
        if (pExtraLegroomBoolean) ticketPrice += EXTRA_LEGROOM_PRICE
        return ticketPrice * numOfTickets
    }

    //get extra legroom checkbox boolean
    private fun getExtraLegroomBoolean(): Pair<Boolean, String>{
        val pExtraLegroomBoolean = findViewById<CheckBox>(R.id.pExtraLegroomCheckBox).isChecked
        val pExtraLegroomBooleanStr = if (pExtraLegroomBoolean) getString(R.string.p_extra_legroom_positive)
        else getString(R.string.p_extra_legroom_negative)
        return Pair(pExtraLegroomBoolean, pExtraLegroomBooleanStr)
    }


    //get full package checkbox boolean
    private fun getFullPackageBoolean(): Pair<Boolean, String>{
        val pFullPackageBoolean = findViewById<CheckBox>(R.id.pFullPackageCheckBox).isChecked
        val pFullPackageBooleanStr = if (pFullPackageBoolean) getString(R.string.p_full_package_positive)
        else getString(R.string.p_full_package_negative)
        return Pair(pFullPackageBoolean, pFullPackageBooleanStr)
    }


    // Update chosen date label function
    private fun updateLabel(myCalender: Calendar) {
        val pDateChosen = findViewById<TextView>(R.id.pDateChosen)
        val myFormat = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.UK)
        pDateChosen.text = simpleDateFormat.format(myCalender.time)
    }
}