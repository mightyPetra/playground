package org.spend.model;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(makeFinal = true, level= AccessLevel.PRIVATE)
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    String imgAddress;
    @EqualsAndHashCode.Include
    String name;
    double price;

}
