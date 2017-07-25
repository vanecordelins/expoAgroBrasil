package com.expoagro.expoagrobrasil.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.expoagro.expoagrobrasil.R;
import com.google.firebase.auth.FirebaseAuth;

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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class CadastroServicoActivityTestNomeVazio {

    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule = new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void cadastroServicoActivityTest() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){

            ViewInteraction appCompatImageButton2 = onView(
                    allOf(withContentDescription("Open navigation drawer"),
                            withParent(withId(R.id.toolbar)),
                            isDisplayed()));
            appCompatImageButton2.perform(click());

            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.menu_email), withText("Fazer Login"), isDisplayed()));
            appCompatTextView.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.campoEmail), isDisplayed()));
            appCompatEditText.perform(replaceText("dinego.nos@gmail.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.campoSenha), isDisplayed()));
            appCompatEditText2.perform(replaceText("123456"), closeSoftKeyboard());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.btnEntrar), withText("Entrar"),
                            withParent(withId(R.id.email_login_form)),
                            isDisplayed()));
            appCompatButton.perform(click());
        }


        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Novo anúncio"), isDisplayed()));
        appCompatCheckedTextView4.perform(click());


        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.rdoBtnServico), withText("Serviço"),
                        withParent(withId(R.id.tipoAnuncio)),
                        isDisplayed()));
        appCompatRadioButton2.perform(click());


        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.campoNomeServico), isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.campoValor), isDisplayed()));
        appCompatEditText7.perform(replaceText("R$1,000"), closeSoftKeyboard());


        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.campoDescricao), isDisplayed()));
        appCompatEditText11.perform(replaceText("serviços do campo"), closeSoftKeyboard());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.campoObservacao), isDisplayed()));
        appCompatEditText12.perform(replaceText("aceito dinheiro em forma de pagamento"), closeSoftKeyboard());


        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnCadastrar), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction result = onView(withText(R.string.error_field_required));
                result.check(matches(withText("Campo obrigatório")));

        Assert.assertNotNull(result);

    }

}
