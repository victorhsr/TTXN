package io.github.victorhsr.sample.person;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterPersonToPersonMapper implements Function<RegisterPersonDTO, Person> {

    @Override
    public Person apply(final RegisterPersonDTO registerPersonDTO) {

        final Person person = Person
                .builder()
                .fullName(registerPersonDTO.getFullName())
                .build();

        return person;
    }
}
