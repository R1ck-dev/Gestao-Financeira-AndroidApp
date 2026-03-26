package com.example.gestaofinanceiraapp.domain.model;
// Classificação dos gastos e organização de orçamento
public class Category {
    private final String id;
    private final String name;
    private final String color;
    private final String icon;

    public Category(String id, String name, String color, String icon) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    // Nas classes não serão implementados setters.
    /*
    No desenvolvimento mobile, a concorrência entre threads é mais agressiva. Se uma classe tem setter, qualquer thread pode alterar
    o seu estado no meio do caminho. Objetos sem setters são imutáveis, sendo uma vez instanciados, não mudam. Caso a interface gráfica
    precise refletir alguma mudança, emitiremos uma nova instância do objeto com o valor alterado, garantindo que a UI receba um estado
    consistente e imutável.
     */
}
