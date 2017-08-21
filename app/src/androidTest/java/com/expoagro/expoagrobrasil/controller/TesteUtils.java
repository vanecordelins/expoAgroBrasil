package com.expoagro.expoagrobrasil.controller;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.view.WindowManager;

import com.expoagro.expoagrobrasil.R;
import com.google.firebase.auth.FirebaseAuth;

import junit.framework.Assert;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


class TesteUtils {

    public static class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken.equals(appToken)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void espera(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     }

    public static void espera(int tempo){
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void fazerLogin(){
        espera(3000);
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
    }

    public static void preencheCampo(int campo, String texto){

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(campo), isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText(texto), closeSoftKeyboard());
    }

    public static void clicaEm(int botao, String texto){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(click());
        espera();
    }

    public static void clicaEm(int botao, String texto,ViewAction scroll){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(scroll, click());
        espera();
    }

    public static void vejaTexto(String texto){
        ViewInteraction result = onView(withText(texto)).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText(texto)));

        Assert.assertNotNull(result);
    }

    public static void vejaErro(int campo,String texto){
        ViewInteraction result = onView(
                allOf(withId(campo), isDisplayed()));
        result.check(matches(hasErrorText(texto)));

        Assert.assertNotNull(result);
    }

    public static void vejaItem(int item) {
        ViewInteraction result = onView(withId(item));
        result.check(matches(isDisplayed()));

        Assert.assertNotNull(result);
    }

    public static void abreMenu(int menu, String texto){
        espera(3000);
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(menu), withText(texto), isDisplayed()));
        appCompatCheckedTextView2.perform(click());


    }

    public static void selecionaItem(int recycle, int item){
        ViewInteraction recyclerView = onView(
                allOf(withId(recycle),isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(item, click()));
    }

}