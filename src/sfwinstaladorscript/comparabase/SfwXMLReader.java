package sfwinstaladorscript.comparabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sfwinstaladorscript.comparabase.objects.BaseCompare;
import sfwinstaladorscript.comparabase.objects.Column;
import sfwinstaladorscript.comparabase.objects.Constraint;
import sfwinstaladorscript.comparabase.objects.Function;
import sfwinstaladorscript.comparabase.objects.Index;
import sfwinstaladorscript.comparabase.objects.Procedure;
import sfwinstaladorscript.comparabase.objects.Sequence;
import sfwinstaladorscript.comparabase.objects.Table;
import sfwinstaladorscript.comparabase.objects.Trigger;
import sfwinstaladorscript.comparabase.objects.View;
import sfwinstaladorscript.comparabase.objects.Package;
import sfwinstaladorscript.comparabase.objects.Package_Body;

public class SfwXMLReader {

    @SuppressWarnings({"unchecked"})
    public BaseCompare ReadXML(String ArquivoComparacao) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(ArquivoComparacao);
        BaseCompare v_baseCompare = new BaseCompare();
        Element node;
        Document document = (Document) builder.build(xmlFile);
        Element rootNode = document.getRootElement();
        String[] v_objects = new String[]{"TABLE", "COLUMN", "CONSTRAINT", "INDEX", "PACKAGE", "PACKAGE_BODY", "PROCEDURE", "TRIGGER", "VIEW",
            "FUNCTION", "SEQUENCE"};
        //
        Element v_info = rootNode.getChild("INFO");
        v_baseCompare.set_stringVersao(v_info.getChildText("VERSAO"));
        for (int j = 0; j < v_objects.length; j++) {
            List<Element> list = rootNode.getChildren(v_objects[j]);
            // Lendo XML para os objetos TABLE
            if (v_objects[j].equals("TABLE")) {
                List<Table> v_listTable = new ArrayList<Table>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Table v_table = new Table();
                    v_table.set_tableName(node.getChildText("TABLE_NAME"));
                    v_listTable.add(v_table);
                }
                v_baseCompare.set_listTable(v_listTable);
                //
                // Lendo XML para os objetos COLUMN
            } else if (v_objects[j].equals("COLUMN")) {
                List<Column> v_listColumn = new ArrayList<Column>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Column v_column = new Column();
                    v_column.set_columnName(node.getChildText("COLUMN_NAME"));
                    v_column.set_dataType(node.getChildText("DATA_TYPE"));
                    v_listColumn.add(v_column);
                }
                v_baseCompare.set_listColumn(v_listColumn);
                //
                // Lendo XML para os objetos CONSTRAINT
            } else if (v_objects[j].equals("CONSTRAINT")) {
                List<Constraint> v_listConstraint = new ArrayList<Constraint>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Constraint v_constraint = new Constraint();
                    v_constraint.set_constraintName(node.getChildText("CONSTRAINT_NAME"));
                    v_constraint.set_constraintType(node.getChildText("CONSTRAINT_TYPE"));
                    v_listConstraint.add(v_constraint);
                }
                v_baseCompare.set_listConstraint(v_listConstraint);
                //
                // Lendo XML para os objetos INDEX
            } else if (v_objects[j].equals("INDEX")) {
                List<Index> v_listIndex = new ArrayList<Index>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Index v_index = new Index();
                    v_index.set_indexName(node.getChildText("INDEX_NAME"));
                    v_listIndex.add(v_index);
                }
                v_baseCompare.set_listIndex(v_listIndex);
                //
                // Lendo XML para os objetos PACKAGE
            } else if (v_objects[j].equals("PACKAGE")) {
                List<Package> v_listPackage = new ArrayList<Package>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Package v_package = new Package();
                    v_package.set_packageName(node.getChildText("NAME"));
                    v_package.set_lineCount(Integer.parseInt(node.getChildText("LINE_COUNT")));
                    v_listPackage.add(v_package);
                }
                v_baseCompare.set_listPackage(v_listPackage);
                //
                // Lendo XML para os objetos PACKAGE_BODY
            } else if (v_objects[j].equals("PACKAGE_BODY")) {
                List<Package_Body> v_listPackage_Body = new ArrayList<Package_Body>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Package_Body v_package = new Package_Body();
                    v_package.setNAME(node.getChildText("NAME"));
                    v_package.setLINE_COUNT(Integer.parseInt(node.getChildText("LINE_COUNT")));
                    v_listPackage_Body.add(v_package);
                }
                v_baseCompare.set_listPackage_Body(v_listPackage_Body);
                //
                // Lendo XML para os objetos PROCEDURE
            } else if (v_objects[j].equals("PROCEDURE")) {
                List<Procedure> v_listProcedure = new ArrayList<Procedure>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Procedure v_procedure = new Procedure();
                    v_procedure.set_procedureName(node.getChildText("NAME"));
                    v_procedure.setCARACTER_COUNT(Integer.parseInt(node.getChildText("CARACTER_COUNT")));
                    v_listProcedure.add(v_procedure);
                }
                v_baseCompare.set_listProcedure(v_listProcedure);
                //
                // Lendo XML para os objetos SEQUENCE
            } else if (v_objects[j].equals("SEQUENCE")) {
                List<Sequence> v_listSequence = new ArrayList<Sequence>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Sequence v_sequence = new Sequence();
                    v_sequence.set_sequenceName(node.getChildText("SEQUENCE_NAME"));
                    v_listSequence.add(v_sequence);
                }
                v_baseCompare.set_listSequence(v_listSequence);
                //
                // Lendo XML para os objetos TRIGGER
            } else if (v_objects[j].equals("TRIGGER")) {
                List<Trigger> v_listTrigger = new ArrayList<Trigger>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Trigger v_trigger = new Trigger();
                    v_trigger.set_triggerName(node.getChildText("TRIGGER_NAME"));
                    v_trigger.set_triggerType(node.getChildText("TRIGGER_TYPE"));
                    v_trigger.set_triggerEvent(node.getChildText("TRIGGERING_EVENT"));
                    v_trigger.set_tableName(node.getChildText("TABLE_NAME"));
                    v_trigger.setCARACTER_COUNT(Integer.parseInt(node.getChildText("CARACTER_COUNT")));
                    v_listTrigger.add(v_trigger);
                }
                v_baseCompare.set_listTrigger(v_listTrigger);
                //
                // Lendo XML para os objetos VIEW
            } else if (v_objects[j].equals("VIEW")) {
                List<View> v_listView = new ArrayList<View>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    View v_view = new View();
                    v_view.set_viewName(node.getChildText("VIEW_NAME"));
                    v_view.set_textLength(Integer.parseInt(node.getChildText("TEXT_LENGTH")));
                    v_listView.add(v_view);
                }
                v_baseCompare.set_listView(v_listView);
                //
                // Lendo XML para os objetos FUNCTION
            } else if (v_objects[j].equals("FUNCTION")) {
                List<Function> v_listFunction = new ArrayList<Function>();
                for (int i = 0; i < list.size(); i++) {
                    node = list.get(i);
                    Function v_function = new Function();
                    v_function.set_functionName(node.getChildText("FUNCTION_NAME"));
                    v_function.setCARACTER_COUNT(Integer.parseInt(node.getChildText("CARACTER_COUNT")));
                    v_listFunction.add(v_function);
                }
                v_baseCompare.set_listFunction(v_listFunction);
            }
        }
        return v_baseCompare;
    }
}
