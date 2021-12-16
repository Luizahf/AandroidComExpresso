package br.com.alura.leilao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.model.Leilao;

public abstract class BaseTesteIntegracao {
    private final LeilaoWebClient webClient = new LeilaoWebClient();

    @Before
    public void limpaBancoDeDados() throws IOException {
        boolean bancoDeDadosNaoFoiLimpo = !webClient.limpaBancoDeDados();
        if (bancoDeDadosNaoFoiLimpo) {
            Assert.fail("Banco de dados não foi limpo.");
        }
    }

    public  void limpaBancoDeDadosInterno() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.deleteDatabase(BuildConfig.BANCO_DE_DADOS);
    }

    protected void tentaSalvarLeilaoNaApi(Leilao... leiloes) throws IOException {
        for (Leilao leilao : leiloes) {
            Leilao leilaoSalvo = webClient.salva(leilao);
            if (leilaoSalvo == null) {
                Assert.fail("Leilão não foi salvo: " + leilao.getDescricao());
            }
        }
    }
}
