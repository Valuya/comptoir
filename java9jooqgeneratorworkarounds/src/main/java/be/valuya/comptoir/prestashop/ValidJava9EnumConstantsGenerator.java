package be.valuya.comptoir.prestashop;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.postgres.PostgresDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Work around issue https://github.com/jOOQ/jOOQ/issues/4703
 */
public class ValidJava9EnumConstantsGenerator extends JavaGenerator {

    @Override
    protected void generateEnum(EnumDefinition e, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(e, GeneratorStrategy.Mode.ENUM);
        final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(e, GeneratorStrategy.Mode.ENUM));
        final List<String> literals = e.getLiterals();
        final List<String> identifiers = new ArrayList<String>(literals.size());

        for (int i = 0; i < literals.size(); i++) {
            String literal = literals.get(i);
            String baseIdentifier = literal.replaceAll("-", "_")
                    .toUpperCase();
            String safeIdentifier = "_" + baseIdentifier + "_VALUE";

            // [#2781] Disambiguate collisions with the leading package name
            if (safeIdentifier.equals(getStrategy().getJavaPackageName(e).replaceAll("\\..*", "")))
                safeIdentifier += "_";

            identifiers.add(safeIdentifier);
        }

        printPackage(out, e);
        generateEnumClassJavadoc(e, out);
        printClassAnnotations(out, e.getSchema());


        boolean enumHasNoSchema = e.isSynthetic() || !(e.getDatabase() instanceof PostgresDatabase);

        interfaces.add(out.ref(EnumType.class));
        out.println("public enum %s[[before= implements ][%s]] {", className, interfaces);

        for (int i = 0; i < literals.size(); i++) {
            out.println();
            out.tab(1).println("%s(\"%s\")%s", identifiers.get(i), literals.get(i), (i == literals.size() - 1) ? ";" : ",");
        }

        out.println();
        out.tab(1).println("private final %s literal;", String.class);
        out.println();
        out.tab(1).println("private %s(%s literal) {", className, String.class);
        out.tab(2).println("this.literal = literal;");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public %s getCatalog() {", Catalog.class);

        if (enumHasNoSchema)
            out.tab(2).println("return null;");
        else
            out.tab(2).println("return getSchema() == null ? null : getSchema().getCatalog();");

        out.tab(1).println("}");

        // [#2135] Only the PostgreSQL database supports schema-scoped enum types
        out.tab(1).overrideInherit();
        out.tab(1).println("public %s getSchema() {", Schema.class);
        out.tab(2).println("return null;");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public %s getName() {", String.class);
        out.tab(2).println("return %s;", e.isSynthetic() ? "null" : "\"" + e.getName().replace("\"", "\\\"") + "\"");
        out.tab(1).println("}");

        out.tab(1).overrideInherit();
        out.tab(1).println("public %s getLiteral() {", String.class);
        out.tab(2).println("return literal;");
        out.tab(1).println("}");

        generateEnumClassFooter(e, out);
        out.println("}");
    }
}
