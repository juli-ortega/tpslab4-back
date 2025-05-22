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
import com.tp4lab4.instrumentos.Model.PedidoDetalle;
import com.tp4lab4.instrumentos.Model.PreferenceMp;

public class MercadoPagoController {
    
     public PreferenceMp getPreferenciaIdMercadoPago(Pedido pedido, List<PedidoDetalle> detalles) {
        try { 
            MercadoPagoConfig.setAccessToken("TEST-2965173602732262-051509-e8b73a730ccdac3f881efb6f1c826656-324104899");

            List<PreferenceItemRequest> items = new ArrayList<>();

            for (PedidoDetalle detalle : detalles) {
                PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(detalle.getInstrumento().getId()))
                    .title(detalle.getInstrumento().getInstrumento())
                    .description("Instrumento musical")
                    .pictureUrl(detalle.getInstrumento().getImagen()) // si tenés imagen
                    .quantity(detalle.getCantidad())
                    .currencyId("ARS")
                    .unitPrice(BigDecimal.valueOf(detalle.getInstrumento().getPrecio()))
                    .build();

                items.add(item);
            }

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:5173/resultadopago")
                .pending("http://localhost:5173/resultadopago")
                .failure("http://localhost:5173/resultadopago")
                .build();

            PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .build();

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

