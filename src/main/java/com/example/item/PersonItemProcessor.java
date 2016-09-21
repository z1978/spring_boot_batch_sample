package com.example.item;


import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName();
        final String lastName  = person.getLastName();

        final Person transformedPerson = new Person(firstName, lastName);

        return transformedPerson;
    }

}
