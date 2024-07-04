package tir;


import tir.entity.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Person person1 = new Person("John",
                                    46,
                                    Collections.emptyList(),
                                    new String[]{"The Pragmatic Programmer"}
        );
        Person person2 = new Person("Valod",
                                    34,
                                    Collections.singletonList(person1),
                                    new String[]{"Clean Code", "Concurrency in practice"}
        );
        Person person3 = new Person("Mike",
                                    27,
                                    List.of(person1, person2),
                                    new String[]{"Component Oriented Development", "Code Complete"}
        );
        person1.setFriends(List.of(person2, person3));
        print(person3);

        System.out.println("*********************************************");

        Person deepCopy = CopyUtils.deepCopy(person3);
        print(deepCopy);
    }

    private static void print(Person person) {
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());
        System.out.print("Friends: ");
        person.getFriends().forEach(p -> System.out.print(p.getName() + " "));
        System.out.println();
        System.out.println("Favorite books: " + Arrays.toString(person.getFavoriteBooks()));
    }
}
