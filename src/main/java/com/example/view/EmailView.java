package com.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

@Route("email")
@PageTitle("Envio de Emails")
@Menu(order = 2, icon = "vaadin:envelope", title = "Enviar Email")
public class EmailView extends VerticalLayout {

    public EmailView() {
        EmailField emailField = new EmailField("Endereço do destinatário");
        Checkbox incluirAnexo = new Checkbox("Incluir PDF em anexo");
        Button enviar = new Button("Enviar Email", event -> enviarEmail(emailField.getValue(), incluirAnexo.getValue()));

        add(emailField, incluirAnexo, enviar);
        setPadding(true);
        setSpacing(true);
    }

    private void enviarEmail(String destinatario, boolean incluirAnexo) {
        try {
            RestTemplate rest = new RestTemplate();
            String url = String.format("http://localhost:8080/api/email/enviar?destinatario=%s&incluirAnexo=%b",
                    destinatario, incluirAnexo);
            rest.postForObject(url, null, String.class);
            Notification.show("Email enviado com sucesso!");
        } catch (Exception e) {
            Notification.show("Erro ao enviar email: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}