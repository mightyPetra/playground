package org.spend.model;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    String email;
    @EqualsAndHashCode.Include
    String password;

    String firstName;
    String lastName;

}
