package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.expoagro.expoagrobrasil.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InicialArrobaActivityTest {

    @Rule
    public ActivityTestRule<InicialArrobaActivity> mActivityTestRule = new ActivityTestRule<>(InicialArrobaActivity.class);

    @Test
    public void inicialArrobaActivityTest() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button = onView(allOf(withId(R.id.buttonAnuncios), withText("Ver Anúncios"), isDisplayed()));
        button.perform(click());

        try {
            Thread.sleep(5339);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)), isDisplayed()));
        appCompatImageButton.perform(click());

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatCheckedTextView = onView(allOf(withId(R.id.design_menu_item_text), withText("Meus anúncios"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        try {
            Thread.sleep(3582178);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.campoEmail), isDisplayed()));
        appCompatEditText.perform(replaceText("diego.nos@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.campoEmail), withText("diego.nos@gmail.com"), isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(allOf(withId(R.id.campoEmail), withText("diego.nos@gmail.com"), isDisplayed()));
        appCompatEditText3.perform(replaceText("dinego.nos@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(allOf(withId(R.id.campoSenha), isDisplayed()));
        appCompatEditText4.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btnEntrar), withText("Entrar"),
                        withParent(withId(R.id.email_login_form)), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.btnEntrar), withText("Entrar"),
                        withParent(withId(R.id.email_login_form)), isDisplayed()));
        appCompatButton2.perform(click());

        try {
            Thread.sleep(3528798);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(allOf(withId(R.id.design_menu_item_text), withText("Meus anúncios"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        inicialArrobaActivityCont();


    }

    private void inicialArrobaActivityCont() {

        try {
            Thread.sleep(3520540);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatRadioButton = onView(allOf(withId(R.id.rdoBtnServico3), withText("Serviços"),
                withParent(allOf(withId(R.id.activity_meusanuncios), withParent(withId(android.R.id.content)))), isDisplayed()));
        appCompatRadioButton.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatRadioButton2 = onView(allOf(withId(R.id.rdoBtnServico3), withText("Serviços"),
                withParent(allOf(withId(R.id.activity_meusanuncios), withParent(withId(android.R.id.content)))), isDisplayed()));
        appCompatRadioButton2.perform(click());

        try {
            Thread.sleep(3515939);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatRadioButton3 = onView(allOf(withId(R.id.rdoBtnProduto3), withText("Produtos"),
                withParent(allOf(withId(R.id.activity_meusanuncios), withParent(withId(android.R.id.content)))), isDisplayed()));
        appCompatRadioButton3.perform(click());

        try {
            Thread.sleep(3511614);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerview2), withParent(allOf(withId(R.id.activity_meusanuncios),
                withParent(withId(android.R.id.content)))), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        try {
            Thread.sleep(3507408);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.alterarProduto), withText("Alterar"), isDisplayed()));
        appCompatButton3.perform(click());

        try {
            Thread.sleep(3504236);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatAutoCompleteTextView = onView(allOf(withId(R.id.campoNomeProduto), withText("Nov")));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("Novoo"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(allOf(withId(R.id.campoNomeProduto), withText("Novoo")));
        appCompatAutoCompleteTextView2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton4 = onView(allOf(withId(R.id.btnAlterar), withText("Confirmar")));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton5.perform(scrollTo(), click());
    }

}
