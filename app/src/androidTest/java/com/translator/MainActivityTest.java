package com.translator;

import android.support.design.internal.BottomNavigationItemView;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.translator.navigation.MainActivity;
import com.translator.system.Language;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by fedorova on 10.04.2017.
 */

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /*@Test
    public void validateEditText() {


        onView(withId(R.id.input_text)).perform(typeText("Hello"));
        onView(allOf(withId(R.id.clear_button),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        //onView(withId(R.id.input_text)).check(matches(withText("")));

        onView(withId(R.id.input_lang)).perform(click());

        //onView(withId(R.id.result_text)).check(withEffectiveVisibility(ViewMatchers.Visibility.GONE));
    }*/

   /* @Test
    public void validateInputLangChooser() {
        onView(withId(R.id.input_lang)).perform(click());
        onData(withItemValue("Азербайджанский")).inAdapterView(withId(R.id.languages_list_view)).perform(click());
        // onData(anything()).inAdapterView(withId(R.id.languages_list_view)).atPosition(0).perform(click());
        pressBack();
        onView(withId(R.id.input_lang)).check(matches(withText("Азербайджанский")));
    }

    public static Matcher<Object> withItemValue(final String value) {
        return new BoundedMatcher<Object, Language>(Language.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has value " + value);
            }

            @Override
            public boolean matchesSafely(Language item) {
                return item.getName().toUpperCase().equals(String.valueOf(value));
            }
        };
    }


    private String getString(int resId){
        return InstrumentationRegistry.getTargetContext().getString(resId);
    }

    public static Matcher<View> withBottomNavItemCheckedStatus(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            boolean triedMatching;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: " + String.valueOf(isChecked));
                    description.appendText("But was: " + String.valueOf(!isChecked));
                }
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                triedMatching = true;
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }*/

}
