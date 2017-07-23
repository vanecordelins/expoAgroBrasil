package com.expoagro.expoagrobrasil.view;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.controller.LoginActivity;

import junit.framework.Assert;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Samir on 11/07/2017.
 */

public class CadastroUsuarioActivityTesteExistente {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void cadastroUsuarioActivityTeste() throws Exception {

        ViewInteraction appCompatTextView = onView(allOf(withId(R.id.textoNovoCadastro), withText("NÃO É CADASTRADO? CADASTRE-SE"), isDisplayed()));
        appCompatTextView.perform(click());

        Thread.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView2 = onView(allOf(withId(R.id.campoNome), isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText("diego"), closeSoftKeyboard());

        ViewInteraction appCompatAutoCompleteTextView3 = onView(allOf(withId(R.id.campoEmail), isDisplayed()));
        appCompatAutoCompleteTextView3.perform(replaceText("diego.tester@teste.com"), closeSoftKeyboard());

        Thread.sleep(3000);

        ViewInteraction phoneEditText = onView(allOf(withId(R.id.campoTelefone), isDisplayed()));
        phoneEditText.perform(replaceText("(89)99999-9999"), closeSoftKeyboard());

        Thread.sleep(3000);

        ViewInteraction appCompatSpinner = onView(allOf(withId(R.id.spinner), isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(allOf(withId(android.R.id.text1), withText("Abreu e Lima"),
                        childAtPosition(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),1),isDisplayed()));
        appCompatCheckedTextView.perform(click());

        Thread.sleep(3000);

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.campoSenha), isDisplayed()));
        appCompatEditText.perform(replaceText("senha1"), closeSoftKeyboard());

        Thread.sleep(3000);

        ViewInteraction appCompatEditText2 = onView(allOf(withId(R.id.campoRepetir), isDisplayed()));
        appCompatEditText2.perform(replaceText("senha1"), closeSoftKeyboard());

        Thread.sleep(3000);

        ViewInteraction appCompatEditText3 = onView(allOf(withId(R.id.campoRepetir), withText("senha1"), isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        Thread.sleep(3000);

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btnCadastrar), withText("Cadastrar"), isDisplayed()));
        appCompatButton.perform(click());

        Thread.sleep(3000);

        ViewInteraction imageView = onView(allOf(withId(R.id.logo), isDisplayed()));
        ViewInteraction result = imageView.check(doesNotExist());

        Assert.assertNotNull(result);

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
