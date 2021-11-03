package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.core.AllOf.allOf;

import static br.com.alura.leilao.ui.activity.matchers.ViewMatcher.apareceLeilaoNaPosicao;

import android.content.Intent;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoTelaTest {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);
    private final LeilaoWebClient webClient = new LeilaoWebClient();

    @Before
    public void setup() throws IOException {
        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            Assert.fail("Banco de dados não foi limpo.");
        }
    }

    @Test
    public void deve_AparecerUmLeilao_quando_CarregarUmLeilaoNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(new Leilao("Carro"));

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(0, "Carro", 0.00)));
    }

    @Test
    public void deve_AparecerDoisLeiloes_quando_CarregarDoisLeiloesNaApi() throws IOException {
        tentaSalvarLeilaoNaApi(
                new Leilao("Carro"),
                new Leilao("Computador"));

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(0, "Carro", 0.00)));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilaoNaPosicao(1, "Computador", 0.00)));
    }

    private void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes) {
            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                Assert.fail("Leilão não foi salvo: " + leilao.getDescricao());
            }
        }
    }
}
