package moca.user_center.utils.gen_code;

public class GenCodeForUserDao {

    public static void main(String[] args) {

        GenCodeForDao genCodeForDao = new GenCodeForDao();

        genCodeForDao.add("id", "int");
        genCodeForDao.add("name", "varchar");
        genCodeForDao.add("password", "varchar");
        genCodeForDao.add("dateCreated", "timestamp");

        genCodeForDao.genCode();
    }
}
