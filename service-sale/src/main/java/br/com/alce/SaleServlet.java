package br.com.alce;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SaleServlet extends HttpServlet {

    private final KafkaDispatcher<Sale> saleDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        saleDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            var param = req.getParameter("qtd");

            if (param == null) {
                throw new ServletException("Parametro qtd obrigatorio!");
            }

            send(Integer.parseInt(param));

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Foi criado " + param + " vendas!");

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

    private void send(final int qtd) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Sale>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {

                for (int i = 0; i < qtd; i++) {

                    // Eh sempre interessante postar uma mensagem com uma Key para garantir que elas sejam lidas em sequencia,
                    // desta maneira o kafka tem um algoritimo que coloca as mensagens de mesma key sempre na mesma particao e
                    // isso garante que sempre o mesmo consumidor leia as mensagens em sequencia.
                    final var userId = UUID.randomUUID().toString();
                    final var saleId = UUID.randomUUID().toString();
                    final var amount = Math.random() * 5000 + 1;

                    final Sale sale = new Sale(userId, saleId, new BigDecimal(amount));
                    orderDispatcher.send(Configs.SALE_TOPIC_NAME, userId, sale);

                    final String email = "EMAIL - " + i + " - Sua compra foi processada com sucesso! O valor total e de: R$ " + sale.getAmount();
                    emailDispatcher.send(Configs.EMAIL_TOPIC_NAME, userId, email);
                }
            }
        }
    }
}
