package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

public class LancesLeilaoTelaTest extends BaseTesteIntegracao {
    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setup() throws IOException {
        limpaBancoDeDados();
        limpaBancoDeDadosInterno();
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_quando_ReceberUmLeilao() throws IOException {
        // Salva leilão na API
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));
        // Inicia a activity
        activity.launchActivity(new Intent());
        // Clica no leilão
        onView(withId(R.id.lista_leilao_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Clicla no float action button de lances do leilão
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona), isDisplayed())).perform(click());
        // Verifica se aparece dialog de aviso por não ter usuário com título e mensagem esperada
        onView(allOf(withText("Usuários não encontrados"), withId(R.id.alertTitle))).check(matches(isDisplayed()));
        onView(allOf(withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."), withId(android.R.id.message))).check(matches(isDisplayed()));
        // Clica no botão cadastrar usuário
        onView(withText("Cadastrar usuário")).perform(click());
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

        Espresso.pressBack();
        onView(withId(R.id.lances_leilao_fab_adiciona)).perform(click());
        onView(withText("Novo lance")).check(matches(isDisplayed()));
        onView(withId(R.id.form_lance_valor_edittext)).perform(click(), replaceText("200"), closeSoftKeyboard());
        onView(withId(R.id.form_lance_usuario)).perform(click());
        onData(is(new Usuario(1, "Luiza"))).inRoot(isPlatformPopup()).perform(click());
        onView(withText("Propor")).perform(click());

        FormatadorDeMoeda formatador = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance)).check(
                matches(allOf(withText(formatador.formata(200)), isDisplayed())));
        onView(withId(R.id.lances_leilao_menor_lance)).check(
                matches(allOf(withText(formatador.formata(200)), isDisplayed())));
        onView(withId(R.id.lances_leilao_maiores_lances)).check(
                matches(allOf(withText("200.0 - (1) Luiza\n"), isDisplayed())));
    }

    @After
    public void teardown() throws IOException {
        limpaBancoDeDados();
        limpaBancoDeDadosInterno();
    }
}
