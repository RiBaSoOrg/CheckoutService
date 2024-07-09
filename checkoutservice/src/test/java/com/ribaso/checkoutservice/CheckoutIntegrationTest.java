package com.ribaso.checkoutservice;

import com.jayway.jsonpath.JsonPath;
import com.ribaso.checkoutservice.core.domain.model.Checkout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CheckoutIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testInitiateCheckout() throws Exception {
        mockMvc.perform(post("/api/checkout/initiate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("pending")));
    }

    @Test
    public void testCompleteCheckout() throws Exception {
        mockMvc.perform(post("/api/checkout/initiate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    // Lesen der ID als Integer und Konvertieren zu String
                    Integer id = JsonPath.read(json, "$.id");
                    String idStr = String.valueOf(id); // Verwende String.valueOf um sicher Integer zu String zu konvertieren

                    // Verwende die ID als String für den folgenden Request
                    mockMvc.perform(post("/api/checkout/complete/" + idStr)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.status", is("completed")));
                });
    }
}