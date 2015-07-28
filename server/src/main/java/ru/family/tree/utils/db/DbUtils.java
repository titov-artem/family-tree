package ru.family.tree.utils.db;

/**
 * @author scorpion@yandex-team on 11.04.15.
 */
public class DbUtils {
    public static final char PLACEHOLDERS_MARK = '#';

    private DbUtils() {
    }

    public static String generatePlaceholders(final String sourceSql, final int... placeNums) {
        final StringBuilder out = new StringBuilder(sourceSql.length());
        int prevIndex = 0;
        for (final int placeNum : placeNums) {
            final int pos = sourceSql.indexOf(PLACEHOLDERS_MARK, prevIndex);
            if (pos == -1) {
                throw new IllegalArgumentException("Not enough placeholders mark found! Expected " + placeNums.length);
            }
            out.append(sourceSql.substring(prevIndex, pos));
            for (int i = 0; i < placeNum; i++) {
                out.append("?");
                if (i != placeNum - 1) {
                    out.append(", ");
                }
            }
            prevIndex = pos + 1;
        }
        out.append(sourceSql.substring(prevIndex, sourceSql.length()));
        return out.toString();
    }

}
