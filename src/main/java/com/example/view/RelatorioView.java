package com.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route("relatorio")
@PageTitle("Relatório PDF")
@Menu(order = 1, icon = "vaadin:file-text", title = "Relatório PDF")
public class RelatorioView extends VerticalLayout {

    public RelatorioView() {
        setSpacing(true);
        setPadding(true);

        // Cria um link direto para o endpoint que gera o PDF
        Anchor link = new Anchor("/api/relatorio/pdf", "");
        link.getElement().setAttribute("download", true);

        // Botão visual que aciona o download
        Button botao = new Button("Exportar PDF", new Icon(VaadinIcon.FILE_TEXT));
        botao.addClickListener(e ->
                getUI().ifPresent(ui -> ui.getPage().open("/api/relatorio/pdf"))
        );

        add(botao);
    }
}