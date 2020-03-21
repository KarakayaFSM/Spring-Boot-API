package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("fake")
public class FakePersonDAService implements PersonDao {

    private static List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public Optional<Person> getPersonById(UUID id) {
        return DB.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Person> selectAll() {
        return DB;
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> probablePerson = getPersonById(id);

        if (!probablePerson.isPresent()) {
            return 0;
        }

        DB.remove(probablePerson.get());
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        return getPersonById(id)
                .map(p -> {
                    int personIdx = DB.indexOf(p);

                    if (personIdx >= 0) {
                        DB.set(personIdx, new Person(id, person.getName()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }
}
