package br.com.alura.leilao.ui.activity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.model.Leilao;

public class ListaLeilaoTelaTest  {

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Test
    public void deve_AparecerUmLeilao_quando_CarregarUmLeilaoNaApi() throws IOException {
        Leilao carroSalvo = new LeilaoWebClient().salva(new Leilao("Carro"));
        if (carroSalvo == null) {
            Assert.fail("Leilão não foi salvo.");
        }

        activity.launchActivity(new Intent());

        onView(withText("Carro")).check(matches(isDisplayed()));
    }
}