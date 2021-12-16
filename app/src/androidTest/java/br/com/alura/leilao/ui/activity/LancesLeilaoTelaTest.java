package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.BaseTesteIntegracao;
import br.com.alura.leilao.R;
import br.com.alura.leilao.model.Leilao;

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
        onView(withId(R.id.lances_leilao_fab_adiciona)).perform(click());
        // Verifica se aparece dialog de aviso por não ter usuário com título e mensagem esperada
        onView(withText("Usuários não encontrados")).check(matches(isDisplayed()));
        onView(withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance.")).check(matches(isDisplayed()));
        // Clica no botão cadastrar usuário
        onView(withText("Cadastrar usuário")).perform(click());
    }

    @After
    public void teardown() throws IOException {
        limpaBancoDeDados();
        limpaBancoDeDadosInterno();
    }
}
