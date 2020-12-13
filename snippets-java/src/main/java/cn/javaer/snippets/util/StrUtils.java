package cn.javaer.snippets.util;

/**
 * @author cn-src
 */
public interface StrUtils {

    /**
     * 将驼峰字符串转换成下划线大写格式.
     *
     * @param str String
     *
     * @return String
     */
    static String toSnakeUpper(final String str) {
        if (null == str || str.isEmpty()) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = str.toCharArray();
        sb.append(Character.toUpperCase(chars[0]));
        for (int i = 1, le = chars.length; i < le; i++) {
            final boolean isUpAndPrevNotLine = Character.isUpperCase(chars[i])
                && '_' != chars[i - 1];

            final boolean isPrevLowOrNotLetter =
                Character.isLowerCase(chars[i - 1]) || !Character.isLetter(chars[i - 1]);

            if (isUpAndPrevNotLine && isPrevLowOrNotLetter) {
                sb.append('_').append(chars[i]);
                continue;
            }
            if ('_' != chars[i] && !Character.isLetter(chars[i]) && Character.isLetter(chars[i - 1])) {
                sb.append('_').append(chars[i]);
                continue;
            }

            sb.append(Character.toUpperCase(chars[i]));
        }
        return sb.toString();
    }

    /**
     * 将驼峰字符串转换成下划线小写格式.
     *
     * @param str String
     *
     * @return String
     */
    static String toSnakeLower(final String str) {
        if (null == str || str.isEmpty()) {
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = str.toCharArray();
        sb.append(Character.toLowerCase(chars[0]));
        for (int i = 1, le = chars.length; i < le; i++) {
            final boolean isUpAndPrevNotLine = Character.isUpperCase(chars[i])
                && '_' != chars[i - 1];

            final boolean isPrevLowOrNotLetter =
                Character.isLowerCase(chars[i - 1]) || !Character.isLetter(chars[i - 1]);

            if (isUpAndPrevNotLine && isPrevLowOrNotLetter) {
                sb.append('_').append(Character.toLowerCase(chars[i]));
                continue;
            }
            if ('_' != chars[i] && !Character.isLetter(chars[i]) && Character.isLetter(chars[i - 1])) {
                sb.append('_').append(chars[i]);
                continue;
            }

            sb.append(Character.toLowerCase(chars[i]));
        }
        return sb.toString();
    }
}
