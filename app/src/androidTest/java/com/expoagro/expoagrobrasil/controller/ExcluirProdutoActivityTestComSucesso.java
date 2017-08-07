package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.expoagro.expoagrobrasil.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExcluirProdutoActivityTestComSucesso {

    @Rule
    public ActivityTestRule<InicialArrobaActivity> mActivityTestRule = new ActivityTestRule<>(InicialArrobaActivity.class);

    @Test
    public void excluirProdutoActivityTestComSucesso() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction button = onView(
allOf(withId(R.id.buttonAnuncios), withText("Ver Anúncios"), isDisplayed()));
        button.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(6202);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatImageButton = onView(
allOf(withContentDescription("Open navigation drawer"),
withParent(withId(R.id.toolbar)),
isDisplayed()));
        appCompatImageButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatCheckedTextView = onView(
allOf(withId(R.id.design_menu_item_text), withText("Meus anúncios"), isDisplayed()));
        appCompatCheckedTextView.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3588282);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.campoEmail), isDisplayed()));
        appCompatEditText.perform(replaceText("dinego.nos@gmail.com"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.campoSenha), isDisplayed()));
        appCompatEditText2.perform(replaceText("12345"), closeSoftKeyboard());
        
        pressBack();
        
        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.campoSenha), withText("12345"), isDisplayed()));
        appCompatEditText3.perform(replaceText("123456"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.btnEntrar), withText("Entrar"),
withParent(withId(R.id.email_login_form)),
isDisplayed()));
        appCompatButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3537913);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatImageButton2 = onView(
allOf(withContentDescription("Open navigation drawer"),
withParent(withId(R.id.toolbar)),
isDisplayed()));
        appCompatImageButton2.perform(click());
        
        ViewInteraction appCompatCheckedTextView2 = onView(
allOf(withId(R.id.design_menu_item_text), withText("Meus anúncios"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3531610);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.recyclerview2),
withParent(allOf(withId(R.id.activity_meusanuncios),
withParent(withId(android.R.id.content)))),
isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction recyclerView2 = onView(
allOf(withId(R.id.recyclerview2),
withParent(allOf(withId(R.id.activity_meusanuncios),
withParent(withId(android.R.id.content)))),
isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(3600000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.excluirProduto), withText("Excluir"), isDisplayed()));
        appCompatButton2.perform(click());
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton3.perform(scrollTo(), click());
        
        }

    }
