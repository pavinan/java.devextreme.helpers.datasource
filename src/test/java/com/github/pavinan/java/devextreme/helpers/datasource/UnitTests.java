/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.pavinan.java.devextreme.helpers.datasource;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.Tree;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.List;

import com.github.pavinan.java.CaseChangingCharStream;
import com.github.pavinan.java.mysqlparser.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class UnitTests {

    private WhereClauseBuilder builder = new WhereClauseBuilder();

    @Test
    public void test1() throws Exception {
        
        String json = "[\"CustomerID\",\"=\",\"ALFKI\"]";

        parseAndTest(json);
    }

    @Test
    public void test2() throws Exception {

        String json = "[[\"CustomerID\",\"=\",\"ALFKI\"],\"or\",[\"CustomerID\",\"=\",\"ANATR\"]]";

        parseAndTest(json);
    }

    @Test
    public void test3() throws Exception {
        
        String json = "[\"!\",[[\"CustomerID\",\"=\",\"ALFKI\"],\"or\",[\"CustomerID\",\"=\",\"ANATR\"]]]";

        parseAndTest(json);
    }

    @Test

    public void test4() throws Exception {

        String json = "[[\"!\",[[\"CustomerID\",\"=\",\"ALFKI\"],\"or\",[\"CustomerID\",\"=\",\"ANATR\"]]],\"and\",[\"!\",[[\"ShipCountry\",\"=\",\"Argentina\"],\"or\",[\"ShipCountry\",\"=\",\"Austria\"]]]]";

        parseAndTest(json);
    }

    @Test
    public void test5() throws Exception {

        String json = "[[\"ShipCountry\",\"contains\",\"i\"],\"and\",[\"!\",[[\"CustomerID\",\"=\",\"ALFKI\"],\"or\",[\"CustomerID\",\"=\",\"ANATR\"]]],\"and\",[\"!\",[[\"ShipCountry\",\"=\",\"Argentina\"],\"or\",[\"ShipCountry\",\"=\",\"Austria\"]]]]";

        parseAndTest(json);
    }

    @Test
    public void test6() throws Exception {

        String json = "[[\"ShipCountry\",\"contains\",\"i\"],\"and\",[\"!\",[[\"CustomerID\",\"=\",\"ALFKI\"],\"or\",[\"CustomerID\",\"=\",\"ANATR\"]]],\"and\",[[[\"OrderDate\",\">=\",\"1996-07-08T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-09T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-11T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-12T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-12T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-13T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-15T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-16T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-18T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-19T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-19T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-20T00:00:00\"]],\"or\",[[\"OrderDate\",\">=\",\"1996-07-26T00:00:00\"],\"and\",[\"OrderDate\",\"<\",\"1996-07-27T00:00:00\"]]],\"and\",[\"!\",[[\"ShipCountry\",\"=\",\"Argentina\"],\"or\",[\"ShipCountry\",\"=\",\"Austria\"]]]]";

        parseAndTest(json);
    }

    @Test
    public void test7() throws Exception {

        String json = "[[\"col1\", \"=\", 45], [\"col2\", \"=\", true]]";

        parseAndTest(json);
    }

    @Test
    public void test8() throws Exception {

        String json = "[[\"col1\", \"<\", 45], [\"col2\", \"=\", true]]";

        parseAndTest(json);
    }

    @Test
    public void test9() throws Exception {

        String json = "[[\"col1\", \"<\", 45], [\"col2\", \">\", 1000]]";

        parseAndTest(json);
    }

    private void parseAndTest(String json) throws Exception {

        JsonArray jArray = (JsonArray) (new JsonParser().parse(json));
        System.out.println(json);
        System.out.println();

        String sql = builder.buildFor(jArray);

        sql = "SELECT * FROM Customers where " + sql + "";

        CharStream charStream = CharStreams.fromString(sql);
        CaseChangingCharStream upper = new CaseChangingCharStream(charStream, true);

        MySqlLexer lexer = new MySqlLexer(upper);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokenStream);

        // parser.setErrorHandler(new BailErrorStrategy());
        parser.setBuildParseTree(false);

        String tree = parser.root().toStringTree();

        System.out.println(tree);

        int totalErrors = parser.getNumberOfSyntaxErrors();

        System.out.println("Total errors : " + totalErrors);

        assertEquals(0, totalErrors);

    }
}
