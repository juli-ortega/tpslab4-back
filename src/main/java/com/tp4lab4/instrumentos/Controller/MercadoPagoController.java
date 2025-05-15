package com.tp4lab4.instrumentos.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.tp4lab4.instrumentos.Model.Pedido;
import com.tp4lab4.instrumentos.Model.PreferenceMp;

public class MercadoPagoController {
    
    public PreferenceMp getPreferenciaIdMercadoPago(Pedido pedido){
        try {
            MercadoPagoConfig.setAccessToken("TEST-2965173602732262-051509-e8b73a730ccdac3f881efb6f1c826656-324104899");
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("1234")
                .title("Instrumentos Musicales")
                .description("Instrumentos realizados desde un carrito de compras")
                .pictureUrl("https://media.istockphoto.com/id/894058154/es/foto/instrumentos-musical.jpg?s=612x612&w=0&k=20&c=WjTwmZPcFkGuAU7DAjpToSMe5baR8XJvHkyg3jMxkWg=")
                .quantity(1)
                .currencyId("ARG")
                .unitPrice(new BigDecimal(pedido.getTotal()))
                .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:3000/")
                .pending("http://localhost:3000/")
                .failure("http://localhost:3000/")
                .build();
            PreferenceRequest request = PreferenceRequest.builder().items(items).backUrls(backUrls).build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);

            PreferenceMp preferenceMp = new PreferenceMp();
            preferenceMp.setStatusCode(preference.getResponse().getStatusCode());
            preferenceMp.setId(preference.getId());
            return preferenceMp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
