package io.github.victorhsr.sample.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonResource {

    private final RegisterPersonToPersonMapper registerPersonToPersonMapper;
    private final PersonRepository personRepository;

    @Autowired
    public PersonResource(RegisterPersonToPersonMapper registerPersonToPersonMapper, PersonRepository personRepository) {
        this.registerPersonToPersonMapper = registerPersonToPersonMapper;
        this.personRepository = personRepository;
    }

    @PostMapping
    public ResponseEntity<Void> registerPerson( @RequestBody final RegisterPersonDTO registerPersonDTO){

        final Person person = this.registerPersonToPersonMapper.apply(registerPersonDTO);
        this.personRepository.persistPerson(() -> registerPersonDTO.getTenant(), person);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
