package com.expoagro.expoagrobrasil.view;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.controller.LoginActivity;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Samir on 11/07/2017.
 */

public class AlterarUsuarioActivityTestNomeValido {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void anunciosActivityTest() throws Exception {

        onView(withId(R.id.campoEmail)).perform(typeText("samirjosue_13@hotmail.com"));
        closeKeyboard();
        onView(withId(R.id.campoSenha)).perform(typeText("123456"));
        closeKeyboard();
        onView(withId(R.id.btnEntrar)).perform(click());

        Thread.sleep(3000);

        ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.alterar), withText("ALTERAR"), isDisplayed()));
        appCompatButton3.perform(click());

        Thread.sleep(3000);

        ViewInteraction appCompatAutoCompleteTextView = onView(allOf(withId(R.id.campoNome), isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("Samir Teste"), closeSoftKeyboard());


        ViewInteraction appCompatButton4 = onView(allOf(withId(R.id.btn_confirm), withText("ATUALIZAR"), isDisplayed()));
        appCompatButton4.perform(click());

        Thread.sleep(3000);

        ViewInteraction textView = onView(allOf(withText("ExpoAgro Brasil"),isDisplayed()));
        ViewInteraction result = textView.check(matches(withText("ExpoAgro Brasil")));

        Thread.sleep(3000);

        ViewInteraction appCompatButton5 = onView(allOf(withId(R.id.btn_sair), withText("SAIR"),isDisplayed()));
        appCompatButton5.perform(click());

        Assert.assertNotNull(result);

    }

    public void closeKeyboard() throws Exception {
        Espresso.closeSoftKeyboard();
        Thread.sleep(1000);
    }
}
