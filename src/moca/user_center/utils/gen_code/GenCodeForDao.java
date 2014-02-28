package moca.user_center.utils.gen_code;

import java.util.ArrayList;
import java.util.List;

public class GenCodeForDao {

    public static enum DatabaseFieldType {

        INT("int", "int"), //
        BIGINT("bigint", "long"), //
        FLOAT("float", "float"), //
        CHAR("char", "String"), //
        VARCHAR("varchar", "String"), //
        TEXT("text", "String"), //
        MEDIUMTEXT("mediumtext", "String"), //
        TIMESTAMP("timestamp", "Timestamp");

        private String type;
        private String javaType;

        private DatabaseFieldType(String type, String javaType) {
            this.type = type;
            this.javaType = javaType;
        }

        public String getType() {
            return type;
        }

        public String getJavaType() {
            return javaType;
        }

        public static DatabaseFieldType parse(String databaseType) {
            for (DatabaseFieldType item : values()) {
                if (item.getType().equals(databaseType)) {
                    return item;
                }
            }
            return null;
        }

    }

    private List<String> fieldNames = new ArrayList<String>();
    private List<String> fieldTypes = new ArrayList<String>();

    public void add(String fieldName, String fieldType) {
        fieldNames.add(fieldName);
        fieldTypes.add(fieldType);
    }

    public void genCode() {

        System.out.println("==================================");
        for (String fieldName : fieldNames) {
            System.out.println("public static final String FIELD_NAME_" + fieldName.toUpperCase() + " = \"" + fieldName + "\";");
        }

        System.out.println("==================================");
        for (int i = 0; i < fieldTypes.size(); i++) {
            System.out.println("private " + DatabaseFieldType.parse(fieldTypes.get(i)).getJavaType() + " " + fieldNames.get(i) + ";");
        }
    }

}
