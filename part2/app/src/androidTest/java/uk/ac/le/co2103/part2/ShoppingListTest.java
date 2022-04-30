package uk.ac.le.co2103.part2;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.View;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ShoppingListTest {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddNewList() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextShoppingListName)).perform(replaceText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.buttonCreateList)).perform(click());

        onView(withText("Birthday Party")).check(matches(isDisplayed()));
        //Must delete shopping list with name "Birthday Party" to run the testDeleteList()
    }


    @Test
    public void testDeleteList() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextShoppingListName)).perform(replaceText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.buttonCreateList)).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));
        onView(withText("Birthday Party")).perform(longClick());
        delay();
        onView(withId(R.id.btn_main_yes)).perform(click());
        delay();
        onView(withText("Birthday Party")).check(doesNotExist());
    }

    @Test
    public void testAddProduct() {
        testAddNewList();
        onView(withText("Birthday Party")).perform(click());
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(replaceText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(replaceText("1"), closeSoftKeyboard());
        onView(withId(R.id.buttonAddProduct)).perform(click());
        onView(withText("Cake")).check(matches(isDisplayed()));

    }

    @Test
    public void testAddDuplicateProduct() {
        onView(withText("Birthday Party")).perform(click());
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(replaceText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(replaceText("1"), closeSoftKeyboard());
        onView(withId(R.id.buttonAddProduct)).perform(click());
        onView(withText("Product already exists.")).inRoot(new ToastMatcher()).check(matches(withText("Product already exists.")));

    }

    @Test
    public void testEditProduct() {
        testAddNewList();
        onView(withText("Birthday Party")).perform(click());
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(replaceText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(replaceText("1"), closeSoftKeyboard());
        onView(withId(R.id.buttonAddProduct)).perform(click());
        onView(withText("Cake")).perform(click());
        onView(withId(R.id.popupUpdateProduct)).perform(click());
        //onView(withId(R.id.imageViewIncrement)).perform(click());
        delay();
        //onView(withId(R.id.imageViewIncrement)).perform(click());
        onView(withId(R.id.buttonUpdateProduct)).perform(click());
        onView(withText("Cake")).check(matches(isDisplayed()));


    }

    @Test
    public void testDeleteProduct() {
        fail("Not implemented yet.");
    }

    public void delay() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}