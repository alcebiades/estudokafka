package br.com.alce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SaleService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Sale>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {

                for (int i = 0; i < 10; i++) {

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
