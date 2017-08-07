package com.expoagro.expoagrobrasil.controller;

import android.os.IBinder;
import android.support.test.espresso.Root;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


class TesteUtils {
    static class ToastMatcher extends TypeSafeMatcher<Root> {

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

    static public ViewInteraction matcherResult(int id ,String text){
        ViewInteraction buttonCadastrar = onView(
                allOf(withId(R.id.btnCadastrar), isDisplayed()));
        buttonCadastrar.perform(scrollTo(), click());


        ViewInteraction result = onView(
                allOf(withId(id), isDisplayed()));
        result.check(matches(withText(text)));
        return result;
    }

    static public void fazerLogin(){
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

    static void preencheCampo(int campo, String texto){

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(campo), isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText(texto), closeSoftKeyboard());
    }

    static void clicaEm(int botao, String texto){
        ViewInteraction appCompatButton = onView(
                allOf(withId(botao), withText(texto), isDisplayed()));
        appCompatButton.perform(click());
    }

    static void verificaTexto(String texto){
        ViewInteraction result = onView(withText(texto)).inRoot(new TesteUtils.ToastMatcher())
                .check(matches(withText(texto)));

        Assert.assertNotNull(result);
    }

    static void abreMenu(int menu, String texto){
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(menu), withText(texto), isDisplayed()));
        appCompatCheckedTextView2.perform(click());
    }
}