package cn.javaer.snippets.kryo;

import lombok.Data;

@Data
public class City {

    private Long id;

    private String name;

    protected City() {
    }

    public City(final String name) {
        this.name = name;
    }
}
