package cn.javaer.snippets.jooq.codegen;

import org.jooq.codegen.GeneratorWriter;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.TableDefinition;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @author cn-src
 */
public class SnippetsGenerator extends JavaGenerator {

    final Pattern jsonbPattern = Pattern.compile("public final TableField<Record, JSONB>(.*)createField\\(DSL.name\\((.*)\\), org.jooq.impl.SQLDataType.JSONB, this, (.*)\\);");
    final Pattern geometryPattern = Pattern.compile("/\\*\\*\\s+\\* @deprecated .+\\s+\\*/\\s+@java\\.lang\\.Deprecated\\s*public final TableField<Record, Object> (\\w+) = createField\\(DSL.name\\((.+)\\), org.jooq.impl.DefaultDataType.getDefaultDataType\\(.+geometry.+\\), this, (.+)\\);", Pattern.MULTILINE);

    public SnippetsGenerator() {
        this.setGenerateRoutines(false);
        this.setGenerateUDTs(false);
        this.setGenerateTableValuedFunctions(false);
        this.setGenerateRecords(false);
    }

    @Override
    protected void generateTable(SchemaDefinition schema, TableDefinition table) {

        JavaWriter out = this.newJavaWriter(this.getFile(table));
        this.generateTable(table, out);
        try {
            Field field = GeneratorWriter.class.getDeclaredField("sb");
            field.setAccessible(true);
            StringBuilder sb = (StringBuilder) field.get(out);
            String newSb = this.replaceJsonb(sb.toString());
            newSb = this.replaceGeometry(newSb);
            sb.setLength(0);
            sb.append(newSb);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
        this.closeJavaWriter(out);
    }

    String replaceJsonb(String str) {
        return this.jsonbPattern.matcher(str)
                .replaceAll("public final cn.javaer.snippets.jooq.field.JsonbField<Record, JSONB>$1new cn.javaer.snippets.jooq.field.JsonbField($2, org.jooq.impl.SQLDataType.JSONB, this);");
    }

    String replaceGeometry(String str) {
        return this.geometryPattern.matcher(str)
                .replaceAll("/**\n" +
                        "    " +
                        " * The column $1.\n" +
                        "    " +
                        " */\n" +
                        "    " +
                        "public final TableField<Record, cn.javaer.snippets.type.Geometry> $1 = createField(DSL.name($2), cn.javaer.snippets.jooq.SQL.GEOMETRY_TYPE, this, $3);");
    }
}
