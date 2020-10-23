package cn.javaer.snippets.kryo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class City {

    @Id
    private Long id;

    private String name;

    protected City() {
    }

    public City(final String name) {
        this.name = name;
    }
}
