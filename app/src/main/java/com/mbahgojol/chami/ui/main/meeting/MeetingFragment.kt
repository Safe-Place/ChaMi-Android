package com.mbahgojol.chami.ui.main.meeting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mbahgojol.chami.R
import com.mbahgojol.chami.ui.main.challanges.TambahChallengeActivity

class MeetingFragment : Fragment() {
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    lateinit var fbAdd : FloatingActionButton
    lateinit var listMeeting : ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragmentView = requireNotNull(view) {"View should not be null when calling onActivityCreated"}

        dateTV = fragmentView.findViewById(R.id.tvDate)
        calendarView = fragmentView.findViewById(R.id.calendarView)
        fbAdd = fragmentView.findViewById(R.id.fb_add_jadwal)
        listMeeting = fragmentView.findViewById(R.id.listMeeting)

        listMeeting.setOnClickListener {
            val intent = Intent(requireContext(), DetailMeetingActivity::class.java)
            startActivity(intent)
        }
        fbAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddMeetingActivity::class.java)
            startActivity(intent)
        }

        // on below line we are adding set on
        // date change listener for calendar view.
        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the cariables in it.
                    val Date = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    // set this date in TextView for Display
                    dateTV.setText(Date)
                })
             }



}