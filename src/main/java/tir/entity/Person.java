package tir.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Person {

    private String name;
    private int age;
    private List<Person> friends;
    private String[] favoriteBooks;
}