package com.example.kevin.slugcentral;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class ScheduleActivity extends BasicActivity {

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Intent i = getIntent();
        int position = i.getIntExtra("position", -1);
        if (position != -1) {
            if (i.getStringExtra("Action").equals("Add")) {
                Log.d("PLZ", String.valueOf(position));
                events = addClass(position, events);
            } else {
                events = removeClass(position, events);
            }
        }
        return events;
    }

    public List<WeekViewEvent> addClass(int position, List<WeekViewEvent> events) {
        //course times are specified in this format 00:00
        //course meetingDays (e.g. Monday, Wednesday, Friday)

        String eventTitle = SearchActivity.allData.get(position).getName();
        String id = SearchActivity.allData.get(position).getId();
        String date;
        int startHour;
        int startMin;
        int endHour;
        int endMin;
        StringTokenizer dateTime = new StringTokenizer(SearchActivity.allData.get(position).getDaysTimes()," ");
        if(dateTime.countTokens() == 2)
        {
            date = dateTime.nextToken();
            if(date.equals("Cancelled"))
            {
                date = "Cancelled";
                startHour = -1;
                startMin = -1;
                endHour = -1;
                endMin = -1;
            }
            else{
                String time = dateTime.nextToken();
                Log.d("Adding Class: ", SearchActivity.allData.get(position).getName());
                StringTokenizer timeSplit = new StringTokenizer(time, "-");
                String sStartTime = timeSplit.nextToken();
                String sEndTime = timeSplit.nextToken();
                startHour = Integer.parseInt(sStartTime.substring(0,2));
                startMin = Integer.parseInt(sStartTime.substring(3,5));

                if(sStartTime.substring(5,7).equals("PM"));
                {
                    startHour += 12;
                }

                endHour = Integer.parseInt(sEndTime.substring(0,2));
                endMin = Integer.parseInt(sEndTime.substring(3,5));

                if(sEndTime.substring(5,7).equals("PM"));
                {
                    endHour += 12;
                }

            }

        }
        else{
            date = "TBA";
            startHour = -1;
            startMin = -1;
            endHour = -1;
            endMin = -1;

        }

        Log.d("PLZ PRINT", date);
        switch(date) {
            case "TuTh":
            case"TuThSa":
                events.add(makeEvent(10, id, eventTitle, startHour, startMin, endHour, endMin, 0));
                events.add(makeEvent(12, id, eventTitle, startHour, startMin, endHour, endMin, 0));
                break;
            case "W":
                events.add(makeEvent(11, id, eventTitle, startHour, startMin, endHour, endMin, 1));
                break;
            case "MW":
                events.add(makeEvent(9, id, eventTitle, startHour, startMin, endHour, endMin, 2));
                events.add(makeEvent(11, id, eventTitle, startHour, startMin, endHour, endMin, 2));
                break;
            case "MWF":
                events.add(makeEvent(9, id, eventTitle, startHour, startMin, endHour, endMin, 3));
                events.add(makeEvent(11, id, eventTitle, startHour, startMin, endHour, endMin, 3));
                events.add(makeEvent(13, id, eventTitle, startHour, startMin, endHour, endMin, 3));
                break;
            default:
                break;
        }

        return events;
    }

    public List<WeekViewEvent> removeClass(int position, List<WeekViewEvent> events) {
        String id = SearchActivity.allData.get(position).getId();
        for(int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == Long.valueOf(id))
                events.remove(i);
        }
        return events;
    }

    public WeekViewEvent makeEvent(int date, String id, String eventTitle, int startHour, int startMin, int endHour, int endMin, int c) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(2018, 6, date);
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMin);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR, endHour);
        endTime.set(Calendar.MINUTE, endMin);

        int[] colors = new int[]{R.color.event_color_01, R.color.event_color_02, R.color.event_color_03, R.color.event_color_04};

        WeekViewEvent event = new WeekViewEvent(Long.valueOf(id), eventTitle, startTime, endTime);
        event.setColor(getResources().getColor(colors[c]));
        Log.d("PLZ PRINT", startTime.toString() + " " + endTime.toString());
        return event;
    }

//        Calendar startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.HOUR_OF_DAY, 8);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.HOUR_OF_DAY, 11);
//        startTime.set(Calendar.MINUTE, 40);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 12);
//        endTime.set(Calendar.MINUTE, 40);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.HOUR_OF_DAY, 13);
//        startTime.set(Calendar.MINUTE, 20);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 14);
//        endTime.set(Calendar.MINUTE, 0);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.HOUR_OF_DAY, 14);
//        startTime.set(Calendar.MINUTE, 40);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 1);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.event_color_04));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.HOUR_OF_DAY, 10);
//        startTime.set(Calendar.MINUTE, 45);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        startTime.add(Calendar.DATE, 1);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 1);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.colorAccent));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.DAY_OF_MONTH, 15);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.colorAccent));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.DAY_OF_MONTH, 1);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.colorAccent));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(2018,6, 9);
//        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//        startTime.set(Calendar.HOUR_OF_DAY, 15);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
//        event.setColor(getResources().getColor(R.color.colorAccent));
//        events.add(event);
//
//        return events;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                Intent i = new Intent(ScheduleActivity.this, SearchActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}