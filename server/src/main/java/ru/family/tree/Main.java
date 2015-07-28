package ru.family.tree;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author scorpion@yandex-team on 30.03.15.
 */
public class Main {

    public static void main(final String[] args) {
        new FileSystemXmlApplicationContext("classpath:application.xml").start();
    }
}
