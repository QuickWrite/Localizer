package net.quickwrite.localizer.processor;

import java.util.ArrayList;
import java.util.List;

public class JClassFileGenerator {
    private final String packageName;
    private final String className;

    private final List<String> imports;

    private final List<String> attributes;
    private final List<String> methods;

    private final List<String> staticConstructor;

    public JClassFileGenerator(final String packageName, final String className) {
        this.packageName = packageName;
        this.className = className;

        this.imports = new ArrayList<>();

        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();

        this.staticConstructor = new ArrayList<>();
    }

    public JClassFileGenerator addImport(final String importPath) {
        this.imports.add(importPath);

        return this;
    }

    public JClassFileGenerator addAttribute(final String attribute) {
        this.attributes.add(attribute);

        return this;
    }

    public JClassFileGenerator addMethod(final String method) {
        this.methods.add(method);

        return this;
    }

    public JClassFileGenerator addStaticConstructorElement(final String staticConstructorElement) {
        this.staticConstructor.add(staticConstructorElement);

        return this;
    }

    public String generate() {
        return String.format("""
                        /**
                        * This file was automatically generated.
                        * Don't change this class as these changes will be lost.
                        */
                        package %s;
                                        
                        %s
                                        
                        public class %s {
                            // attributes
                            %s
                            
                            // static constructor
                            static {
                                %s
                            }
                            
                            // methods
                            %s
                        }
                        """,
                this.packageName,
                formatImports(this.imports),
                this.className,
                formatLines(this.attributes, 4),
                formatLines(this.staticConstructor, 8),
                formatLines(this.methods, 4)
        );
    }

    private static String formatImports(final List<String> imports) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < imports.size(); i++) {
            builder.append("import ");
            builder.append(imports.get(i));
            builder.append(";");

            if (i != imports.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private static String formatLines(final List<String> list, final int indent) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            final String[] stringList = list.get(i).split("\n");

            for (int j = 0; j < stringList.length; j++) {
                if (!(i == 0 && j == 0)) {
                    builder.append(" ".repeat(Math.max(0, indent)));
                }

                builder.append(stringList[j]);

                if (!(i == list.size() - 1 && j == stringList.length - 1)) {
                    builder.append("\n");
                }
            }

            if (i != list.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
