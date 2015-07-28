package ru.family.tree.utils.db;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author scorpion@yandex-team on 11.04.15.
 */
@SuppressWarnings("MessageMissingOnJUnitAssertion")
public class DbUtilsTest {

    @Test
    public void testGeneratePlaceholders() throws Exception {
        assertThat(DbUtils.generatePlaceholders("# some text", 3), is("?, ?, ? some text"));
        assertThat(DbUtils.generatePlaceholders("# some # text", 3, 2), is("?, ?, ? some ?, ? text"));
        assertThat(DbUtils.generatePlaceholders("some text #", 3), is("some text ?, ?, ?"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePlaceholdersWithNotEnoughArgs() throws Exception {
        DbUtils.generatePlaceholders("# some text", 3, 2);
    }
}