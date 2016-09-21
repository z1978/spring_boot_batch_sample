package com.example.batch2.sub;


import org.springframework.batch.item.ItemProcessor;

import com.example.item.Person;

public class PersonItemProcessor2 implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName();
        final String lastName  = person.getLastName();

        final Person transformedPerson = new Person(firstName, lastName);

        return transformedPerson;
    }

}
