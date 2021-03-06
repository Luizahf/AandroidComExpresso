package br.com.alura.leilao.ui.activity;


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

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;

public class ListaUsuariosTelaTest extends BaseTesteIntegracao {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> mActivityTestRule = new ActivityTestRule<>(ListaLeilaoActivity.class);

    @Before
    public void setup() throws IOException {
        limpaBancoDeDados();
        limpaBancoDeDadosInterno();
    }
    @Test
    public void deve_AparecerUsuarioNaListaDeUsuarios_quando_CadastrarUmUsuario() {
        onView(
            allOf(withId(R.id.lista_leilao_menu_usuarios), withContentDescription("Usuários"),
            isDisplayed()))
            .perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.lista_usuario_fab_adiciona),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.form_usuario_nome),
                                0),
                        0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("Luiza"), closeSoftKeyboard());

        onView(
                allOf(withId(android.R.id.button1), withText("Adicionar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3))).perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_usuario_id_com_nome), withText("(1) Luiza"),
                        withParent(withParent(withId(R.id.lista_usuario_recyclerview))),
                        isDisplayed()));
        textView.check(matches(withText("(1) Luiza")));
    }


    @After
    public void teardown() throws IOException {
        limpaBancoDeDados();
        limpaBancoDeDadosInterno();
    }
}
