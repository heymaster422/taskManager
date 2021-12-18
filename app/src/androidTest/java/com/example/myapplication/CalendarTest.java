package com.example.myapplication;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class CalendarTest {

    @Rule
    public ActivityTestRule<Calendar> calendarActivity = new ActivityTestRule<>(Calendar.class);

    private Calendar calendar = null;

    Instrumentation.ActivityMonitor createEventmonitor = getInstrumentation().addMonitor(CreateAnEvent.class.getName(), null, false);
    Instrumentation.ActivityMonitor notesMonitor = getInstrumentation().addMonitor(Notes.class.getName(), null, false);
    Instrumentation.ActivityMonitor tasksMonitor = getInstrumentation().addMonitor(Tasks.class.getName(), null, false);
    Instrumentation.ActivityMonitor remindersMonitor = getInstrumentation().addMonitor(Reminders.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(Settings.class.getName(), null, false);
    Instrumentation.ActivityMonitor calendarMonitor = getInstrumentation().addMonitor(Calendar.class.getName(), null, false);
    Instrumentation.ActivityMonitor createReminderMonitor = getInstrumentation().addMonitor(CreateReminder.class.getName(), null, false);

    @Before
    public void setUp() {
        calendar = calendarActivity.getActivity();
    }

    //test to check if create an event activity launches upon clicking Create an Event Button
    @Test
    public void testLaunchOfCreateEventFromCalendar() {
        onView(withId(R.id.save)).perform(click());

        Activity createAnEvent = getInstrumentation().waitForMonitorWithTimeout(createEventmonitor, 5000);
        assertNotNull(createAnEvent);

    }

    //test to check if clicking on notes option takes us to notes feature
    @Test
    public void testLaunchOfNotesFromCalendar() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());

        Activity notes = getInstrumentation().waitForMonitorWithTimeout(notesMonitor, 5000);
        assertNotNull(notes);

    }

    //test to check if clicking on notes option takes us to notes feature
    @Test
    public void testLaunchOfTasksFromCalendar() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());

        Activity tasks = getInstrumentation().waitForMonitorWithTimeout(tasksMonitor, 5000);
        assertNotNull(tasks);

    }

    //test to check if clicking on notes option takes us to notes feature
    @Test
    public void testLaunchOfRemindersFromCalendar() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());

        Activity reminders = getInstrumentation().waitForMonitorWithTimeout(remindersMonitor, 5000);
        assertNotNull(reminders);

    }

    //test to check if clicking on notes option takes us to notes feature
    @Test
    public void testLaunchOfSettingsFromCalendar() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());

        Activity settings = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settings);

    }

    @Test
    public void testLaunchOfCalendarFromNotes() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.calendar)).perform(click());

        Activity calendarActivity = getInstrumentation().waitForMonitorWithTimeout(calendarMonitor, 5000);
        assertNotNull(calendarActivity);

    }

    @Test
    public void testLaunchOfRemindersFromNotes() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());

        Activity reminders = getInstrumentation().waitForMonitorWithTimeout(remindersMonitor, 5000);
        assertNotNull(reminders);

    }

    @Test
    public void testLaunchOfTasksFromNotes() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());

        Activity tasks = getInstrumentation().waitForMonitorWithTimeout(tasksMonitor, 5000);
        assertNotNull(tasks);

    }

    @Test
    public void testLaunchOfSettingsFromNotes() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());

        Activity settings = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settings);

    }

    @Test
    public void testLaunchOfCalendarFromTasks() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.calendar)).perform(click());

        Activity calendarActivity = getInstrumentation().waitForMonitorWithTimeout(calendarMonitor, 5000);
        assertNotNull(calendarActivity);

    }

    @Test
    public void testLaunchOfRemindersFromTasks() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());

        Activity reminders = getInstrumentation().waitForMonitorWithTimeout(remindersMonitor, 5000);
        assertNotNull(reminders);

    }

    @Test
    public void testLaunchOfNotesFromTasks() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());

        Activity notes = getInstrumentation().waitForMonitorWithTimeout(notesMonitor, 5000);
        assertNotNull(notes);

    }

    @Test
    public void testLaunchOfSettingsFromTasks() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());

        Activity settings = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settings);

    }

    @Test
    public void testLaunchOfCalendarFromReminders() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.calendar)).perform(click());

        Activity calendarActivity = getInstrumentation().waitForMonitorWithTimeout(calendarMonitor, 5000);
        assertNotNull(calendarActivity);

    }

    @Test
    public void testLaunchOfCreateReminderFromReminders() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());
        onView(withId(R.id.buttonsave)).perform(click());

        Activity createReminder = getInstrumentation().waitForMonitorWithTimeout(createReminderMonitor, 5000);
        assertNotNull(createReminder);

    }

    @Test
    public void testLaunchOfTasksFromReminders() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());

        Activity tasks = getInstrumentation().waitForMonitorWithTimeout(tasksMonitor, 5000);
        assertNotNull(tasks);

    }

    @Test
    public void testLaunchOfNotesFromReminders() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());

        Activity notes = getInstrumentation().waitForMonitorWithTimeout(notesMonitor, 5000);
        assertNotNull(notes);

    }

    @Test
    public void testLaunchOfSettingsFromReminders() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());

        Activity settings = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settings);

    }

    @Test
    public void testLaunchOfCalendarFromSettings() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.calendar)).perform(click());

        Activity calendarActivity = getInstrumentation().waitForMonitorWithTimeout(calendarMonitor, 5000);
        assertNotNull(calendarActivity);

    }

    @Test
    public void testLaunchOfRemindersFromSettings() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.remindersButton)).perform(click());

        Activity reminders = getInstrumentation().waitForMonitorWithTimeout(remindersMonitor, 5000);
        assertNotNull(reminders);

    }

    @Test
    public void testLaunchOfNotesFromSettings() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.notes)).perform(click());

        Activity notes = getInstrumentation().waitForMonitorWithTimeout(notesMonitor, 5000);
        assertNotNull(notes);

    }

    @Test
    public void testLaunchOfTasksFromSettings() {
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.tasksButton)).perform(click());

        Activity tasks = getInstrumentation().waitForMonitorWithTimeout(tasksMonitor, 5000);
        assertNotNull(tasks);

    }


    @After
    public void tearDown() {
        calendar = null;
    }
}