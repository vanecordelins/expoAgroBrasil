package com.expoagro.expoagrobrasil.view;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.controller.LoginActivity;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTestDelete {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void usuarioActivityTestDelete() throws Exception {

        Thread.sleep(3000);

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.campoEmail), isDisplayed()));
        appCompatEditText2.perform(replaceText("samirjosue_13@hotmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(allOf(withId(R.id.campoSenha), isDisplayed()));
        appCompatEditText4.perform(replaceText("123456"), closeSoftKeyboard());

        Thread.sleep(3000);

        onView(withId(R.id.btnEntrar)).perform(click());

        Thread.sleep(3000);
        // -----------------------------------------------------------
      //  ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.btn_delete), withText("DELETAR"), isDisplayed()));
      //  appCompatButton3.perform(click());
// -----------------------------------------------------------
        Thread.sleep(3000);

        ViewInteraction appCompatButton4 = onView(allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton4.perform(scrollTo(), click());

        Thread.sleep(3000);

        ViewInteraction imageView = onView(allOf(withId(R.id.logo),isDisplayed()));
        ViewInteraction result = imageView.check(matches(isDisplayed()));

        Assert.assertNotNull(result);
    }

}